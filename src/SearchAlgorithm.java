public enum SearchAlgorithm {
    NAIVE,
    KMP;

    public static SearchAlgorithm fromString(String value) {
        for (SearchAlgorithm algorithm : SearchAlgorithm.values()) {
            if (algorithm.name().equalsIgnoreCase(value)) {
                return algorithm;
            }
        }
        throw new IllegalArgumentException("Algoritmo invalido: " + value + ". Use NAIVE ou KMP.");
    }
}
