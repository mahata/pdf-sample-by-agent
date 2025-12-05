package com.example.pdfsample.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import jakarta.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class PdfGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(PdfGenerationService.class);
    private String cachedFontPath;

    @PostConstruct
    public void init() {
        // Load and cache the font file on service initialization
        try {
            ClassPathResource fontResource = new ClassPathResource("fonts/NotoSansJP.ttf");
            try (InputStream fontStream = fontResource.getInputStream()) {
                byte[] fontBytes = fontStream.readAllBytes();
                java.io.File tempFont = java.io.File.createTempFile("noto-sans-jp", ".ttf");
                tempFont.deleteOnExit();
                Files.write(tempFont.toPath(), fontBytes);
                cachedFontPath = tempFont.getAbsolutePath();
                logger.info("Japanese font cached at: {}", cachedFontPath);
            }
        } catch (IOException e) {
            logger.error("Failed to cache Japanese font", e);
            throw new IllegalStateException("Failed to initialize PDF generation service", e);
        }
    }

    public byte[] generatePdfFromHtml() throws IOException, DocumentException {
        // Read HTML template from resources
        ClassPathResource resource = new ClassPathResource("template.html");
        String htmlContent;
        try (InputStream inputStream = resource.getInputStream()) {
            htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        // Generate PDF using try-with-resources
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();

            // Add Japanese font support using cached font path
            ITextFontResolver fontResolver = renderer.getFontResolver();
            fontResolver.addFont(cachedFontPath, "sans-serif",
                                BaseFont.IDENTITY_H, BaseFont.EMBEDDED, null);

            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        }
    }
}

