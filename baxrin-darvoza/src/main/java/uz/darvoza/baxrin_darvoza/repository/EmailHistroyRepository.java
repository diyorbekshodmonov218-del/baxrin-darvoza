package uz.darvoza.baxrin_darvoza.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import uz.darvoza.baxrin_darvoza.entity.EmailHistroyEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailHistroyRepository extends JpaRepository<EmailHistroyEntity,String> {
    Long countByEmailAndCreatedDateBetween(String email, LocalDateTime from, LocalDateTime to);

    Optional<EmailHistroyEntity> findTop1ByEmailOrderByCreatedDateDesc(String email);

    @Transactional
    @Modifying
    @Query("UPDATE EmailHistroyEntity e SET e.attemptCount = e.attemptCount + 1 WHERE e.id = :id ")
    void updateAttemptCount(String id);
}
