package ru.template.example.documents.service;

import ru.template.example.documents.controller.dto.MessageInDto;

import java.util.List;
import java.util.Optional;

public interface MessageInService {
    MessageInDto save(MessageInDto messageInDto);
    void delete(Long id);
    MessageInDto update(MessageInDto messageInDto);
    List<MessageInDto> findAll();
    MessageInDto get(Long id);
    Optional<MessageInDto> getFirstNotAccepted();
}
