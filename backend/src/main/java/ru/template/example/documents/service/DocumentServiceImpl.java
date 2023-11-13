package ru.template.example.documents.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.controller.dto.Status;
import ru.template.example.documents.entity.Document;
import ru.template.example.documents.repository.DocumentRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    public DocumentDto save(DocumentDto documentDto) {
        if (documentDto.getId() == null) {
            documentDto.setId(RandomUtils.nextLong(0L, 999L));
        }
        documentDto.setDate(new Date());
        if (documentDto.getStatus() == null) {
            documentDto.setStatus(Status.of("NEW", "Новый"));
        }
        Document entity = modelMapper.map(documentDto, Document.class);
        entity.setStatus(documentDto.getStatus().getCode());
        documentRepository.save(entity);
        return documentDto;
    }


    public DocumentDto update(DocumentDto documentDto) {
        //List<DocumentDto> documentDtos = mapperFacade.mapAsList(documentRepository.findAll(), DocumentDto.class);
        /*Optional<DocumentDto> dto = documentDtos.stream()
                .filter(d -> d.getId().equals(documentDto.getId())).findFirst();*/
        DocumentDto dto = get(documentDto.getId());
        if (dto != null) {
            save(documentDto);
        }
        return documentDto;
    }

    public void delete(Long id) {
        if(documentRepository.findById(id).isPresent()) {
            documentRepository.deleteById(id);
        }
    }

    public void deleteAll(Set<Long> ids) {
        for (Long id: ids) {
            delete(id);
        }
    }

    public List<DocumentDto> findAll() {
        List<Document> documents = documentRepository.findAll();
        List<DocumentDto> documentDtos = new ArrayList<>();
        for(Document document: documents) {
            DocumentDto dto = modelMapper.map(document, DocumentDto.class);
            documentDtos.add(setDtoStatus(document.getStatus(), dto));
        }
        return documentDtos;
    }

    public DocumentDto get(Long id) {
        Document document = documentRepository.getOne(id);
        DocumentDto dto = modelMapper.map(document, DocumentDto.class);
        return setDtoStatus(document.getStatus(), dto);
        //return mapperFacade.map(documentRepository.getOne(id), DocumentDto.class);
        /*List<DocumentDto> documentDtos = DocumentStore.getInstance().getDocumentDtos();
        return documentDtos.stream()
                .filter(d -> d.getId().equals(id)).findFirst().orElseThrow(() -> new IllegalStateException("cannot find " + id));*/
    }

    public DocumentDto setDtoStatus(String status, DocumentDto dto) {
        switch (status) {
            case "NEW": {
                dto.setStatus(Status.of("NEW", "Новый"));
            } break;
            case "IN_PROCESS": {
                dto.setStatus(Status.of("IN_PROCESS", "В обработке"));
            } break;
            case "ACCEPTED": {
                dto.setStatus(Status.of("ACCEPTED", "Принят"));
            } break;
            case "REJECTED": {
                dto.setStatus(Status.of("REJECTED", "Отклонен"));
            } break;
        }
        return dto;
    }
}
