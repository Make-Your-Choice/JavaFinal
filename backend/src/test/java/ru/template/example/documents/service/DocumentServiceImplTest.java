package ru.template.example.documents.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.controller.dto.Status;
import ru.template.example.documents.repository.DocumentRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Класс, тестирующий сервис по работе с документами
 */
@SpringBootTest
public class DocumentServiceImplTest {
    /**
     * Сервис по работе с документами
     */
    private DocumentServiceImpl documentService;
    /**
     * Репозиторий для рабоы с документами
     */
    @Autowired
    private DocumentRepository documentRepository;
    /**
     * Сервис по работе с исходящими сообщениями
     */
    @Autowired
    private MessageOutService messageOutService;

    /**
     * Инициализация
     */
    @BeforeEach
    public void before() {
        documentService = new DocumentServiceImpl(documentRepository, messageOutService);
        documentRepository.deleteAll();
    }

    /**
     * Тест на сохранение и получение документа
     */
    @Test
    public void saveGetTest() {
        DocumentDto dto = new DocumentDto(null, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        DocumentDto saved = documentService.save(dto);
        assertNotNull(saved);
        assertEquals(dto.getType(), saved.getType());
        assertEquals(dto.getOrganization(), saved.getOrganization());
        assertEquals(dto.getDescription(), saved.getDescription());
        assertEquals(dto.getPatient(), saved.getPatient());
        assertEquals(dto.getDate(), saved.getDate());
        assertEquals(dto.getStatus(), saved.getStatus());
    }

    /**
     * Тест на обновление документа
     */
    @Test
    public void update() {
        DocumentDto dto = new DocumentDto(null, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        documentService.save(dto);
        dto.setDescription("test");
        DocumentDto updated = documentService.update(dto);
        assertEquals(dto.getDescription(), updated.getDescription());
    }

    /**
     * Тест на отправку документа
     */
    @Test
    public void send() {
        DocumentDto dto = new DocumentDto(null, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        documentService.save(dto);
        Long id = documentService.findAll().get(0).getId();
        DocumentDto sent = documentService.send(id);
        assertEquals(Status.of("IN_PROCESS", "В обработке"), sent.getStatus());
    }

    /**
     * Тест на удаление документа
     */
    @Test
    public void delete() {
        DocumentDto dto = new DocumentDto(null, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        documentService.save(dto);
        Long id = documentService.findAll().get(0).getId();
        documentService.delete(id);
        assertEquals(0, documentService.findAll().size());
    }

    /**
     * Тест на удаление нескольких документов
     */
    @Test
    public void deleteAll() {
        DocumentDto dto1 = new DocumentDto(null, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        DocumentDto dto2 = new DocumentDto(null, "type2", "organization2", "description2", "patient2", new Date(), Status.of("NEW", "Новый"));
        documentService.save(dto1);
        documentService.save(dto2);
        Long id1 = documentService.findAll().get(0).getId();
        Long id2 = documentService.findAll().get(1).getId();
        documentService.deleteAll(Set.of(id1, id2));
        assertEquals(0, documentService.findAll().size());
    }

    /**
     * Тест на получение списка документов
     */
    @Test
    public void findAll() {
        DocumentDto dto1 = new DocumentDto(null, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        DocumentDto dto2 = new DocumentDto(null, "type2", "organization2", "description2", "patient2", new Date(), Status.of("NEW", "Новый"));
        documentService.save(dto1);
        documentService.save(dto2);
        List<DocumentDto> documents = documentService.findAll();
        assertEquals(2, documents.size());
        assertNotNull(documents.get(0));
        assertNotNull(documents.get(1));
    }
}
