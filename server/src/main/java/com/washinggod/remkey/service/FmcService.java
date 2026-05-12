package com.washinggod.remkey.service;

import com.google.firebase.messaging.*;
import com.washinggod.remkey.dto.request.NotificationTokenCreationRequest;
import com.washinggod.remkey.entity.NotificationToken;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.repository.NotificationTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FmcService {

  NotificationTokenRepository notificationTokenRepository;

  public void sendPushNotification(String token, String title, String body) {

    Message message =
        Message.builder()
            .setToken(token)
            .setNotification(Notification.builder().setTitle(title).setBody(body).build())
            .build();

    try {
      FirebaseMessaging.getInstance().send(message);
      log.info("Send notification to user successfully");
    } catch (FirebaseMessagingException exception) {
      this.handleFireBaseMessagingException(token, exception);
    }
  }

  private void handleFireBaseMessagingException(String token, FirebaseMessagingException e) {

    MessagingErrorCode errMessage = e.getMessagingErrorCode();

    if (errMessage == MessagingErrorCode.UNREGISTERED
        || errMessage == MessagingErrorCode.INVALID_ARGUMENT) {
      notificationTokenRepository.deleteByFcmToken(token);
    }
  }

  public void createNotificationToken(NotificationTokenCreationRequest request) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    String userId = auth.getName();

    NotificationToken notificationToken = new NotificationToken();
    notificationToken.setUserId(userId);
    notificationToken.setToken(request.getToken());

    try {
      this.notificationTokenRepository.save(notificationToken);
    } catch (DataIntegrityViolationException e) {
      throw new AppException(ErrorCode.SAVE_NOTIFICATION_TOKEN_FAILED);
    }
  }
}
