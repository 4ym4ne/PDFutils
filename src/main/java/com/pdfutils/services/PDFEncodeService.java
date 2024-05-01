package com.pdfutils.services;

import com.pdfutils.domain.PdfFile;
import com.pdfutils.repositories.PdfFileRepository;
import com.pdfutils.repositories.UserRepository;
import com.pdfutils.utils.PDFEncode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class PDFEncodeService {

    @Autowired
    private PdfFileRepository pdfFileRepository;
    @Autowired
    private UserRepository userRepository;

    public PdfFile encryptAndSavePdf(MultipartFile file, String userPassword, String ownerPassword) throws Exception {
        // Temporary save the uploaded file to process
        Path tempFile = Files.createTempFile(null, ".pdf");
        file.transferTo(tempFile.toFile());

        // Encrypt the PDF and get the byte array
        byte[] encryptedContent = PDFEncode.addPasswordtoPDF(tempFile.toString(), userPassword, ownerPassword);

        // Clean up the temporary file
        Files.delete(tempFile);

        PdfFile pdfFile = new PdfFile();
        pdfFile.setId(UUID.randomUUID());
        pdfFile.setFilename("Encrypted_" + file.getOriginalFilename());
        pdfFile.setDate(new Date().toInstant());
        pdfFile.setType("application/pdf");
        pdfFile.setContent(encryptedContent);

        pdfFile.setUser(userRepository.findByUsername("admin").orElseThrow());

        return pdfFileRepository.save(pdfFile);
    }
}