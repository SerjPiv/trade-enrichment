package com.verygoodbank.tes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

import static com.verygoodbank.tes.web.TestUtils.readCsvToMultipartFile;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TradeEnrichmentServiceApplicationTests {


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldEnrichTrades() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", readCsvToMultipartFile("trade.csv").getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/v1/enrich", requestEntity, String.class);

        assertThat(response.getBody())
                .isEqualTo("""
                        date,product_name,currency,price
                        20160101,Treasury Bills Domestic,EUR,10.0
                        20160101,REPO Domestic,EUR,30.34
                        20160101,Missing Product Name,EUR,35.34
                        """);

    }
}
