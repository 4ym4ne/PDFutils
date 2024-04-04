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

import java.util.HashMap;
import java.util.Map;

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

            // Creating a Map to return the file ID in JSON format
            Map<String, String> response = new HashMap<>();
            response.put("id", pdfFile.getId().toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            // For consistency, consider also returning an error message in JSON format
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to encrypt PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
