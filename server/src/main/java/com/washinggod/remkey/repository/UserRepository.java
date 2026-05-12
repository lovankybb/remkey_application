package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  public Optional<User> findByUsername(String username);

  public Optional<User> findByEmail(String email);

  public boolean existsByUsername(String username);

  public boolean existsByEmail(String email);
}
