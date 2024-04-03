package com.pdfutils.controllers;



import com.pdfutils.services.PdfMergerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class PDFMergerController {

    @Autowired
    private PdfMergerService pdfMergerService;

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



}
