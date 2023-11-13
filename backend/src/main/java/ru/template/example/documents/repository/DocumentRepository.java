package ru.template.example.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.template.example.documents.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
