## Fixed Function 1
public double percentageValue() {
    return multiply(100).divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP).doubleValue();
}