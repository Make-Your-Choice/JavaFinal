package ru.template.example.documents.service;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils;
import ru.template.example.Application;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.controller.dto.Status;
import ru.template.example.documents.entity.Document;
import ru.template.example.documents.repository.DocumentRepository;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DocumentServiceImplTest {
    //@Autowired
    //@InjectMocks
    private DocumentService documentService;
    //@Mock
    @Autowired
    private DocumentRepository documentRepository;

    @BeforeEach
    public void before() {
        //documentRepository = mock(DocumentRepository.class);
        documentService = new DocumentServiceImpl(documentRepository);
        documentRepository.deleteAll();
    }

    @Test
    public void saveGetTest() {
        DocumentDto dto = new DocumentDto(1L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        //when(documentRepository.save(any(Document.class))).thenReturn(any());
        DocumentDto saved = documentService.save(dto);
        //verify(documentRepository).save(any(Document.class));
        assertNotNull(saved);
        assertEquals(dto.getId(), saved.getId());
        assertEquals(dto.getType(), saved.getType());
        assertEquals(dto.getOrganization(), saved.getOrganization());
        assertEquals(dto.getDescription(), saved.getDescription());
        assertEquals(dto.getPatient(), saved.getPatient());
        assertEquals(dto.getDate(), saved.getDate());
        assertEquals(dto.getStatus(), saved.getStatus());
        //documentService.delete(1L);
    }

    @Test
    public void update() {
        DocumentDto dto = new DocumentDto(2L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        //when(documentRepository.save(any(Document.class))).thenReturn(any());
        documentService.save(dto);
        dto.setDescription("test");
        DocumentDto updated = documentService.update(dto);
        //verify(documentRepository).save(any(Document.class));
        assertEquals(dto.getDescription(), updated.getDescription());
        //documentService.delete(2L);
    }

    @Test
    public void send() {
        DocumentDto dto = new DocumentDto(8L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        documentService.save(dto);
        DocumentDto sent = documentService.send(dto);
        assertEquals(Status.of("IN_PROCESS", "В обработке"), sent.getStatus());
    }

    @Test
    public void delete() {
        DocumentDto dto = new DocumentDto(3L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        documentService.delete(dto.getId());
        assertEquals(0, documentService.findAll().size());
    }

    @Test
    public void deleteAll() {
        DocumentDto dto1 = new DocumentDto(4L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        DocumentDto dto2 = new DocumentDto(5L, "type2", "organization2", "description2", "patient2", new Date(), Status.of("NEW", "Новый"));
        documentService.deleteAll(Set.of(4L, 5L));
        assertEquals(0, documentService.findAll().size());
    }

    @Test
    public void findAll() {
        DocumentDto dto1 = new DocumentDto(6L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        DocumentDto dto2 = new DocumentDto(7L, "type2", "organization2", "description2", "patient2", new Date(), Status.of("NEW", "Новый"));
        documentService.save(dto1);
        documentService.save(dto2);
        List<DocumentDto> documents = documentService.findAll();
        //verify(documentRepository).findAll();
        assertEquals(2, documents.size());
        assertNotNull(documents.get(0));
        assertNotNull(documents.get(1));
        //documentService.deleteAll(Set.of(6L, 7L));
    }

    /*@Test
    void getWhenNotExistsTest() {
        Assertions.assertThrows(IllegalStateException.class, () -> documentService.get(5L));
    }*/
}
