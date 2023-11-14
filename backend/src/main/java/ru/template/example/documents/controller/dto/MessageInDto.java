package ru.template.example.documents.controller.dto;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageInDto {
    private Long id;
    private String payload;
    private Boolean isAccepted;
}
