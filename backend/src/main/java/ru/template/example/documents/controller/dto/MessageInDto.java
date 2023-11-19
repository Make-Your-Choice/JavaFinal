package ru.template.example.documents.controller.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageInDto {
    @NotNull
    private Long id;
    @NotNull
    private String payload;
    @NotNull
    private Boolean isAccepted;
}
