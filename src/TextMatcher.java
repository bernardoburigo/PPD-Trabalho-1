public final class TextMatcher {
    private TextMatcher() {
    }

    public static boolean contains(String text, String pattern, SearchAlgorithm algorithm) {
        if (pattern.isEmpty()) {
            return true;
        }
        if (text == null || pattern.length() > text.length()) {
            return false;
        }

        return switch (algorithm) {
            case NAIVE -> text.contains(pattern);
            case KMP -> kmpContains(text, pattern);
        };
    }

    private static boolean kmpContains(String text, String pattern) {
        int[] lps = buildLps(pattern);
        int i = 0;
        int j = 0;

        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == pattern.length()) {
                    return true;
                }
            } else if (j != 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return false;
    }

    private static int[] buildLps(String pattern) {
        int[] lps = new int[pattern.length()];
        int len = 0;
        int i = 1;

        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else if (len != 0) {
                len = lps[len - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }
        return lps;
    }
}
