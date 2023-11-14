package ru.template.example.documents.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.template.example.documents.controller.dto.MessageOutDto;
import ru.template.example.documents.entity.MessageOut;
import ru.template.example.documents.repository.MessageOutRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageOutServiceImpl implements MessageOutService {

    private final MessageOutRepository messageOutRepository;
    private final ModelMapper modelMapper = new ModelMapper();

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

    @Override
    public void delete(Long id) {
        if(messageOutRepository.findById(id).isPresent()) {
            messageOutRepository.deleteById(id);
        }
    }

    @Override
    public MessageOutDto update(MessageOutDto messageOutDto) {
        MessageOutDto dto = get(messageOutDto.getId());
        if (dto != null) {
            save(messageOutDto);
        }
        return messageOutDto;
    }

    @Override
    public List<MessageOutDto> findAll() {
        List<MessageOut> messages = messageOutRepository.findAll();
        List<MessageOutDto> messageOutDtos = new ArrayList<>();
        for(MessageOut message: messages) {
            messageOutDtos.add(modelMapper.map(message, MessageOutDto.class));
        }
        return messageOutDtos;
    }

    @Override
    public MessageOutDto get(Long id) {
        MessageOut message = messageOutRepository.getOne(id);
        return modelMapper.map(message, MessageOutDto.class);
    }

    @Override
    public Optional<MessageOutDto> getFirstNotSent() {
        Optional<MessageOut> message = messageOutRepository.findFirstByIsSentFalse();
        return Optional.ofNullable(modelMapper.map(message, MessageOutDto.class));
    }
}
