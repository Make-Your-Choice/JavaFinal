package ru.template.example.documents.service;

import ru.template.example.documents.controller.dto.MessageInDto;

import java.util.List;
import java.util.Optional;

/**
 * Сервис по работе со входящими сообщениями
 */
public interface MessageInService {
    /**
     * Сохранить сообщение
     *
     * @param messageInDto сообщение
     * @return сохраненное сообщение
     */
    MessageInDto save(MessageInDto messageInDto);

    /**
     * Получить все сообщения
     *
     * @return список сообщений
     */
    List<MessageInDto> findAll();

    /**
     * Получить сообщение по номеру
     *
     * @param id идентификатор
     * @return сообщение
     */
    Optional<MessageInDto> get(Long id);

    /**
     * Получить первое непринятое сообщение
     *
     * @return сообщение или null
     */
    Optional<MessageInDto> getFirstNotAccepted();

    /**
     * Получить сообщение по тексту
     *
     * @return сообщение или null
     */
    Optional<MessageInDto> getFirstByPayload(String payload);
}
