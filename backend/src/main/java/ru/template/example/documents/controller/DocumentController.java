package ru.template.example.documents.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.template.example.documents.controller.dto.*;
import ru.template.example.documents.service.DocumentService;
import ru.template.example.documents.service.MessageOutService;
import ru.template.example.kafka.KafkaConsumer;
import ru.template.example.kafka.KafkaProducer;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService service;
    private final KafkaProducer producer;
    private final KafkaConsumer consumer;
    private final MessageOutService messageOutService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DocumentDto save(@RequestBody DocumentDto dto) {
        return service.save(dto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DocumentDto> get() throws JSONException {
        /*if(!consumer.checkMessages()) {
            while(!consumer.checkMessages()) {
                JSONObject data = consumer.getRecentMessage();
                Long id = data.getLong("id");
                DocumentDto document = service.get(id);
                switch (data.get("status").toString()) {
                    case "ACCEPTED": {
                        document.setStatus(Status.of("ACCEPTED", "Принят"));
                    } break;
                    case "REJECTED": {
                        document.setStatus(Status.of("REJECTED", "Отклонен"));
                    } break;
                }
                service.update(document);
            }
        }*/
        return service.findAll();
    }

    @PostMapping(
            path = "send",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DocumentDto send(@RequestBody IdDto id) {
        DocumentDto document = service.get(id.getId());
        //producer.sendMessage(new Gson().toJson(document));
        document.setStatus(Status.of("IN_PROCESS", "В обработке"));
        MessageOutDto messageOut = new MessageOutDto();
        messageOut.setPayload(new Gson().toJson(document));
        messageOutService.save(messageOut);
        return service.update(document);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @DeleteMapping
    public void deleteAll(@RequestBody IdsDto idsDto) {
        service.deleteAll(idsDto.getIds());
    }

}
