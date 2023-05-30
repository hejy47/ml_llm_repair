## Fixed Function 1
public Line revert() {
    final Line reverted = new Line(getPoint(), getDirection().negate());
    return reverted;
}