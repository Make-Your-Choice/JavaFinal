package ru.template.example.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.template.example.documents.entity.MessageIn;

import java.util.Optional;

/**
 * Класс репозиторий для работы со входящими сообщениями
 */
@Repository
public interface MessageInRepository extends JpaRepository<MessageIn, Long> {
    /**
     * Поиск первого непринятого сообщения
     *
     * @return сообщение
     */
    Optional<MessageIn> findFirstByIsAcceptedFalse();

    /**
     * Поиск по тексту сообщения
     *
     * @return сообщение
     */
    Optional<MessageIn> findFirstByPayloadEquals(String payload);
}
