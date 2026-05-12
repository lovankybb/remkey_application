package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.Language;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

  Optional<Language> findByName(String name);
}
