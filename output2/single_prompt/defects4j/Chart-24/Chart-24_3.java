## Fixed Function 1
public Paint getPaint(double value) {
    double v = Math.max(value, this.lowerBound);
    v = Math.min(v, this.upperBound);
    int g = (int) ((v - this.lowerBound) / (this.upperBound - this.lowerBound) * 255.0); //fixed variable 'value' to 'v'
    return new Color(g, g, g);
}