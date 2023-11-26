package ru.template.example.documents.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.template.example.documents.controller.dto.MessageOutDto;
import ru.template.example.documents.entity.MessageOut;
import ru.template.example.documents.repository.MessageOutRepository;
import ru.template.example.kafka.KafkaProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса по работе с исходящими сообщениями
 */
@Service
@RequiredArgsConstructor
public class MessageOutServiceImpl implements MessageOutService {
    /**
     * Репозиторий для работы с исходящими сообщениями
     */
    private final MessageOutRepository messageOutRepository;
    /**
     * Маппер
     */
    private final ModelMapper modelMapper = new ModelMapper();
    /**
     * Отправитель сообщений в кафку
     */
    private final KafkaProducer producer;

    /**
     * Сохранить сообщение
     *
     * @param messageOutDto сообщение
     * @return сохраненное сообщение
     */
    @Override
    public MessageOutDto save(MessageOutDto messageOutDto) {
        if (messageOutDto.getId() == null) {
            messageOutDto.setId(RandomUtils.nextLong(0L, 999L));
        }
        if (messageOutDto.getTopic() == null) {
            messageOutDto.setTopic("docs_in");
        }
        if (messageOutDto.getIsSent() == null) {
            messageOutDto.setIsSent(false);
        }
        MessageOut entity = modelMapper.map(messageOutDto, MessageOut.class);
        messageOutRepository.save(entity);
        return messageOutDto;
    }

    /**
     * Получить все сообщения
     *
     * @return список сообщений
     */
    @Override
    public List<MessageOutDto> findAll() {
        List<MessageOut> messages = messageOutRepository.findAll();
        List<MessageOutDto> messageOutDtos = new ArrayList<>();
        for (MessageOut message : messages) {
            messageOutDtos.add(modelMapper.map(message, MessageOutDto.class));
        }
        return messageOutDtos;
    }

    /**
     * Получить сообщение по номеру
     *
     * @param id идентификатор
     * @return сообщение
     */
    @Override
    public MessageOutDto get(Long id) {
        MessageOut message = messageOutRepository.getOne(id);
        return modelMapper.map(message, MessageOutDto.class);
    }

    /**
     * Получить первое неотправленное сообщение
     *
     * @return сообщение или null
     */
    @Override
    public Optional<MessageOutDto> getFirstNotSent() {
        Optional<MessageOut> message = messageOutRepository.findFirstByIsSentFalse();
        return Optional.ofNullable(modelMapper.map(message, MessageOutDto.class));
    }

    /**
     * Проверить исходящие сообщения
     * если есть неотправленные - отправить их
     * (передать сообщение отправителю)
     * пометить сообщение как отправленное
     */
    @Scheduled(fixedDelay = 2000)
    public void checkOutMessages() {
        if (!findAll().isEmpty()) {
            Optional<MessageOutDto> messageOutDto = getFirstNotSent();
            if (messageOutDto.isPresent()) {
                MessageOutDto messageOut = messageOutDto.get();
                producer.sendMessage(messageOut.getPayload());
                messageOut.setIsSent(true);
                save(messageOut);
            }
        }
    }
}
