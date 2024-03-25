package com.trs.tickets.repository;

import com.trs.tickets.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameContainingIgnoreCase(String username);
    Optional<User> findByUsername(String username);

    Page<User> findByUsernameNotIn(List<String> excludeUsername, Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCaseAndUsernameNotIn(String username, List<String> excludeUsername, Pageable pageable);

    List<User> findAllByRegistrationDateBetween(LocalDate start, LocalDate end);
}
