package com.pdfutils.controllers;


import com.pdfutils.entities.PdfFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
public class PdfMergerController {

    @Autowired
    private com.pdfutils.service.PdfMergerService pdfMergerService;

    @PostMapping(value = "/merge", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> mergePdfs(@RequestParam("files") List<MultipartFile> files) {
        if (files.isEmpty()) {
            return ResponseEntity.badRequest().body("No PDF files provided.");
        }

        // In the PdfMergerController
        try {
            String fileID = pdfMergerService.mergePdfFiles(files);
            // Return success response with relevant info or file access URL
            return ResponseEntity.ok().body("Merged File ID: " + fileID);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to merge PDFs: " + e.getMessage());
        }

    }

    @GetMapping("/pdfs/{id}")
    public ResponseEntity<?> getPdfById(@PathVariable String id) {
        try {
            Optional<PdfFile> pdfFileOpt = pdfMergerService.getPdfFileById(id);
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
