package com.washinggod.remkey.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FireBaseConfiguration {
  @Bean
  FirebaseApp firebaseApp() throws IOException {

    ClassPathResource resource =
        new ClassPathResource("firebase/remkey-001-firebase-adminsdk-fbsvc-bc498b2749.json");
    InputStream inputStream = resource.getInputStream();

    FirebaseOptions firebaseOptions =
        FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(inputStream)).build();

    return FirebaseApp.initializeApp(firebaseOptions);
  }
}
