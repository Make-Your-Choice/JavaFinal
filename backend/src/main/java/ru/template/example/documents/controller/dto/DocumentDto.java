package ru.template.example.documents.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Класс дто документа
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    /**
     * Идентификатор
     */
    @NotNull
    private Long id;
    /**
     * Вид документа
     */
    @NotNull
    private String type;
    /**
     * Организация
     */
    @NotNull
    private String organization;
    /**
     * Описание
     */
    @NotNull
    private String description;
    /**
     * Пациент
     */
    @NotNull
    private String patient;
    /**
     * Дата изменения документа
     */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+7")
    private Date date;
    /**
     * Статус
     */
    @Valid
    private Status status;
}
