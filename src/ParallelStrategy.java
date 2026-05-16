public enum ParallelStrategy {
    PER_FILE,
    BATCHED;

    public static ParallelStrategy fromString(String value) {
        for (ParallelStrategy strategy : ParallelStrategy.values()) {
            if (strategy.name().equalsIgnoreCase(value)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Estrategia invalida: " + value + ". Use PER_FILE ou BATCHED.");
    }
}
