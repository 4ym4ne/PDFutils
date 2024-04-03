package com.pdfutils.utils;


import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class PDFEncode {

    /**
     * Adds a password to the PDF and returns the encrypted content as a byte array.
     *
     * @param inputPath Path to the input PDF file.
     * @param userPassword The password required to open the PDF.
     * @param ownerPassword The owner password that gives full permissions.
     * @return A byte array containing the encrypted PDF content.
     * @throws Exception if an error occurs during processing.
     */
    public static byte[] addPasswordtoPDF(String inputPath, String userPassword, String ownerPassword) throws Exception {
        try (PDDocument document = Loader.loadPDF(new File(inputPath))) {
            AccessPermission accessPermission = new AccessPermission();

            // Set up a new StandardProtectionPolicy
            StandardProtectionPolicy spp = new StandardProtectionPolicy(ownerPassword, userPassword, accessPermission);
            spp.setEncryptionKeyLength(128); //128-bit encryption.
            document.protect(spp);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                document.save(outputStream);
                return outputStream.toByteArray();
            }
        }
    }
}

