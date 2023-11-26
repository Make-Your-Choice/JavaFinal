package ru.template.example.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.template.example.documents.entity.MessageOut;

import java.util.Optional;

/**
 * Класс репозиторий для работы со исходящими сообщениями
 */
@Repository
public interface MessageOutRepository extends JpaRepository<MessageOut, Long> {
    /**
     * Поиск первого неотправленного сообщения
     *
     * @return сообщение
     */
    Optional<MessageOut> findFirstByIsSentFalse();
}
