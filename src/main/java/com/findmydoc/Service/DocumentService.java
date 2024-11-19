package com.findmydoc.Service;

import com.findmydoc.Model.DocumentDetails;
import org.springframework.stereotype.Service;

@Service
public interface DocumentService {
    DocumentDetails addNewDocument(DocumentDetails documentDetails) throws Exception;

}
