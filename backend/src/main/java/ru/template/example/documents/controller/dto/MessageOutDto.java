package ru.template.example.documents.controller.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * Класс дто исходящего сообщения (то, что отправляется сервером в кафку автоматически)
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageOutDto {
    /**
     * Идентификатор
     */
    @NotNull
    private Long id;
    /**
     * Название раздела
     */
    @NotNull
    private String topic;
    /**
     * Текст сообщения
     */
    @NotNull
    private String payload;
    /**
     * Флаг отправленного сообщения
     */
    @NotNull
    private Boolean isSent;
}
