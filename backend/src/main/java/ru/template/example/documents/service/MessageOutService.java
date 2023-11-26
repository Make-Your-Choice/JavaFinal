package ru.template.example.documents.service;

import ru.template.example.documents.controller.dto.MessageOutDto;

import java.util.List;
import java.util.Optional;

/**
 * Сервис по работе с исходящими сообщениями
 */
public interface MessageOutService {
    /**
     * Сохранить сообщение
     *
     * @param messageOutDto сообщение
     * @return сохраненное сообщение
     */
    MessageOutDto save(MessageOutDto messageOutDto);

    /**
     * Получить все сообщения
     *
     * @return список сообщений
     */
    List<MessageOutDto> findAll();

    /**
     * Получить сообщение по номеру
     *
     * @param id идентификатор
     * @return сообщение
     */
    MessageOutDto get(Long id);

    /**
     * Получить первое неотправленное сообщение
     *
     * @return сообщение или null
     */
    Optional<MessageOutDto> getFirstNotSent();
}
