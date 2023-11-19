package ru.template.example.kafka;

import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.template.example.documents.controller.dto.MessageInDto;
import ru.template.example.documents.entity.MessageIn;
import ru.template.example.documents.service.MessageInService;

import java.util.Stack;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private Stack<JSONObject> messages = new Stack<>();
    private final MessageInService messageInService;

    // containerFactory - не обязательный параметр, нужен если хочется дополнительно иметь какие то настройки
    @KafkaListener(topics = "docs_out", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload String message) throws JSONException {
        if(!messageInService.getFirstByPayload(message).isPresent()) {
            MessageInDto messageIn = new MessageInDto();
            messageIn.setPayload(message);
            messageInService.save(messageIn);
        }
    }
}