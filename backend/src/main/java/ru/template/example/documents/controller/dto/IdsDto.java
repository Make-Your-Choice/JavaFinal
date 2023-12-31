package ru.template.example.documents.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Класс дто id документов
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdsDto {
    /**
     * Идентификаторы
     */
    @NotNull
    private Set<Long> ids;

}
