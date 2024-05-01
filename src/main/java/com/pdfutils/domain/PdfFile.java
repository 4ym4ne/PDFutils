package com.pdfutils.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "pdf_files")
public class PdfFile {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "content", nullable = false)
    private byte[] content;

}