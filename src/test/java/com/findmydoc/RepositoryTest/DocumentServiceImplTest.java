package com.findmydoc.RepositoryTest;

import com.findmydoc.Model.DocumentDetails;
import com.findmydoc.Repository.DocumentsRepository;
import com.findmydoc.Service.Impl.DocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DocumentServiceImplTest {
    @Mock
    private DocumentsRepository documentsRepository;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddDocument_Success() {
        DocumentDetails documentDetails = new DocumentDetails();
        documentDetails.setDocType("Passport");
        documentDetails.setDocumentNumber("A123456");
        documentDetails.setSerialNumber("123456789");
        documentDetails.setFounderId(1);
        documentDetails.setOwnerId(0);

        DocumentDetails savedDocument = new DocumentDetails();
        savedDocument.setDocId(1);
        savedDocument.setDocType("Passport");
        savedDocument.setSerialNumber("123456789");
        savedDocument.setDocumentNumber("A123456");
        savedDocument.setUploadDate(new Timestamp(System.currentTimeMillis()));
        savedDocument.setFounderId(1);
        savedDocument.setOwnerId(0);

        when(documentsRepository.save(any(DocumentDetails.class))).thenReturn(savedDocument);

//        Act
        DocumentDetails result = documentService.addNewDocument(documentDetails);

//        Assert
        assertEquals(savedDocument.getDocId(), result.getDocId());
        assertEquals(savedDocument.getDocType(), result.getDocType());
        assertEquals(savedDocument.getDocumentNumber(), result.getDocumentNumber());
        assertEquals(savedDocument.getSerialNumber(), result.getSerialNumber());
        verify(documentsRepository, times(1)).save(any(DocumentDetails.class));
    }

    @Test
    public void testAddNewDocument_NullDocumentDetails() {
        // Arrange
        DocumentDetails documentDetails = null;

        // Act & Assert
        try {
            documentService.addNewDocument(documentDetails);
        } catch (NullPointerException e) {
            assertEquals("documentDetails is marked non-null but is null", e.getMessage());
        }
    }

}
