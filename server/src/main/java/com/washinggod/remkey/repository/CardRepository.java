package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.Card;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

  @Query(
      """
            SELECT c
            FROM Card c
            WHERE c.question LIKE %:pattern%
            """)
  public List<Card> findByQuestionPattern(@Param("pattern") String pattern);

  @Query(
      """
            SELECT c
            FROM Card c
            WHERE c.topic.id = :topic_id
            """)
  public List<Card> findByTopic(@Param("topic_id") Long topicId);

  @Query(
      """
            SELECT c
            FROM Card c
            WHERE c.language.id = :language_id
            """)
  public List<Card> findByLanguage(@Param("language_id") Long languageId);

  @Query("""
        SELECT c FROM Card c ORDER BY c.id DESC
        """)
  public Slice<Card> findNewestCard(Pageable pageable);

  @Query("""
        SELECT c FROM Card c WHERE  c.id < :latestId ORDER BY c.id DESC
        """)
  public Slice<Card> findNextCardById(@Param("latestId") Long latestId, Pageable pageable);
}
