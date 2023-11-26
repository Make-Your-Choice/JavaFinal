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
import ru.template.example.documents.entity.MessageIn;
import ru.template.example.documents.repository.MessageInRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса по работе со входящими сообщениями
 */
@Service
@RequiredArgsConstructor
public class MessageInServiceImpl implements MessageInService {
    /**
     * Репозиторий для работы со входящими сообщениями
     */
    private final MessageInRepository messageInRepository;
    /**
     * Маппер
     */
    private final ModelMapper modelMapper = new ModelMapper();
    /**
     * Репозиторий для работы с документами
     */
    private final DocumentService documentService;

    /**
     * Сохранить сообщение
     *
     * @param messageInDto сообщение
     * @return сохраненное сообщение
     */
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

    /**
     * Получить все сообщения
     *
     * @return список сообщений
     */
    @Override
    public List<MessageInDto> findAll() {
        List<MessageIn> messages = messageInRepository.findAll();
        List<MessageInDto> messageInDtos = new ArrayList<>();
        for (MessageIn message : messages) {
            messageInDtos.add(modelMapper.map(message, MessageInDto.class));
        }
        return messageInDtos;
    }

    /**
     * Получить сообщение по номеру
     *
     * @param id идентификатор
     * @return сообщение
     */
    @Override
    public Optional<MessageInDto> get(Long id) {
        Optional<MessageIn> message = messageInRepository.findById(id);
        if (message.isPresent()) {
            MessageIn messageIn = message.get();
            return Optional.ofNullable(modelMapper.map(messageIn, MessageInDto.class));
        }
        return Optional.empty();
    }

    /**
     * Получить первое непринятое сообщение
     *
     * @return сообщение или null
     */
    @Override
    public Optional<MessageInDto> getFirstNotAccepted() {
        Optional<MessageIn> message = messageInRepository.findFirstByIsAcceptedFalse();
        return Optional.ofNullable(modelMapper.map(message, MessageInDto.class));
    }

    /**
     * Получить сообщение по тексту
     *
     * @return сообщение или null
     */
    @Override
    public Optional<MessageInDto> getFirstByPayload(String payload) {
        Optional<MessageIn> message = messageInRepository.findFirstByPayloadEquals(payload);
        return Optional.ofNullable(modelMapper.map(message, MessageInDto.class));
    }

    /**
     * Проверить входящие сообщения
     * если есть непринятые - обработать их
     * (обновить статус указанного документа в соответвии с текстом сообщения)
     * пометить сообщение как принятое
     */
    @Scheduled(fixedDelay = 2000)
    public void checkInMessages() {
        if (!findAll().isEmpty()) {
            Optional<MessageInDto> messageInDto = getFirstNotAccepted();
            if (messageInDto.isPresent()) {
                MessageInDto messageIn = messageInDto.get();
                JSONObject data = new JSONObject(messageIn.getPayload());
                Long id = data.getLong("id");
                DocumentDto document = documentService.get(id).get();
                switch (data.get("status").toString()) {
                    case "ACCEPTED": {
                        document.setStatus(Status.of("ACCEPTED", "Принят"));
                    }
                    break;
                    case "REJECTED": {
                        document.setStatus(Status.of("REJECTED", "Отклонен"));
                    }
                    break;
                }
                documentService.update(document);
                messageIn.setIsAccepted(true);
                save(messageIn);
            }
        }
    }
}
