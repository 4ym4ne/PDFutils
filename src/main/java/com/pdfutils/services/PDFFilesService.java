package com.pdfutils.services;

import com.pdfutils.domain.PdfFile;
import com.pdfutils.repositories.PdfFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PDFFilesService {

    @Autowired
    private PdfFileRepository pdfFileRepository;

    public Optional<PdfFile> getPdfFileById(String uuid) {
        return pdfFileRepository.findById(UUID.fromString(uuid));
    }

}
