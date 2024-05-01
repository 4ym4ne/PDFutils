package com.pdfutils.controllers;

import com.pdfutils.domain.PdfFile;
import com.pdfutils.services.PDFFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/pdfs")
public class PDFFilesController {

    @Autowired
    private PDFFilesService pdfFilesService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPdfById(@PathVariable String id) {
        try {
            Optional<PdfFile> pdfFileOpt = pdfFilesService.getPdfFileById(id);
            if (pdfFileOpt.isPresent()) {
                PdfFile pdfFile = pdfFileOpt.get();
                ByteArrayResource resource = new ByteArrayResource(pdfFile.getContent());
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfFile.getFilename() + "\"")
                        .contentType(MediaType.APPLICATION_PDF)
                        .contentLength(resource.contentLength())
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UUID format.");
        }
    }
}
