package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.NotificationToken;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {

  @Transactional
  @Modifying
  @Query("DELETE FROM NotificationToken n WHERE n.token = :token")
  void deleteByFcmToken(@Param("token") String token);

  List<NotificationToken> findByUserId(String userId);
}
