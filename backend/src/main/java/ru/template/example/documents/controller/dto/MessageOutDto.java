package ru.template.example.documents.controller.dto;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageOutDto {
    private Long id;
    private String topic;
    private String payload;
    private Boolean isSent;
}
