package com.verygoodbank.tes.web.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DateValidatorTest {

    @Test
    public void shouldReturnTrueForValidDate() {
        assertThat(DateValidator.isValidDate("20210101"))
                .isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2021010", "202101011", "2021010a"})
    public void shouldReturnFalseForInvalidDate(String invalidDate) {
        assertThat(DateValidator.isValidDate(invalidDate))
                .isFalse();
    }

    @Test
    public void shouldReturnFalseForNullDate() {
        assertThat(DateValidator.isValidDate(null))
                .isFalse();
    }
}
