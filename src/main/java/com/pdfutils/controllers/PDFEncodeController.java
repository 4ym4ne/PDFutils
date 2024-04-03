package com.pdfutils.controllers;

import com.pdfutils.entities.PdfFile;
import com.pdfutils.services.PDFEncodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PDFEncodeController {

    @Autowired
    private PDFEncodeService pdfEncodeService;

    @PostMapping(value = "/encryptPdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> encryptPdf(@RequestParam("file") MultipartFile file,
                                        @RequestParam("userPassword") String userPassword,
                                        @RequestParam("ownerPassword") String ownerPassword) {
        try {
            PdfFile pdfFile = pdfEncodeService.encryptAndSavePdf(file, userPassword, ownerPassword);

            // success message with the file's ID
            return ResponseEntity.ok("Encrypted PDF saved with ID: " + pdfFile.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to encrypt PDF: " + e.getMessage());
        }
    }
}
