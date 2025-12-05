package com.example.pdfsample.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class PdfGenerationService {

    public byte[] generatePdfFromHtml() throws IOException, DocumentException {
        // Read HTML template from resources
        ClassPathResource resource = new ClassPathResource("template.html");
        String htmlContent;
        try (InputStream inputStream = resource.getInputStream()) {
            htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        // Generate PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();

        // Add Japanese font support
        ITextFontResolver fontResolver = renderer.getFontResolver();
        
        // Use embedded Noto Sans JP font for Japanese support
        ClassPathResource fontResource = new ClassPathResource("fonts/NotoSansJP.ttf");
        try (InputStream fontStream = fontResource.getInputStream()) {
            // Create a temporary file for the font (Flying Saucer needs file path)
            byte[] fontBytes = fontStream.readAllBytes();
            java.io.File tempFont = java.io.File.createTempFile("font", ".ttf");
            tempFont.deleteOnExit();
            java.nio.file.Files.write(tempFont.toPath(), fontBytes);
            
            // Register font with explicit alias for sans-serif
            fontResolver.addFont(tempFont.getAbsolutePath(), "sans-serif",
                                BaseFont.IDENTITY_H, BaseFont.EMBEDDED, null);
        }

        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);

        return outputStream.toByteArray();
    }
}
