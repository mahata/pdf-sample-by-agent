# pdf-sample-by-agent

Spring Boot PDF Generation Sample - A proof-of-concept project demonstrating on-the-fly PDF generation from HTML templates with Japanese font support.

## Features

- **GET /pdf endpoint**: Generates PDF files on demand
- **HTML to PDF conversion**: Uses Flying Saucer library to convert HTML templates to PDF
- **Japanese font support**: Includes embedded Noto Sans JP font for proper Japanese text rendering
- **Performance optimized**: Font caching on application startup for fast PDF generation
- **Comprehensive tests**: Automated tests for endpoint validation

## Requirements

- Java 17 or higher
- Maven 3.6+

## Building the Project

```bash
mvn clean compile
```

## Running Tests

```bash
mvn test
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080.

## Using the API

### Generate PDF

**Endpoint**: `GET /pdf`

**Example**:
```bash
curl http://localhost:8080/pdf --output document.pdf
```

This will generate a PDF from the HTML template located at `src/main/resources/template.html`.

## Project Structure

```
src/
├── main/
│   ├── java/com/example/pdfsample/
│   │   ├── PdfSampleApplication.java      # Main Spring Boot application
│   │   ├── controller/
│   │   │   └── PdfController.java         # REST controller for /pdf endpoint
│   │   └── service/
│   │       └── PdfGenerationService.java  # PDF generation logic
│   └── resources/
│       ├── application.properties          # Application configuration
│       ├── template.html                   # HTML template for PDF
│       └── fonts/
│           └── NotoSansJP.ttf             # Japanese font file
└── test/
    └── java/com/example/pdfsample/
        └── controller/
            └── PdfControllerTest.java     # Tests for PDF endpoint
```

## Technology Stack

- **Spring Boot 3.2.0**: Application framework
- **Flying Saucer 9.7.2**: HTML to PDF conversion
- **iText 2.1.7**: PDF library (required by Flying Saucer)
- **Noto Sans JP**: Google's Japanese font

## Notes

- The Japanese font is cached on application startup for performance
- PDF files are generated in-memory and returned directly in the HTTP response
- The HTML template uses standard HTML and CSS for layout and styling
