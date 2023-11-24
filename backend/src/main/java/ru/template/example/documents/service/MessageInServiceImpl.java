package ru.template.example.documents.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.controller.dto.MessageInDto;
import ru.template.example.documents.controller.dto.Status;
import ru.template.example.documents.entity.Document;
import ru.template.example.documents.entity.MessageIn;
import ru.template.example.documents.repository.MessageInRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageInServiceImpl implements MessageInService {

    private final MessageInRepository messageInRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final DocumentService documentService;

    @Override
    public MessageInDto save(MessageInDto messageInDto) {
        if (messageInDto.getId() == null) {
            messageInDto.setId(RandomUtils.nextLong(0L, 999L));
        }
        if (messageInDto.getIsAccepted() == null) {
            messageInDto.setIsAccepted(false);
        }
        MessageIn entity = modelMapper.map(messageInDto, MessageIn.class);
        messageInRepository.save(entity);
        return messageInDto;
    }

    @Override
    public List<MessageInDto> findAll() {
        List<MessageIn> messages = messageInRepository.findAll();
        List<MessageInDto> messageInDtos = new ArrayList<>();
        for(MessageIn message: messages) {
            messageInDtos.add(modelMapper.map(message, MessageInDto.class));
        }
        return messageInDtos;
    }

    @Override
    public Optional<MessageInDto> get(Long id) {
        Optional<MessageIn> message = messageInRepository.findById(id);
        if(message.isPresent()) {
            MessageIn messageIn = message.get();
            return Optional.ofNullable(modelMapper.map(messageIn, MessageInDto.class));
        }
        return Optional.empty();
    }

    @Override
    public Optional<MessageInDto> getFirstNotAccepted() {
        Optional<MessageIn> message = messageInRepository.findFirstByIsAcceptedFalse();
        return Optional.ofNullable(modelMapper.map(message, MessageInDto.class));
    }

    @Override
    public Optional<MessageInDto> getFirstByPayload(String payload) {
        Optional<MessageIn> message = messageInRepository.findFirstByPayloadEquals(payload);
        return Optional.ofNullable(modelMapper.map(message, MessageInDto.class));
    }

    @Scheduled(fixedDelay = 2000)
    public void checkInMessages() {
        if(!findAll().isEmpty()) {
            Optional<MessageInDto> messageInDto = getFirstNotAccepted();
            if(messageInDto.isPresent()) {
                MessageInDto messageIn = messageInDto.get();
                JSONObject data = new JSONObject(messageIn.getPayload());
                Long id = data.getLong("id");
                DocumentDto document = documentService.get(id).get();
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
                save(messageIn);
            }
        }
    }
}
