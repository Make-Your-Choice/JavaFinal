package ru.template.example.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Класс отправителя сообщений
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    /**
     * Шаблон отправителя
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Отправка сообщения в раздел docs_in
     * при успехе/неудаче выводится соответствующее сообщение в лог
     *
     * @param message текст сообщения
     */
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
