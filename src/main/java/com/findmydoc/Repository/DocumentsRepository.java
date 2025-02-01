package com.findmydoc.Repository;

import com.findmydoc.Model.DocumentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentsRepository extends JpaRepository<DocumentDetails, Integer> {

    Optional<DocumentDetails> findBySerialNumberAndDocumentType(String docSerialNumber, String documentType);


    Optional<DocumentDetails> findByDocumentNumberAndDocumentType(String documentNumber, String documentType);

}
