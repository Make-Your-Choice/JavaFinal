package ru.template.example.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.logging.Level;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {

        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send("docs_in", message);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error sending message");
            }
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Success sending message");
            }
        });
    }
}
