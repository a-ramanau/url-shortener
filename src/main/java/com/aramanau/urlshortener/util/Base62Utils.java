package com.aramanau.urlshortener.util;

public class Base62Utils {

    private static final String DIGITS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int BASE = DIGITS.length();

    public static String encode(long base10) {

        if (base10 < 0L) {
            throw new IllegalArgumentException("Number must be positive: " + base10);
        }

        if (base10 == 0L) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        while (base10 != 0L) {
            sb.append(DIGITS.charAt((int) (base10 % BASE)));
            base10 /= BASE;
        }

        return sb.reverse().toString();
    }

    public static long decode(String base62) {
        long base10 = 0L;
        for (int i = 0; i < base62.length(); i++) {
            base10 = base10 * BASE + DIGITS.indexOf(base62.charAt(i));
        }
        return base10;
    }
}
