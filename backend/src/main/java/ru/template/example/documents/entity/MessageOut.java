package ru.template.example.documents.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Класс сущности исходящего сообщения (то, что отправляется сервером в кафку автоматически)
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Data
@Builder
@Table(name = "message_out")
public class MessageOut {
    /**
     * Идентификатор (первичный ключ)
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Название раздела
     */
    @Column(length = 1000)
    private String topic;
    /**
     * Текст сообщения
     */
    @Column(length = 3000)
    private String payload;
    /**
     * Флаг отправленного сообщения
     */
    @Column
    private Boolean isSent;
}
