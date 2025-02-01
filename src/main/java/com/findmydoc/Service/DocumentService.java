package com.findmydoc.Service;

import com.findmydoc.Model.DocumentDetails;
import com.findmydoc.Model.dto.DocumentSearchDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DocumentService {
    DocumentDetails addNewDocument(DocumentDetails documentDetails) throws Exception;

    Optional<DocumentDetails> documentExists(DocumentSearchDTO documentSearch) throws Exception;
}
