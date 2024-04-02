package com.pdfutils.utils;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import java.io.File;
import java.io.OutputStream;
import java.util.List;

public class PdfMerger {

    public static void mergePdfs(List<File> sources, OutputStream destination) throws Exception {
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.setDestinationStream(destination);

        for (File source : sources) {
            pdfMerger.addSource(source);
        }

        pdfMerger.mergeDocuments(null);
    }

}