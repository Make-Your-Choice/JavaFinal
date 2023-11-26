package ru.template.example.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.template.example.documents.controller.dto.MessageInDto;
import ru.template.example.documents.service.DocumentService;
import ru.template.example.documents.service.MessageInService;

import javax.management.InstanceAlreadyExistsException;

/**
 * Класс получателя сообщений
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {
    /**
     * Сервис обработки входящих сообщений
     */
    private final MessageInService messageInService;
    /**
     * Сервис обработки документов
     */
    private final DocumentService documentService;

    /**
     * Обработка входящих сообщений
     * если сообщение имеет корректный формат, не дублирует существующее сообщение
     * и имеет корректные значения параметров, оно поступает на обработку
     * статус указанного документа изменяется в соответствие с текстом сообщения
     * сообщение помечается как принятое
     *
     * @param message текст сообщения
     * @throws JSONException
     * @throws InstanceAlreadyExistsException
     */
    @KafkaListener(topics = "docs_out", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload String message) throws JSONException, InstanceAlreadyExistsException {
        if (message.contains("id") && message.contains("status")) {
            if (!messageInService.getFirstByPayload(message).isPresent()) {
                JSONObject data = new JSONObject(message);
                if (documentService.checkDocumentById(data.getLong("id"))) {
                    if (data.get("status").equals("ACCEPTED") ||
                            data.get("status").equals("REJECTED")) {
                        MessageInDto messageIn = new MessageInDto();
                        messageIn.setPayload(message);
                        messageInService.save(messageIn);
                    } else {
                        log.error("Status " + data.getString("status") + " is not acceptable");
                        throw new IllegalArgumentException("Status " + data.getString("status") + " is not acceptable");
                    }
                } else {
                    log.error("Id " + data.getLong("id") + " does not exist");
                    throw new IllegalArgumentException("Id " + data.getLong("id") + " does not exist");
                }
            } else {
                log.error("Payload " + message + " already exists");
                throw new InstanceAlreadyExistsException("Payload " + message + " already exists");
            }
        } else {
            log.error("Incorrect payload format " + message);
            throw new IllegalArgumentException("Incorrect payload format " + message);
        }
    }
}