package ru.template.example.documents.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.template.example.documents.controller.dto.MessageOutDto;
import ru.template.example.documents.repository.MessageOutRepository;
import ru.template.example.kafka.KafkaProducer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MessageOutServiceImplTest {
    private MessageOutServiceImpl messageOutService;
    @Autowired
    private MessageOutRepository messageOutRepository;
    @Autowired
    private KafkaProducer producer;

    @BeforeEach
    public void before() {
        messageOutService = new MessageOutServiceImpl(messageOutRepository, producer);
        messageOutRepository.deleteAll();
    }

    @Test
    public void saveGetTest() {
        MessageOutDto dto = new MessageOutDto(1L, "docs_in", "{}", false);
        MessageOutDto saved = messageOutService.save(dto);
        assertNotNull(saved);
        assertEquals(dto.getId(), saved.getId());
        assertEquals(dto.getTopic(), saved.getTopic());
        assertEquals(dto.getPayload(), saved.getPayload());
        assertEquals(dto.getIsSent(), saved.getIsSent());
    }

    @Test
    public void findAll() {
        MessageOutDto dto1 = new MessageOutDto(1L, "docs_in", "{1}", false);
        MessageOutDto dto2 = new MessageOutDto(2L, "docs_in", "{2}", true);
        messageOutService.save(dto1);
        messageOutService.save(dto2);
        List<MessageOutDto> messages = messageOutService.findAll();
        assertEquals(2, messages.size());
        assertNotNull(messages.get(0));
        assertNotNull(messages.get(1));
    }

    @Test
    public void getFirstNotSent() {
        MessageOutDto dto1 = new MessageOutDto(1L, "docs_in", "{1}", false);
        MessageOutDto dto2 = new MessageOutDto(2L, "docs_in", "{2}", true);
        messageOutService.save(dto1);
        messageOutService.save(dto2);
        Optional<MessageOutDto> notSent = messageOutService.getFirstNotSent();
        assertTrue(notSent.isPresent());
        assertEquals(dto1.getTopic(), notSent.get().getTopic());
        assertEquals(dto1.getPayload(), notSent.get().getPayload());
        assertEquals(dto1.getIsSent(), notSent.get().getIsSent());
    }

    @Test
    public void checkOutMessages() {
        MessageOutDto message = new MessageOutDto(1L, "docs_in", "{}", false);
        messageOutService.save(message);
        messageOutService.checkOutMessages();
        MessageOutDto messageOut = messageOutService.findAll().get(0);
        assertNotNull(messageOut);
        assertTrue(messageOut.getIsSent());
    }
}
