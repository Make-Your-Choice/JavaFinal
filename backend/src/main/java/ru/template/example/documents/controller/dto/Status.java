package ru.template.example.documents.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Status {
    @NotNull
    private String code;
    @NotNull
    private String name;

    public static Status of(String code, String name) {
        Status codeName = new Status();
        codeName.setCode(code);
        codeName.setName(name);
        return codeName;
    }
}
