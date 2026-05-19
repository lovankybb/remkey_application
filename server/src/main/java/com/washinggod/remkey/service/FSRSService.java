package com.washinggod.remkey.service;

import com.washinggod.remkey.entity.CardUser;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FSRSService {

    //    WARNING: Do NOT change the order of this array
    final double[] WEIGHT =
            {0.4, 0.6, 2.4, 5.8, 4.93, 0.94, 0.86, 0.01, 1.49, 0.14, 0.94, 2.18, 0.05, 0.34, 1.26, 0.29, 2.61};

    //    Init value with rating GOOD => rating = 3
    final double FIRST_RATING = 3;

    final double FIRST_RETRIEVABILITY = 1;

    final double DEFAULT_TARGET = 0.9;

    final double DEFAULT_PARAM = 9;

    public CardUser initValue(CardUser cardUser, LocalDateTime now) {

        double stability = WEIGHT[(int) FIRST_RATING - 1];
        Double retrievability = FIRST_RETRIEVABILITY;
        Double difficulty = WEIGHT[4] - WEIGHT[5] * (FIRST_RATING - 3.0);

        cardUser.setRetrievability(retrievability);
        cardUser.setDifficulty(difficulty);
        cardUser.setStability(stability);
        cardUser.setLastReview(now);

        cardUser.setCreatedAt(now);

        //    parse to second
        long secondToAdd = (long) (stability * 24 * 60 * 60);

        LocalDateTime nextReview = now.plusSeconds(secondToAdd);

        cardUser.setNextReview(nextReview);

        return cardUser;
    }

    public double updateRetrievability(double stability, LocalDateTime last_review, LocalDateTime now) {

        Duration timeDuration = Duration.between(last_review, now);
        double time = (double) timeDuration.toSeconds() / 60.0 / 60.0 / 24.0;

        return Math.pow(1 + (time / (DEFAULT_PARAM * stability)), -1);
    }

    public double updateStability(
            int rating, double oldStability, double oldRetrievability, double newDifficulty) {

        //        rating = again Stability will decrease
        if (rating == 1) {
            return WEIGHT[11]
                    * Math.pow(newDifficulty, -WEIGHT[12])
                    * Math.pow(oldStability + 1, -WEIGHT[13])
                    * Math.exp(WEIGHT[14] * (1 - oldRetrievability));
        }

        double factor = this.getFactor(rating);
        double stability =
                oldStability
                        * (Math.exp(WEIGHT[8])
                        * (11 - newDifficulty)
                        * Math.pow(oldStability, -WEIGHT[9])
                        * Math.exp(WEIGHT[10] * (1 - oldRetrievability)) - 1)
                        * factor
                        + 1;

        return Math.max(0.1, stability);
    }

    public double updateDifficulty(double oldDifficulty, int rating) {
        double difficulty = oldDifficulty - WEIGHT[6] * (rating - 3);

        double w4 = WEIGHT[4];
        double w7 = WEIGHT[7];
        difficulty = w4 * w7 + difficulty * (1 - w7);

        return Math.max(1, Math.min(10, difficulty));
    }

    public double calculateNextReview(Double stability) {
        return stability * DEFAULT_PARAM * (1.0 / DEFAULT_TARGET - 1);
    }

    private double getFactor(int rating) {
        return switch (rating) {
            case 2 -> WEIGHT[15];
            case 4 -> WEIGHT[16];
            default -> 1.0;
        };
    }
}
