package ru.template.example.documents.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Класс дто id документа
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdDto {
    /**
     * Идентификатор
     */
    @NotNull
    private Long id;
}
