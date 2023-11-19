package ru.template.example.documents.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class IdsDto {
    @NotNull
    private Set<Long> ids;

}
