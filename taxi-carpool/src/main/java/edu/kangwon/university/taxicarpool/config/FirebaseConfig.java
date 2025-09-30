package edu.kangwon.university.taxicarpool.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private static final String FIREBASE_CONFIG_PATH = "knu-carpool-firebase-adminsdk-fbsvc-6dfb3c3cdb.json";

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        // Firebase 서비스 계정 키 파일 로드
        GoogleCredentials googleCredentials = GoogleCredentials
            .fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream());

        // Firebase 옵션 설정
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
            .setCredentials(googleCredentials)
            .build();

        // Firebase 앱 초기화 (이미 초기화된 경우 기존 앱 사용)
        FirebaseApp firebaseApp;
        if (FirebaseApp.getApps().isEmpty()) {
            firebaseApp = FirebaseApp.initializeApp(firebaseOptions);
        } else {
            firebaseApp = FirebaseApp.getInstance();
        }

        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
