package com.findmydoc.Service.Impl;

import com.findmydoc.Model.DocumentDetails;
import com.findmydoc.Model.dto.DocumentSearchDTO;
import com.findmydoc.Repository.DocumentsRepository;
import com.findmydoc.Service.DocumentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Autowired
    private DocumentsRepository documentsRepository;

    public DocumentServiceImpl(){}

    @Override
    public DocumentDetails addNewDocument(@Valid DocumentDetails documentDetails) throws Exception {

//        Trim white spaces
        String documentType = documentDetails.getDocumentType().strip();
        String documentNumber = documentDetails.getDocumentNumber().toUpperCase().strip();
        String docSerialNo = documentDetails.getSerialNumber().toUpperCase().strip();
        String ownerFirstName = documentDetails.getOwnerFirstName().strip();
        String moreNotes = documentDetails.getMoreNotes().strip();
        int custodianId = documentDetails.getCustodianId();

        logger.info(documentType + " " + documentNumber + " " + docSerialNo + " " + custodianId);

//        Validate that the custodian ID exists in the request and matches one in the database
        if (custodianId != documentDetails.getCustodianId()){
            throw new InvalidParameterException("Custodian ID is not valid");
        }

//        Validate that either the document number or serial number must exist
        if (documentNumber.length() < 5 && docSerialNo.length() < 5){
            throw new InvalidParameterException("Invalid document identifier");
        }


//        Validate the document type
        if (!(documentType.equals("ID Card") || documentType.equals("Passport") || documentType.equals("Other"))){
            throw new InvalidParameterException("Invalid document type");

        } else if (!documentType.matches("[A-Za-z\\s]{5,50}")) {
            throw new InvalidParameterException("Invalid document type. Only 5 - 50 characters and a space are allowed");

        } else if (documentNumber.length() < 5 || docSerialNo.length() < 5) {
            if (documentNumber.length() < 5 && docSerialNo.length() >= 5) {
            } else if (docSerialNo.length() < 5 && documentNumber.length() >= 5) {
            } else if (documentNumber.length() >= 5 && docSerialNo.length() >= 5) {
            } else {
                throw new InvalidParameterException("Either the Document Number or the Serial Number must be at least 5 characters long.");
            }
        } else if (!ownerFirstName.matches("[A-Za-z]{2,20}")) {
            throw new InvalidParameterException("Invalid owner's first name. Only 2 - 20 Alphabetic characters are allowed");

        } else if (!String.valueOf(custodianId).matches("\\d{1,10}")) {
            throw new InvalidParameterException("Invalid founder id");

        } else {
            if (documentType.equals("ID Card")) {
                if (!documentNumber.matches("\\d{7,8}")) {
                    throw new Exception("Invalid ID Card Number. Only 7 to 8 numeric digits are allowed.");
                }
                if (!docSerialNo.matches("\\d{0,10}")) {
                    throw new InvalidParameterException("Invalid serial number");
                }

            } else if (documentType.equals("Passport")) {
                if (!documentNumber.matches("[A-Z0-9]{7,9}")) {
                    throw new Exception("Invalid Passport number. Only 7 to 9 alphanumeric characters are allowed.");
                }
                if (!docSerialNo.matches("\\d{0,10}")) {
                    throw new InvalidParameterException("Invalid serial number");
                }
            } else {
                if (documentNumber != null && !documentNumber.equals("")){
                    if (!documentNumber.matches("[A-Z0-9]{3,20}")) {
                        throw new InvalidParameterException("Invalid Document Number. Only 3 - 20 alphanumeric characters are allowed.");
                    }
                } else if (docSerialNo != null && !docSerialNo.equals("")) {
                    if (!docSerialNo.matches("[A-Z0-9]{3,20}")) {
                        throw new InvalidParameterException("Invalid serial number. Only 3 - 20 alphanumeric characters are allowed.");
                    }
                }
            }
        }

//      Validate more notes
        if (!moreNotes.matches("[A-Za-z0-9.,\\s]{0,300}")){
            throw new InvalidParameterException("Additional notes contain invalid characters");
        }

        documentDetails.setOwnerId(0);
        documentDetails.setUploadDate(new Timestamp(System.currentTimeMillis()));
        return documentsRepository.save(documentDetails);
    }

    @Override
    public Optional<DocumentDetails> documentExists(DocumentSearchDTO documentSearch) throws Exception {
        String documentType = documentSearch.getDocType().strip();
        String documentNumber = documentSearch.getDocNo() != null ? documentSearch.getDocNo().toUpperCase().strip() : null;

        if (!documentType.matches("[A-Za-z\\s]{5,50}")) {
            throw new InvalidParameterException("Invalid document type provided");
        }

        // Check by document number if provided
        if (documentNumber != null && !documentNumber.isEmpty()) {
            // First check if it's a document number If not found, check if it's a serial number
            Optional<DocumentDetails> documentDetails = documentsRepository.findByDocumentNumberAndDocumentType(documentNumber, documentType);

            if (documentDetails.isEmpty()) {
                return documentsRepository.findBySerialNumberAndDocumentType(documentNumber, documentType);
            }

            return documentDetails;
        } else {
            throw new InvalidParameterException("Document number must be provided");
        }
    }


}
