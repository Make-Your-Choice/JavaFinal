package ru.template.example.documents.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Класс дто статуса документа
 */
@Data
public class Status {
    /**
     * Код (латинницей)
     */
    @NotNull
    private String code;
    /**
     * Название (кириллицей)
     */
    @NotNull
    private String name;

    /**
     * Задание статуса
     *
     * @param code код
     * @param name название
     * @return новый статус
     */
    public static Status of(String code, String name) {
        Status codeName = new Status();
        codeName.setCode(code);
        codeName.setName(name);
        return codeName;
    }
}
