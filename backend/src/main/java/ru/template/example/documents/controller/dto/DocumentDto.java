package ru.template.example.documents.controller.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    /**
     * Номер
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
     * Дата документа
     */
    @NotNull
    private Date date;
    /**
     * Статус
     */
    @Valid
    private Status status;

}
