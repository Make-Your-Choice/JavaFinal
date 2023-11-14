package ru.template.example.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.template.example.documents.entity.MessageIn;

import java.util.Optional;

@Repository
public interface MessageInRepository extends JpaRepository<MessageIn, Long> {
    Optional<MessageIn> findFirstByIsAcceptedFalse();
}
