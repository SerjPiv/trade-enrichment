package com.verygoodbank.tes.web.util;

import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@UtilityClass
public class DateValidator {

    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.length() != 8) {
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
