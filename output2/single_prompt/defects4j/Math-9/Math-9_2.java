## Fixed Function 1
public Line revert() {
    final Line reverted = new Line(this.zero.subtract(this.direction), this.zero);
    return reverted;
}