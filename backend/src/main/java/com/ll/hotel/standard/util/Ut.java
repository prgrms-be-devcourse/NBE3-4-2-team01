package com.ll.hotel.standard.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.hotel.global.app.AppConfig;
import lombok.SneakyThrows;

public class Ut {
    public static class str {
        public static boolean isBlank(String str) {
            return str == null || str.trim().isEmpty();
        }
    }

    public static class json {
        private static final ObjectMapper om = AppConfig.getObjectMapper();

        @SneakyThrows
        public static String toString(Object obj) {
            return om.writeValueAsString(obj);
        }
    }
}
