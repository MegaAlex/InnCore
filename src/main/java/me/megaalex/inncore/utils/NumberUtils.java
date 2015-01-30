package me.megaalex.inncore.utils;

import java.math.BigDecimal;

public class NumberUtils {

    public static String parseDecimal(BigDecimal number) {
        return number.setScale(2).toPlainString();
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis() / 1000L;
    }
}
