package dev.funa.headsChat.utils;

public class StringOps {
    /**
     * Split the input string around the first occurrence of the separator.
     * Returns a String array of length 2: {before, after}.
     * - If input is null returns {"", ""}.
     * - If separator is null or empty returns {input, ""}.
     * - If separator is not found returns {input, ""}.
     */
    public static String[] splitAround(String input, String separator) {
        if (input == null) return new String[]{"", ""};
        if (separator == null || separator.isEmpty()) return new String[]{input, ""};

        int idx = input.indexOf(separator);
        if (idx == -1) return new String[]{"", input};

        String before = input.substring(0, idx);
        String after = input.substring(idx + separator.length());
        return new String[]{before, after};
    }
}
