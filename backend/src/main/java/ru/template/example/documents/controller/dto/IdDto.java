package ru.template.example.documents.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IdDto {
    @NotNull
    private Long id;
}
