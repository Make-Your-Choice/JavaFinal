package ru.template.example.documents.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.controller.dto.MessageOutDto;
import ru.template.example.documents.controller.dto.Status;
import ru.template.example.documents.entity.Document;
import ru.template.example.documents.repository.DocumentRepository;

import java.util.*;

/**
 * Реализация сервиса по работе с документами
 */
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    /**
     * Репозиторий для работы с документами
     */
    private final DocumentRepository documentRepository;
    /**
     * Маппер
     */
    private final ModelMapper modelMapper = new ModelMapper();
    /**
     * Сервис обработки исходящих сообщений
     */
    private final MessageOutService messageOutService;

    /**
     * Сохранить документ
     *
     * @param documentDto документ
     * @return сохраненный документ
     */
    @Override
    public DocumentDto save(DocumentDto documentDto) {
        if (documentDto.getId() == null) {
            documentDto.setId(RandomUtils.nextLong(0L, 999L));
        }
        documentDto.setDate(new Date());
        if (documentDto.getStatus() == null) {
            documentDto.setStatus(Status.of("NEW", "Новый"));
        }
        Document entity = modelMapper.map(documentDto, Document.class);
        entity.setStatus(documentDto.getStatus().getCode());
        documentRepository.save(entity);
        return documentDto;
    }

    /**
     * Обновить документ
     *
     * @param documentDto документ
     * @return обновленный документ
     */
    @Override
    public DocumentDto update(DocumentDto documentDto) {
        Optional<DocumentDto> dto = get(documentDto.getId());
        if (dto.isPresent()) {
            save(documentDto);
        }
        return documentDto;
    }

    /**
     * Удалить документ по ид
     *
     * @param id идентификатор документа
     */
    @Override
    public void delete(Long id) {
        if (documentRepository.findById(id).isPresent()) {
            documentRepository.deleteById(id);
        }
    }

    /**
     * Удалить документ
     *
     * @param ids идентификаторы документов
     */
    @Override
    public void deleteAll(Set<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    /**
     * Получить все документы
     *
     * @return список документов
     */
    @Override
    public List<DocumentDto> findAll() {
        List<Document> documents = documentRepository.findAll();
        List<DocumentDto> documentDtos = new ArrayList<>();
        for (Document document : documents) {
            DocumentDto dto = modelMapper.map(document, DocumentDto.class);
            documentDtos.add(setDtoStatus(document.getStatus(), dto));
        }
        return documentDtos;
    }

    /**
     * Получить документ по номеру
     *
     * @param id идентификатор
     * @return документ
     */
    @Override
    public Optional<DocumentDto> get(Long id) {
        Optional<Document> document = documentRepository.findById(id);
        if (document.isPresent()) {
            Document doc = document.get();
            DocumentDto dto = modelMapper.map(doc, DocumentDto.class);
            return Optional.ofNullable(setDtoStatus(doc.getStatus(), dto));
        }
        return Optional.empty();

    }

    /**
     * Отправить документ на обработку в кафку
     *
     * @param id идентификатор
     * @return обновленный документ
     */
    @Override
    public DocumentDto send(Long id) {
        Optional<DocumentDto> dto = get(id);
        if (dto.isPresent()) {
            MessageOutDto messageOut = new MessageOutDto();
            messageOut.setPayload(new Gson().toJson(dto.get()));
            messageOutService.save(messageOut);
            return save(setDtoStatus("IN_PROCESS", dto.get()));
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Установить статус документа
     *
     * @param status статус (латинницей)
     * @param dto    документ
     * @return обновленный документ
     */
    public DocumentDto setDtoStatus(String status, DocumentDto dto) {
        switch (status) {
            case "NEW": {
                dto.setStatus(Status.of("NEW", "Новый"));
            }
            break;
            case "IN_PROCESS": {
                dto.setStatus(Status.of("IN_PROCESS", "В обработке"));
            }
            break;
            case "ACCEPTED": {
                dto.setStatus(Status.of("ACCEPTED", "Принят"));
            }
            break;
            case "REJECTED": {
                dto.setStatus(Status.of("REJECTED", "Отклонен"));
            }
            break;
        }
        return dto;
    }

    /**
     * Проверить документ по id
     *
     * @param id идентификатор
     * @return true или false в зависимости от результата поиска
     */
    @Override
    public boolean checkDocumentById(Long id) {
        return documentRepository.findById(id).isPresent();
    }
}
