package com.findmydoc.Service.Impl;

import com.findmydoc.Model.DocumentDetails;
import com.findmydoc.Repository.DocumentsRepository;
import com.findmydoc.Service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentsRepository documentsRepository;

    public DocumentServiceImpl(){}

    @Override
    public DocumentDetails addNewDocument(DocumentDetails documentDetails) {
        documentDetails.setUploadDate(new Timestamp(System.currentTimeMillis()));
        return documentsRepository.save(documentDetails);
    }
}
