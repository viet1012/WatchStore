package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.PasswordResetToken;
import com.ecommerce.WatchStore.Entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    @Transactional
    void deleteByUser(User user);
}
