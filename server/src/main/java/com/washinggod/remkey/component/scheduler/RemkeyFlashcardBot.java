package com.washinggod.remkey.component.scheduler;

import com.washinggod.remkey.dto.projection.NotificationDTO;
import com.washinggod.remkey.entity.CardUser;
import com.washinggod.remkey.entity.NotificationToken;
import com.washinggod.remkey.entity.User;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.repository.CardUserRepository;
import com.washinggod.remkey.repository.NotificationTokenRepository;
import com.washinggod.remkey.repository.UserRepository;
import com.washinggod.remkey.service.FmcService;
import com.washinggod.remkey.util.MailService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RemkeyFlashcardBot {

  NotificationTokenRepository notificationTokenRepository;

  CardUserRepository cardUserRepository;

  Long DEFAULT_NOTIFICATION_LIMIT_TIME = 1L;

  FmcService fmcService;

  UserRepository userRepository;

  MailService mailService;

  @Scheduled(fixedRate = 600000) // default scheduled time is 600000, it equals 10 minutes
  @Transactional
  public void scanCardUsers() {

    LocalDateTime limitTime = LocalDateTime.now().minusHours(DEFAULT_NOTIFICATION_LIMIT_TIME);
    LocalDateTime now = LocalDateTime.now();
    List<NotificationDTO> notifications = cardUserRepository.findCardToNotification(now, limitTime);

    if (notifications.isEmpty()) return;

    notifications.forEach(
        notification -> {
          String email = this.getUser(notification.userId()).getEmail();
          List<CardUser> cardUsers =
              cardUserRepository.findStudyCardNow(LocalDateTime.now(), notification.userId());
          this.sendNotificationEmail(cardUsers, email);

          List<NotificationToken> notificationTokens =
              this.getNotificationTokenByUserId(notification.userId());

          notificationTokens.forEach(
              notificationToken -> {
                fmcService.sendPushNotification(
                    notificationToken.getToken(),
                    this.getNotificationTitle(),
                    this.getNotificationBody(notification.cardCount()));
              });

          cardUserRepository.updateNotificationTimeByUserId(notification.userId(), now);
        });
  }

  private String getNotificationTitle() {

    return """
                It's time to practice.
                """;
  }

  private List<NotificationToken> getNotificationTokenByUserId(String userId) {

    return notificationTokenRepository.findByUserId(userId);
  }

  private String getNotificationBody(Long quantity) {

    String card = (quantity > 1) ? "card" : "cards";
    String body =
        """
                Right now, you have QUANTITY CARD to review.
                """;
    return body.replace("QUANTITY", String.valueOf(quantity)).replace("CARD", card);
  }

  private User getUser(String userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.USER_NOT_EXIST);
            });
  }

  private void sendNotificationEmail(List<CardUser> cardUsers, String receiver) {
    String title = this.getEmailTitle();
    String body = this.getEmailBody(cardUsers);
      mailService.sendEmail(receiver, title, body);

  }

  private String getEmailTitle() {
    return "It's time to practice";
  }

  private String getEmailBody(List<CardUser> cardUsers) {

    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append(
        """
                        <!DOCTYPE html>
                        <html lang="en">

                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        </head>

                        <body style="display: flex; justify-content: center;">
                            <table style="width: 1000px; border-collapse: collapse; font-family: sans-serif;">
                                <thead>
                                    <tr style="background-color: #4CAF50; color: white;">
                                        <th style="padding: 12px; border: 1px solid #ddd; text-align: left;">Câu hỏi (Front)</th>
                                        <th style="padding: 12px; border: 1px solid #ddd; text-align: left;">Đáp án (Back)</th>
                                    </tr>
                                </thead>
                                <tbody>
                        """);

    for (CardUser c : cardUsers) {

      String cardHtml =
          """
                            <tr>
                                <td style="padding: 10px; border: 1px solid #ddd; vertical-align: top;">QUESTION</td>
                                <td style="padding: 10px; border: 1px solid #ddd; color: #555;">
                                    <details>
                                        <summary style="cursor: pointer; color: #2196F3;">Xem đáp án</summary>
                                        <div style="margin-top: 5px;">ANSWER</div>
                                    </details>
                                </td>
                            </tr>
                            """;
      stringBuilder.append(
          cardHtml.replace("QUESTION", c.getQuestion()).replace("ANSWER", c.getAnswer()));
    }

    stringBuilder.append(
        """
                         </tbody>
                            </table>
                        </body>
                        </html>
                        """);
    return stringBuilder.toString();
  }
}
