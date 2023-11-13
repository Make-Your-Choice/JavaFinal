package ru.template.example.documents.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Data
@Builder
@Table(name = "document")
public class Document {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String type;
    @Column(length = 1000)
    private String organization;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @Column(length = 2000)
    private String description;
    @Column(length = 100)
    private String patient;
    @Column(length = 100)
    private String status;
}
