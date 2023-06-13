## Fixed Function 1
public Paint getPaint(double value) {
    double v = Math.max(value, this.lowerBound);
    v = Math.min(v, this.upperBound);
    int g = (int) Math.round((v - this.lowerBound) / (this.upperBound - this.lowerBound) * 255.0);
    return new Color(g, g, g);
}