package ru.template.example.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {

        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send("docs_in", message);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("failure");
            }
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("success");
            }
        });
    }
}
