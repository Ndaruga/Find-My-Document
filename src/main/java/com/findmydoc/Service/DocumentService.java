package com.findmydoc.Service;

import com.findmydoc.Model.DocumentDetails;
import com.findmydoc.Model.dto.DocumentSearchDTO;
import org.springframework.stereotype.Service;

@Service
public interface DocumentService {
    DocumentDetails addNewDocument(DocumentDetails documentDetails) throws Exception;

    boolean documentExists(DocumentSearchDTO documentSearch) throws Exception;
}
