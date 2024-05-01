package com.pdfutils.services;

import com.pdfutils.domain.PdfFile;
import com.pdfutils.repositories.PdfFileRepository;
import com.pdfutils.utils.PdfMerger;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

@Service
public class PdfMergerService {

    @Autowired
    private PdfFileRepository pdfFileRepository;

    public String mergePdfFiles(List<MultipartFile> multipartFiles) throws Exception {
        List<File> tempFiles = new ArrayList<>();
        ByteArrayOutputStream mergedContent = new ByteArrayOutputStream();

        try {
            for (MultipartFile multipartFile : multipartFiles) {
                if (!"pdf".equalsIgnoreCase(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))) {
                    throw new IllegalArgumentException("Invalid file type. Only PDF files are supported.");
                }
                File tempFile = Files.createTempFile(null, ".pdf").toFile();
                multipartFile.transferTo(tempFile);
                tempFiles.add(tempFile);
            }

            PdfMerger.mergePdfs(tempFiles, mergedContent);

            UUID uuid = UUID.randomUUID();

            PdfFile pdfFile = new PdfFile();
            pdfFile.setId(uuid);
            pdfFile.setFilename(uuid + ".pdf"); // Set filename. You can adjust as needed
            pdfFile.setDate(new Date().toInstant());
            pdfFile.setType("application/pdf");
            pdfFile.setContent(mergedContent.toByteArray());

            pdfFileRepository.save(pdfFile);

            return uuid.toString();
        } finally {
            // Clean up temporary files
            for (File file : tempFiles) {
                Files.deleteIfExists(file.toPath());
            }
        }
    }


}