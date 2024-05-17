package com.trs.tickets.repository;

import com.trs.tickets.model.entity.PasswordResetToken;
import com.trs.tickets.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    @Transactional
    void deleteByUser(User user);
}