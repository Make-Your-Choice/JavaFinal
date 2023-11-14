package ru.template.example.documents.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Data
@Builder
@Table(name = "message_in")
public class MessageIn {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 3000)
    private String payload;
    @Column
    private Boolean isAccepted;
}
