package ru.template.example.configuration;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.controller.dto.MessageInDto;
import ru.template.example.documents.controller.dto.MessageOutDto;
import ru.template.example.documents.controller.dto.Status;
import ru.template.example.documents.service.DocumentService;
import ru.template.example.documents.service.MessageInService;
import ru.template.example.documents.service.MessageOutService;
import ru.template.example.kafka.KafkaProducer;

import java.util.Optional;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfiguration {

    private final KafkaProducer producer;
    private final MessageOutService messageOutService;
    private final MessageInService messageInService;
    private final DocumentService documentService;

    @Scheduled(fixedDelay = 2000)
    public void checkOutMessages() {
        if(!messageOutService.findAll().isEmpty()) {
            Optional<MessageOutDto> messageOutDto = messageOutService.getFirstNotSent();
            if(messageOutDto.isPresent()) {
                MessageOutDto messageOut = messageOutDto.get();
                producer.sendMessage(messageOut.getPayload());
                messageOut.setIsSent(true);
                messageOutService.save(messageOut);
            }
        }
    }

    @Scheduled(fixedDelay = 2000)
    public void checkInMessages() {
        if(!messageInService.findAll().isEmpty()) {
            Optional<MessageInDto> messageInDto = messageInService.getFirstNotAccepted();
            if(messageInDto.isPresent()) {
                MessageInDto messageIn = messageInDto.get();
                JSONObject data = new JSONObject(messageIn.getPayload());
                Long id = data.getLong("id");
                DocumentDto document = documentService.get(id);
                switch (data.get("status").toString()) {
                    case "ACCEPTED": {
                        document.setStatus(Status.of("ACCEPTED", "Принят"));
                    } break;
                    case "REJECTED": {
                        document.setStatus(Status.of("REJECTED", "Отклонен"));
                    } break;
                }
                documentService.update(document);
                messageIn.setIsAccepted(true);
                messageInService.save(messageIn);
            }
        }
    }
}
