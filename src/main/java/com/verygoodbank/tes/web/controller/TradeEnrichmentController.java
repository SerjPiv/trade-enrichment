package com.verygoodbank.tes.web.controller;

import com.verygoodbank.tes.web.service.StreamingTradeEnrichmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TradeEnrichmentController {
    private final StreamingTradeEnrichmentService streamingTradeEnrichmentService;

    @PostMapping("/enrich")
    public ResponseEntity enrichProduct(@RequestParam("file") MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                String csvString = streamingTradeEnrichmentService.enrichTrades(file);
                return buildResponse(csvString);
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body("An error occurred while processing the file.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The file is empty or null. Please provide a valid file.");
        }
    }

    public ResponseEntity buildResponse(String csv) {
        byte[] content = csv.getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "enriched_trades.csv");

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}


