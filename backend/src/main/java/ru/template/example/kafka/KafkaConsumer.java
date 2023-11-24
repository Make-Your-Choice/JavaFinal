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
import java.util.Stack;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private Stack<JSONObject> messages = new Stack<>();
    private final MessageInService messageInService;
    private final DocumentService documentService;

    // containerFactory - не обязательный параметр, нужен если хочется дополнительно иметь какие то настройки
    @KafkaListener(topics = "docs_out", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload String message) throws JSONException, InstanceAlreadyExistsException {
        if(!messageInService.getFirstByPayload(message).isPresent()) {
            JSONObject data = new JSONObject(message);
            if(documentService.checkDocumentById(data.getLong("id"))) {
                if(data.get("status").equals("ACCEPTED") ||
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
    }
}