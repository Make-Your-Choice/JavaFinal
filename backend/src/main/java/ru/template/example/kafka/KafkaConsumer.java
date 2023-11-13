package ru.template.example.kafka;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
public class KafkaConsumer {
    Stack<JSONObject> messages = new Stack<>();

    // containerFactory - не обязательный параметр, нужен если хочется дополнительно иметь какие то настройки
    @KafkaListener(topics = "docs_out", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload String message) throws JSONException {
        JSONObject obj = new JSONObject(message);
        //if((obj.get("status").equals("ACCEPTED") || obj.get("status").equals("REJECTED")) && !checkContains(obj)) {
        if(!checkContains(obj)) {
            messages.push(obj);
        }
    }

    public JSONObject getRecentMessage() {
        return messages.pop();
    }

    public boolean checkMessages() {
        return messages.isEmpty();
    }

    public boolean checkContains(JSONObject object) {
        for(JSONObject obj: messages) {
            if (obj.get("id") == object.get("id") && obj.get("status").equals(object.get("status"))) {
                return true;
            }
        }
        return false;
    }
}