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
  final double[] DEFAULT_WEIGHT = {
    0.4025, 0.9482, 2.1875, 5.6773, 4.9371, 0.944, 0.866, 0.012, 1.629, 0.1441, 2.1382, 0.3181,
    0.3979, 2.0958, 0.7404, 1.2244, 2.0
  };

  final double DEFAULT_RATING = 2;

  final double DEFAULT_RETRIEVABILITY = 1;

  final double DEFAULT_TARGET = 0.9;

  final double DEFAULT_PARAM = 9;

  public void initValue(CardUser cardUser) {

    double stability = DEFAULT_WEIGHT[(int) DEFAULT_RATING - 1];
    Double retrievability = DEFAULT_RETRIEVABILITY;
    Double difficulty = DEFAULT_WEIGHT[4] - DEFAULT_WEIGHT[5] * (DEFAULT_RATING - 3.0);

    cardUser.setRetrievability(retrievability);
    cardUser.setDifficulty(difficulty);
    cardUser.setStability(stability);
    cardUser.setLastReview(LocalDateTime.now());

    cardUser.setCreatedAt(LocalDateTime.now());

    //    parse to second
    long secondToAdd = (long) (stability * 24 * 60 * 60);

    LocalDateTime nextReview = LocalDateTime.now().plusSeconds(secondToAdd);

    cardUser.setNextReview(nextReview);
  }

  public double updateRetrievability(double stability, LocalDateTime last_review) {

    Duration timeDuration = Duration.between(last_review, LocalDateTime.now());
    double time = (double) timeDuration.toSeconds() / 60.0 / 60.0 / 24.0;

    return Math.pow(1 + (time / (DEFAULT_PARAM * stability)), -1);
  }

  public double updateStability(
      int rating, double oldStability, double oldRetrievability, double newDifficulty) {

    //        rating = again Stability will decrease
    if (rating == 1) {
      return DEFAULT_WEIGHT[11]
          * Math.pow(newDifficulty, -DEFAULT_WEIGHT[12])
          * Math.pow(oldStability + 1, -DEFAULT_WEIGHT[13])
          * Math.exp(DEFAULT_WEIGHT[14] * (1 - oldRetrievability));
    }

    double factor = this.getFactor(rating);
    double stability =
        oldStability
            * (1
                + factor
                    * Math.exp(DEFAULT_WEIGHT[8])
                    * (11 - newDifficulty)
                    * Math.pow(oldStability, -DEFAULT_WEIGHT[9])
                    * Math.exp(DEFAULT_WEIGHT[10] * (1 - oldRetrievability) - 1));

    return Math.max(0.1, stability);
  }

  public double updateDifficulty(double oldDifficulty, int rating) {
    double difficulty = oldDifficulty - DEFAULT_WEIGHT[6] * (rating - 3);

    double w4 = DEFAULT_WEIGHT[4];
    double w7 = DEFAULT_WEIGHT[7];
    difficulty = w4 * w7 + difficulty * (1 - w7);

    return Math.max(1, Math.min(10, difficulty));
  }

  public double calculateNextReview(Double stability) {
    return stability * DEFAULT_PARAM * (1.0 / DEFAULT_TARGET - 1);
  }

  private double getFactor(int rating) {
    return switch (rating) {
      case 2 -> DEFAULT_WEIGHT[15];
      case 4 -> DEFAULT_WEIGHT[16];
      default -> 1.0;
    };
  }
}
