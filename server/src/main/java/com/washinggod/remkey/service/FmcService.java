package com.washinggod.remkey.service;

import com.google.firebase.messaging.*;
import com.washinggod.remkey.dto.request.NotificationTokenCreationRequest;
import com.washinggod.remkey.entity.NotificationToken;
import com.washinggod.remkey.entity.User;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.repository.NotificationTokenRepository;
import com.washinggod.remkey.repository.UserRepository;
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

  UserRepository userRepository;

  public void sendPushNotification(String token, String title, String body) {

    Message message =
        Message.builder()
            .setToken(token)
            .setNotification(Notification.builder().setTitle(title).setBody(body).build())
            .build();

    try {
      FirebaseMessaging.getInstance().send(message);
    } catch (FirebaseMessagingException exception) {
      log.error("ERROR: Failed in sending notification to token: {}", token);
      log.error("Cause by: {}", exception.getMessage());
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

    NotificationToken notificationToken = new NotificationToken();
    notificationToken.setUser(this.getCurrentUser());
    notificationToken.setToken(request.getToken());

    try {
      this.notificationTokenRepository.save(notificationToken);
    } catch (DataIntegrityViolationException e) {
      throw new AppException(ErrorCode.SAVE_NOTIFICATION_TOKEN_FAILED);
    }
  }


  private User getCurrentUser(){

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    String userId = auth.getName();

    return this.userRepository.findById(userId).orElseThrow(()-> {
      return new AppException(ErrorCode.USER_NOT_EXIST);
    });
  }
}
