package com.washinggod.remkey.repository;

import com.washinggod.remkey.dto.projection.NotificationDTO;
import com.washinggod.remkey.entity.CardUser;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardUserRepository extends JpaRepository<CardUser, Long> {

  @Query(
      """
                    SELECT c
                    FROM CardUser c
                    WHERE c.nextReview <= :now
                    AND c.userId = :userId
                    """)
  public List<CardUser> findStudyCardNow(
      @Param("now") LocalDateTime now, @Param("userId") String userId);

  public List<CardUser> findByUserId(String userId);

  @Query(
      value =
          """
                  SELECT new com.washinggod.remkey.dto.projection.NotificationDTO(c.userId, COUNT(c.id))
                  FROM CardUser c
                  WHERE c.nextReview <= :now AND( c.notificationTime <= :limitTime OR c.notificationTime IS NULL) GROUP BY c.userId
                  """)
  public List<NotificationDTO> findCardToNotification(
      @Param("now") LocalDateTime now, @Param("limitTime") LocalDateTime limitTime);

  @Query(
      """
            SELECT c
            FROM CardUser c
            WHERE c.nextReview <= :now
            """)
  public List<CardUser> findAllStudyCardNow(@Param("now") LocalDateTime now);

  @Transactional
  @Modifying
  @Query(
      """
         UPDATE CardUser c SET c.notificationTime = :now WHERE c.userId = :userId
         """)
  public void updateNotificationTimeByUserId(
      @Param("userId") String userId, @Param("now") LocalDateTime now);
}
