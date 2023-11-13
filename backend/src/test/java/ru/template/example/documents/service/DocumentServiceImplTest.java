package ru.template.example.documents.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.template.example.documents.controller.dto.DocumentDto;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentServiceImplTest {
    @Autowired
    private DocumentServiceImpl documentService;

    @BeforeEach
    public void before() {
        /*DocumentStore.getInstance().getDocumentDtos().clear();*/
    }

    @Test
    public void saveGetTest() {
        long id = 5L;
        documentService.save(new DocumentDto() {{
            setId(id);
        }});

        DocumentDto documentDto = documentService.get(id);
        assertEquals(id, documentDto.getId());
    }

    @Test
    void update() {
        DocumentDto documentDto = new DocumentDto() {{
            setId(5L);
        }};
        documentService.save(documentDto);

        documentDto.setDescription("test");
        documentService.update(documentDto);

        DocumentDto documentDtoFromSystem = documentService.get(5L);
        assertEquals("test", documentDtoFromSystem.getDescription());
    }

    @Test
    void delete() {
        documentService.save(new DocumentDto() {{
            setId(5L);
        }});

        documentService.delete(5L);
        assertEquals(0, documentService.findAll().size());
    }

    @Test
    void deleteAll() {
        documentService.save(new DocumentDto() {{
            setId(1L);
        }});
        documentService.save(new DocumentDto() {{
            setId(2L);
        }});

        documentService.deleteAll(Set.of(1L, 2L));
        assertEquals(0, documentService.findAll().size());
    }

    @Test
    void findAll() {
        documentService.save(new DocumentDto() {{
            setId(1L);
        }});
        documentService.save(new DocumentDto() {{
            setId(2L);
        }});

        Map<Long, DocumentDto> allDocumentMap = documentService.findAll()
                .stream()
                .collect(Collectors.toMap(DocumentDto::getId, Function.identity()));
        assertEquals(2, allDocumentMap.size());
        assertNotNull(allDocumentMap.get(1L));
        assertNotNull(allDocumentMap.get(2L));
        assertNull(allDocumentMap.get(3L));
    }

    @Test
    void getWhenNotExistsTest() {
        Assertions.assertThrows(IllegalStateException.class, () -> documentService.get(5L));
    }
}
