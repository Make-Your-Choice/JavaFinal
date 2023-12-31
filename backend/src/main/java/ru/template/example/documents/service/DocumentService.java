package ru.template.example.documents.service;

import ru.template.example.documents.controller.dto.DocumentDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Сервис по работе с документами
 */
public interface DocumentService {
    /**
     * Сохранить документ
     *
     * @param documentDto документ
     * @return сохраненный документ
     */
    DocumentDto save(DocumentDto documentDto);

    /**
     * Удалить документ
     *
     * @param ids идентификаторы документов
     */
    void deleteAll(Set<Long> ids);

    /**
     * Удалить документ по ид
     *
     * @param id идентификатор документа
     */
    void delete(Long id);

    /**
     * Обновить документ
     *
     * @param documentDto документ
     * @return обновленный документ
     */
    DocumentDto update(DocumentDto documentDto);

    /**
     * Получить все документы
     *
     * @return список документов
     */
    List<DocumentDto> findAll();

    /**
     * Получить документ по номеру
     *
     * @param id идентификатор
     * @return документ
     */
    Optional<DocumentDto> get(Long id);

    /**
     * Отправить документ на обработку в кафку
     *
     * @param id идентификатор
     * @return обновленный документ
     */
    DocumentDto send(Long id);

    /**
     * Проверить документ по id
     *
     * @param id идентификатор
     * @return true или false в зависимости от результата поиска
     */
    boolean checkDocumentById(Long id);

}
