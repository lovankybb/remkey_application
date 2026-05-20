package com.washinggod.remkey.service;

import com.washinggod.remkey.entity.CardUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class FSRSServiceTest {

    CardUser cardUserInitRequest;

    LocalDateTime lockedNow;

    @Autowired
    FSRSService fsrsService;

    @BeforeEach
    void initData() {

        lockedNow = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
        cardUserInitRequest = CardUser.builder()
                .question("Hello")
                .answer("Xin chao")
                .build();

    }


    @Test
//    Check whether the algorithm works well
    void initCardUser__success() {

        CardUser resp = fsrsService.initValue(cardUserInitRequest, lockedNow);

        Assertions.assertThat(resp.getQuestion()).isEqualTo("Hello");
        Assertions.assertThat(resp.getAnswer()).isEqualTo("Xin chao");
        Assertions.assertThat(resp.getDifficulty()).isEqualTo(4.93);
        Assertions.assertThat(resp.getRetrievability()).isEqualTo(1.0);
        Assertions.assertThat(resp.getStability()).isEqualTo(2.4);
//        next Review is 2026-01-03T09:35:59
        final LocalDateTime nextReviewResult = LocalDateTime.of(2026, 01, 03, 9, 35, 59);
        Assertions.assertThat(resp.getNextReview()).isEqualTo(nextReviewResult);
    }

    @Test
    void updateRetrievable__success() {

        final LocalDateTime lockedNowToUpdateRetrievable = LocalDateTime.of(2026, 01, 03, 9, 35, 59);
        final LocalDateTime lockedLastReview = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
        final double stability = 2.4;

        final double resp = fsrsService.updateRetrievability(stability, lockedLastReview, lockedNowToUpdateRetrievable);

        Assertions.assertThat(resp).isEqualTo(0.9000004340279871);
    }


    @Test
    void updateStability__notForgetCase() {

//        not forget case the rating is 2, 3, 4
        final int rating = 3;
        final double oldStability = 2.4;
        final double oldDifficulty = 4.93;
        final  double oldRetrievable = 1.0;

        final double resp = fsrsService.updateStability(rating,oldStability, oldRetrievable, oldDifficulty);

        Assertions.assertThat(resp).isEqualTo(55.78328979163894);
    }

    @Test
    void updateStability__forgetCase() {

//        not forget case the rating is 2, 3, 4
        final int rating = 1;
        final double oldStability = 2.4;
        final double oldDifficulty = 4.93;
        final  double oldRetrievable = 1.0;

        final double resp = fsrsService.updateStability(rating,oldStability, oldRetrievable, oldDifficulty);

        Assertions.assertThat(resp).isEqualTo(1.327734731396139);
    }

    @Test
    void updateDifficulty__success(){
        final int rating = 3;
        final double oldDifficulty = 4.93;

        final double resp = fsrsService.updateDifficulty(oldDifficulty, rating);

        Assertions.assertThat(resp ).isEqualTo(4.93);
    }
}
