package com.example.pdfsample.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PdfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGeneratePdf_shouldReturnPdfWithCorrectContentType() throws Exception {
        mockMvc.perform(get("/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().exists("Content-Disposition"));
    }

    @Test
    void testGeneratePdf_shouldReturnNonEmptyPdf() throws Exception {
        byte[] result = mockMvc.perform(get("/pdf"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        // PDF files start with "%PDF" signature
        assert result.length > 0;
        String pdfHeader = new String(result, 0, Math.min(4, result.length));
        assert pdfHeader.equals("%PDF");
    }
}
