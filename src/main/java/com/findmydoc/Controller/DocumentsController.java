package com.findmydoc.Controller;

import com.findmydoc.Model.DocumentDetails;
import com.findmydoc.Model.dto.DocumentSearchDTO;
import com.findmydoc.Service.DocumentService;
import com.findmydoc.Service.OwnerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/api/v1/document/"})
public class DocumentsController {
    public static final Logger logger = LoggerFactory.getLogger(DocumentsController.class);

    @Autowired
    private DocumentService documentService;

    @Autowired
    private OwnerService ownerService;

    public DocumentsController() {}

    @PostMapping({"new"})
    public ResponseEntity<Map<String, String>> addNewDocument(@Valid @RequestBody DocumentDetails documentDetails) throws Exception {
        documentService.addNewDocument(documentDetails);
        logger.info("Document saved successfully");
        return new ResponseEntity<>(Map.of("message", "Document added Successfully"), HttpStatus.CREATED);
    }

    @GetMapping({"/search"})
    public ResponseEntity<Map<String, String>> searchDocument(@Valid @RequestBody DocumentSearchDTO documentSearchDTO) throws Exception {

        if (!documentService.documentExists(documentSearchDTO)){
            return new ResponseEntity<>(Map.of("message", "Document not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Map.of("message", "Document found"), HttpStatus.OK);
    }
}
