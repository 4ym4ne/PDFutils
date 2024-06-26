package com.pdfutils.repositories;

import com.pdfutils.domain.PdfFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PdfFileRepository extends JpaRepository<PdfFile, UUID> {
}