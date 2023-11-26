package ru.template.example.documents.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.controller.dto.IdDto;
import ru.template.example.documents.controller.dto.IdsDto;
import ru.template.example.documents.service.DocumentService;

import java.util.List;

/**
 * Класс контроллер документов
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocumentController {

    /**
     * Сервис обработки документов
     */
    private final DocumentService service;

    /**
     * Сохранение документа
     *
     * @param dto документ
     * @return сохраненный документ
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DocumentDto save(@RequestBody DocumentDto dto) {
        return service.save(dto);
    }

    /**
     * Вывод списка документов
     *
     * @return список документов
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DocumentDto> get() {
        return service.findAll();
    }

    /**
     * Отправка документа на обработку в кафку
     *
     * @param id идентификатор документа
     * @return обновленный документ
     */
    @PostMapping(
            path = "send",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DocumentDto send(@RequestBody IdDto id) {
        return service.send(id.getId());
    }

    /**
     * Удаление документа
     *
     * @param id идентификатор документа
     */
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    /**
     * Удаление нескольких документов
     *
     * @param idsDto идентификаторы документов
     */
    @DeleteMapping
    public void deleteAll(@RequestBody IdsDto idsDto) {
        service.deleteAll(idsDto.getIds());
    }
}
