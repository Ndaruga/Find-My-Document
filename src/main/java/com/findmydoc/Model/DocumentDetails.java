package com.findmydoc.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="document_details")

public class DocumentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="document_id", nullable=false, unique=true)
    private int docId;

    @Size(min=3, max=100)
    @Column(name="document_type", nullable = false)
    private String docType;

    @Size(min=0, max=100)
    @Column(name="serial_number")
    private String serialNumber;

    @Size(min=3, max = 100, message = "Document Number must be between 3 - 100 characters")
    @Column(name="document_number", nullable=false)
    private String documentNumber;

    @Column(name="upload_date", nullable = false)
    private Timestamp uploadDate;

    @Column(name="founder_id", nullable=false, updatable = false)
    private int founderId;

    @Column(name = "owner_id", updatable = false)
    private int ownerId;

    @Column(name="claimDate", updatable = false)
    private Timestamp claimDate;

}
