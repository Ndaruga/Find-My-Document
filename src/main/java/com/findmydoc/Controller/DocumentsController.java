package com.findmydoc.Controller;

import com.findmydoc.Model.DocumentDetails;
import com.findmydoc.Service.DocumentService;
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
@RequestMapping({"/api/documents/"})
public class DocumentsController {
    public static final Logger logger = LoggerFactory.getLogger(DocumentsController.class);

    @Autowired
    private DocumentService documentService;

    public DocumentsController() {}

    @PostMapping({"add-new"})
    public ResponseEntity<Map<String, String>> addNewDocument(@Validated @RequestBody DocumentDetails documentDetails){
        logger.info("addNewDocument");
        logger.info(documentDetails.toString());
        documentService.addNewDocument(documentDetails);
        logger.info("Document saved successfully");
        return new ResponseEntity<>(Map.of("message", "Document added Successfully"), HttpStatus.CREATED);

    }

}
