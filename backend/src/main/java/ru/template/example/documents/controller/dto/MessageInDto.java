package ru.template.example.documents.controller.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * Класс дто входящего сообщения (то, что отправляется пользователем из кафки вручную)
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageInDto {
    /**
     * Идентификатор
     */
    @NotNull
    private Long id;
    /**
     * Текст сообщения
     */
    @NotNull
    private String payload;
    /**
     * Флаг принятого сообщения
     */
    @NotNull
    private Boolean isAccepted;
}
