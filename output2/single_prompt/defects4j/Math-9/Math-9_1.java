## Fixed Function 1
public Line revert() {
    final Line reverted = new Line(zero, direction.negate());
    return reverted;
}