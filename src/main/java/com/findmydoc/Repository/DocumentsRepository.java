package com.findmydoc.Repository;

import com.findmydoc.Model.DocumentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentsRepository extends JpaRepository<DocumentDetails, Integer> {
    boolean existsBySerialNumber(String serialNumber);
    boolean existsByDocumentNumber(String documentNumber);
}
