package ru.template.example.documents.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Класс сущности входящего сообщения (те, что отправляются пользователем из кафки вручную)
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Data
@Builder
@Table(name = "message_in")
public class MessageIn {
    /**
     * Идентификатор (первичный ключ)
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Текст сообщения
     */
    @Column(length = 3000)
    private String payload;
    /**
     * Флаг принятого сообщения
     */
    @Column
    private Boolean isAccepted;
}
