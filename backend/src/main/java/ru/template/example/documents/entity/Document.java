package ru.template.example.documents.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Класс сущности документа
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Data
@Builder
@Table(name = "document")
public class Document {
    /**
     * Идентификатор (первичный ключ)
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Вид документа
     */
    @Column(length = 100)
    private String type;
    /**
     * Организация
     */
    @Column(length = 1000)
    private String organization;
    /**
     * Дата изменения документа
     */
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
    /**
     * Описание
     */
    @Column(length = 2000)
    private String description;
    /**
     * Пациент
     */
    @Column(length = 100)
    private String patient;
    /**
     * Статус
     */
    @Column(length = 100)
    private String status;
}
