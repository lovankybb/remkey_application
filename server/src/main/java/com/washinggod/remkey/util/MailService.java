package com.washinggod.remkey.util;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailService {


    @Value("${resend.api-key}")
    String apiKey;

    @Async
    public void sendEmail(String to, String subject, String text) {

        Resend resend = new Resend(apiKey);

        CreateEmailOptions email = CreateEmailOptions.builder()
                .from("no-reply@remkey.site")
                .to(to)
                .subject(subject)
                .html(text)
                .build();

        try {
            resend.emails().send(email);
        } catch (ResendException e) {
            log.error("ERROR: FAILED in sending email to {}", to);
            throw new AppException(ErrorCode.SEND_EMAIL_FAILED);
        }

    }
}
