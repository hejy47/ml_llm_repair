## Fixed Function 1
public int compareTo(Fraction object) {
    double nOd = this.doubleValue();
    double dOn = object.doubleValue();
    return Double.compare(nOd, dOn);
}