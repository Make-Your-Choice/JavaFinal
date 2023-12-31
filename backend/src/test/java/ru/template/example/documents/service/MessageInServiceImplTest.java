package ru.template.example.documents.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.controller.dto.MessageInDto;
import ru.template.example.documents.controller.dto.Status;
import ru.template.example.documents.repository.MessageInRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс, тестирующий сервис по работе со входящими сообщениями
 */
@SpringBootTest
public class MessageInServiceImplTest {
    /**
     * Сервис по работе со входящими сообщениями
     */
    private MessageInServiceImpl messageInService;
    /**
     * Репозиторий для работы со входящими сообщениями
     */
    @Autowired
    private MessageInRepository messageInRepository;
    /**
     * Сервис по работе с документами
     */
    @Autowired
    private DocumentServiceImpl documentService;

    /**
     * Инициализация
     */
    @BeforeEach
    public void before() {
        messageInService = new MessageInServiceImpl(messageInRepository, documentService);
        messageInRepository.deleteAll();
    }

    /**
     * Тест на сохранение и получение входящего сообщения
     */
    @Test
    public void saveGetTest() {
        MessageInDto dto = new MessageInDto(1L, "{}", false);
        MessageInDto saved = messageInService.save(dto);
        assertNotNull(saved);
        assertEquals(dto.getId(), saved.getId());
        assertEquals(dto.getPayload(), saved.getPayload());
        assertEquals(dto.getIsAccepted(), saved.getIsAccepted());
    }

    /**
     * Тест на получение спписка входящих сообщений
     */
    @Test
    public void findAll() {
        MessageInDto dto1 = new MessageInDto(1L, "{1}", false);
        MessageInDto dto2 = new MessageInDto(2L, "{2}", true);
        messageInService.save(dto1);
        messageInService.save(dto2);
        List<MessageInDto> messages = messageInService.findAll();
        assertEquals(2, messages.size());
        assertNotNull(messages.get(0));
        assertNotNull(messages.get(1));
    }

    /**
     * Тест на получение первого непринятого входящего сообщения
     */
    @Test
    public void getFirstNotAccepted() {
        MessageInDto dto1 = new MessageInDto(1L, "{1}", false);
        MessageInDto dto2 = new MessageInDto(2L, "{2}", true);
        messageInService.save(dto1);
        messageInService.save(dto2);
        Optional<MessageInDto> notAccepted = messageInService.getFirstNotAccepted();
        assertTrue(notAccepted.isPresent());
        assertEquals(dto1.getPayload(), notAccepted.get().getPayload());
        assertEquals(dto1.getIsAccepted(), notAccepted.get().getIsAccepted());
    }

    /**
     * Тест на получение входящего сообщения по его тексту
     */
    @Test
    public void getFirstByPayload() {
        MessageInDto dto = new MessageInDto(1L, "{}", false);
        messageInService.save(dto);
        Optional<MessageInDto> messageByPayload = messageInService.getFirstByPayload(dto.getPayload());
        assertTrue(messageByPayload.isPresent());
        assertEquals(dto.getPayload(), messageByPayload.get().getPayload());
    }

    /**
     * Тест на обработку еще не принятых входящих сообщений
     */
    @Test
    public void checkInMessages() {
        DocumentDto documentDto = new DocumentDto(1L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        MessageInDto messageInDto = new MessageInDto(1L, "{\"id\":1, \"status\":\"ACCEPTED\"}", false);
        documentService.save(documentDto);
        messageInService.save(messageInDto);
        messageInService.checkInMessages();
        Optional<DocumentDto> accepted = documentService.get(1L);
        Optional<MessageInDto> acceptMessage = messageInService.getFirstByPayload(messageInDto.getPayload());
        assertTrue(accepted.isPresent());
        assertEquals(Status.of("ACCEPTED", "Принят"), accepted.get().getStatus());
        assertTrue(acceptMessage.isPresent());
        assertTrue(acceptMessage.get().getIsAccepted());
    }
}
