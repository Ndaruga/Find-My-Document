package com.findmydoc.Repository;

import com.findmydoc.Model.DocumentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<DocumentDetails, Integer> {

    boolean existsBySerialNumberAndDocumentType(String docSerialNumber, String documentType);

    boolean existsByDocumentNumberAndDocumentType(String documentNumber, String documentType);
}
