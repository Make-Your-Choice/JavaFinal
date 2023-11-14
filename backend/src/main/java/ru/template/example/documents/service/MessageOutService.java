package ru.template.example.documents.service;

import ru.template.example.documents.controller.dto.MessageOutDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MessageOutService {
    MessageOutDto save(MessageOutDto messageOutDto);
    void delete(Long id);
    MessageOutDto update(MessageOutDto messageOutDto);
    List<MessageOutDto> findAll();
    MessageOutDto get(Long id);
    Optional<MessageOutDto> getFirstNotSent();
}
