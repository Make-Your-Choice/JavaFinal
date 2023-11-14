package ru.template.example.documents.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Data
@Builder
@Table(name = "message_out")
public class MessageOut {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1000)
    private String topic;
    @Column(length = 3000)
    private String payload;
    @Column
    private Boolean isSent;
}
