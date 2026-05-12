package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.Topic;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

  Optional<Topic> findByName(String name);
}
