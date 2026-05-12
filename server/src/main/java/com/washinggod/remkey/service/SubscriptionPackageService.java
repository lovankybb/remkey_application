package com.washinggod.remkey.service;

import com.washinggod.remkey.dto.request.FindPackageByNameRequest;
import com.washinggod.remkey.dto.request.SubscriptionPackageCreationRequest;
import com.washinggod.remkey.dto.request.SubscriptionPackageUpdateRequest;
import com.washinggod.remkey.dto.response.SubscriptionPackageResponse;
import com.washinggod.remkey.entity.SubscriptionPackage;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.mapper.SubscriptionPackageMapper;
import com.washinggod.remkey.repository.PackageRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionPackageService {

  PackageRepository packageRepository;

  SubscriptionPackageMapper subscriptionPackageMapper;

  @PreAuthorize("hasRole('ADMIN')")
  public SubscriptionPackageResponse create(SubscriptionPackageCreationRequest request) {

    String packageName = request.getName().toUpperCase();
    log.info("INFO: Package's name: {}", packageName);
    SubscriptionPackage subscriptionPackage =
        subscriptionPackageMapper.toSubscriptionPackage(request);
    subscriptionPackage.setCreatedAt(LocalDateTime.now());
    subscriptionPackage.setName(packageName);
    return this.save(subscriptionPackage);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public SubscriptionPackageResponse update(Long id, SubscriptionPackageUpdateRequest request) {

    String packageName = request.getName().toUpperCase();
    SubscriptionPackage subscriptionPackage = this.getSubscriptionPackageById(id);
    subscriptionPackage.setName(packageName);

    subscriptionPackageMapper.updateSubscriptionPackage(subscriptionPackage, request);

    return this.save(subscriptionPackage);
  }

  public List<SubscriptionPackageResponse> getAll() {
    List<SubscriptionPackage> subscriptionPackages = packageRepository.findAll();
    return subscriptionPackages.stream()
        .map(subscriptionPackageMapper::toSubscriptionPackageResponse)
        .toList();
  }

  @PreAuthorize("hasRole('ADMIN')")
  public void delete(Long id) {
    packageRepository.delete(this.getSubscriptionPackageById(id));
  }

  public SubscriptionPackageResponse getSubscriptionPackageResponseById(Long id) {
    return subscriptionPackageMapper.toSubscriptionPackageResponse(
        this.getSubscriptionPackageById(id));
  }

  public SubscriptionPackageResponse getSubscriptionPackageResponseByName(
      FindPackageByNameRequest request) {
    return subscriptionPackageMapper.toSubscriptionPackageResponse(
        this.getSubscriptionPackageByName(request.getName()));
  }

  private SubscriptionPackageResponse save(SubscriptionPackage subscriptionPackage) {

    try {
      subscriptionPackage.setUpdatedAt(LocalDateTime.now());
      return subscriptionPackageMapper.toSubscriptionPackageResponse(
          packageRepository.save(subscriptionPackage));
    } catch (DataIntegrityViolationException exception) {
      throw new AppException(ErrorCode.SAVE_PACKAGE_FAILED);
    }
  }

  private SubscriptionPackage getSubscriptionPackageById(Long id) {
    return packageRepository
        .findById(id)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.USER_NOT_EXIST);
            });
  }

  private SubscriptionPackage getSubscriptionPackageByName(String name) {
    return packageRepository
        .findByName(name)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.USER_NOT_EXIST);
            });
  }
}
