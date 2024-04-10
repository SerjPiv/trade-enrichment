package com.verygoodbank.tes.web;

import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class TestUtils {
    public static MockMultipartFile readCsvToMultipartFile(String fileName) throws IOException {
        InputStream inputStream = TestUtils.class.getClassLoader().getResourceAsStream(fileName);
        byte[] csvBytes = StreamUtils.copyToByteArray(inputStream);
        return new MockMultipartFile("file", fileName, MediaType.TEXT_PLAIN_VALUE, csvBytes);
    }
}
