package ru.template.example.documents.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.template.example.documents.controller.dto.MessageInDto;
import ru.template.example.documents.controller.dto.MessageOutDto;
import ru.template.example.documents.entity.MessageIn;
import ru.template.example.documents.entity.MessageOut;
import ru.template.example.documents.repository.MessageInRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageInServiceImpl implements MessageInService {

    private final MessageInRepository messageInRepository;
    private final ModelMapper modelMapper = new ModelMapper();

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
    public void delete(Long id) {
        if(messageInRepository.findById(id).isPresent()) {
            messageInRepository.deleteById(id);
        }
    }

    @Override
    public MessageInDto update(MessageInDto messageInDto) {
        MessageInDto dto = get(messageInDto.getId());
        if (dto != null) {
            save(messageInDto);
        }
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
    public MessageInDto get(Long id) {
        MessageIn message = messageInRepository.getOne(id);
        return modelMapper.map(message, MessageInDto.class);
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
}
