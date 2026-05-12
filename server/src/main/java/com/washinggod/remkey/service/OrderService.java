package com.washinggod.remkey.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.washinggod.remkey.dto.request.FindOrderByPaymentStatusRequest;
import com.washinggod.remkey.dto.request.OrderCreationRequest;
import com.washinggod.remkey.dto.request.PaymentRequest;
import com.washinggod.remkey.dto.response.OrderResponse;
import com.washinggod.remkey.dto.response.PaymentResponse;
import com.washinggod.remkey.dto.response.PaymentResult;
import com.washinggod.remkey.entity.Order;
import com.washinggod.remkey.entity.Subscription;
import com.washinggod.remkey.entity.SubscriptionPackage;
import com.washinggod.remkey.enums.PaymentMethod;
import com.washinggod.remkey.enums.PaymentStatus;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.mapper.OrderMapper;
import com.washinggod.remkey.repository.OrderRepository;
import com.washinggod.remkey.repository.PackageRepository;
import com.washinggod.remkey.repository.SubscriptionRepository;
import com.washinggod.remkey.util.RequestUtilService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {

  PackageRepository packageRepository;

  OrderRepository orderRepository;

  OrderMapper orderMapper;

  PaymentServiceFactory paymentServiceFactory;

  SubscriptionRepository subscriptionRepository;

  final int DEFAULT_USED_QUOTA = 0;

  public OrderResponse create(OrderCreationRequest request) {

    String userId = this.getCurrentUserId();

    log.info("INFO: User id: {}", userId);

    SubscriptionPackage subscriptionPackage =
        this.getSubscriptionPackage(request.getPackageIds().getFirst());

    Set<SubscriptionPackage> packages = new HashSet<>();
    packages.add(subscriptionPackage);
    Order order =
        Order.builder()
            .userId(userId)
            .packages(packages)
            .createdAt(LocalDateTime.now())
            .totalAmount(subscriptionPackage.getPrice())
            .paymentStatus(PaymentStatus.PENDING)
            .transactionDate(LocalDateTime.now())
            .build();

    return this.save(order);
  }

  public PaymentResponse checkout(
      PaymentRequest paymentRequest, HttpServletRequest servletRequest) {

    String clientIp = RequestUtilService.getIpAddress(servletRequest);

    Order order = this.getOrderById(paymentRequest.getOrderId());

    if (order.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
      throw new AppException(ErrorCode.ORDER_ALREADY_PAID);
    }

    PaymentService paymentService =
        paymentServiceFactory.getPaymentService(paymentRequest.getPaymentMethod());

    try {
      return paymentService.pay(order, clientIp);
    } catch (UnsupportedEncodingException | JsonProcessingException e) {
      throw new AppException(ErrorCode.PAYMENT_REQUEST_FAILED);
    }
  }

  @Transactional
  public Map<String, String> handlePaymentResult(
      PaymentResult paymentResult, PaymentMethod paymentMethod) {

    Order order = this.getOrderById(paymentResult.getOrderId());

    Optional<SubscriptionPackage> subscriptionPackageOptional =
        order.getPackages().stream().findFirst();

    if (subscriptionPackageOptional.isEmpty()) {
      throw new AppException(ErrorCode.PACKAGE_NOT_EXIST);
    } else {

      SubscriptionPackage subscriptionPackage = subscriptionPackageOptional.get();
      String userId = order.getUserId();

      Subscription subscription =
          subscriptionRepository
              .findByUserId(userId)
              .orElse(
                  Subscription.builder()
                      .userId(userId)
                      .createdAt(LocalDateTime.now())
                      .updatedAt(LocalDateTime.now())
                      .build());

      LocalDateTime expiredTime = LocalDateTime.now().plusDays(subscriptionPackage.getDuration());
      subscription.setExpiredTime(expiredTime);
      subscription.setQuota(DEFAULT_USED_QUOTA);
      subscription.setPackageId(subscriptionPackage.getId());
      subscription.setQuota(subscriptionPackage.getQuota());

      try {
        subscriptionRepository.save(subscription);
      } catch (DataIntegrityViolationException e) {
        throw new AppException(ErrorCode.SAVE_SUBSCRIPTION_FAILED);
      }

      order.setPaymentStatus(PaymentStatus.SUCCESS);

      order.setPaymentMethod(paymentMethod);

      this.save(order);

      PaymentService paymentService = paymentServiceFactory.getPaymentService(paymentMethod);

      return paymentService.handlePaymentResult(paymentResult);
    }
  }

  @PreAuthorize("hasRole('ADMIN')")
  public void delete(Long id) {
    Order order = this.getOrderById(id);
    orderRepository.delete(order);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public List<OrderResponse> getAll() {
    return orderRepository.findAll().stream().map(this::generateOrderResponse).toList();
  }

  public List<OrderResponse> getOrderByPaymentStatus(FindOrderByPaymentStatusRequest request) {

    String userId = this.getCurrentUserId();
    PaymentStatus paymentStatus = PaymentStatus.valueOf(request.getPaymentStatus());
    return orderRepository.getOrderByUserIdAndStatus(userId, paymentStatus).stream()
        .map(this::generateOrderResponse)
        .toList();
  }

  public OrderResponse cancel(Long id) {

    Order order = this.getOrderById(id);
    order.setPaymentStatus(PaymentStatus.CANCELED);
    return this.save(order);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public OrderResponse getOrderResponseById(Long id) {
    return this.generateOrderResponse(this.getOrderById(id));
  }

  public List<OrderResponse> getOrderResponsesByUserId() {
    return this.getOrderByUserId().stream().map(this::generateOrderResponse).toList();
  }

  private SubscriptionPackage getSubscriptionPackage(Long id) {
    return packageRepository
        .findById(id)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.PACKAGE_NOT_EXIST);
            });
  }

  private OrderResponse save(Order order) {
    try {
      order.setUpdatedAt(LocalDateTime.now());
      return this.generateOrderResponse(orderRepository.save(order));
    } catch (DataIntegrityViolationException exception) {
      throw new AppException(ErrorCode.SAVE_ORDER_FAILED);
    }
  }

  private OrderResponse generateOrderResponse(Order order) {
    OrderResponse orderResponse = orderMapper.toOrderResponse(order);

    Set<String> packages =
        order.getPackages().stream().map(SubscriptionPackage::getName).collect(Collectors.toSet());
    orderResponse.setPackages(packages);

    return orderResponse;
  }

  private Order getOrderById(Long id) {

    return orderRepository
        .findById(id)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.ORDER_NOT_EXIST);
            });
  }

  private String getCurrentUserId() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  private List<Order> getOrderByUserId() {
    String userId = this.getCurrentUserId();
    return orderRepository.getOrderByUserId(userId);
  }
}
