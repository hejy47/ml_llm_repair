## Fixed Function 1
public int compareTo(Fraction object) {
    double nOd = this.doubleValue();
    double dOn = object.doubleValue();
    if(nOd < dOn) {
        return -1;
    } else if(nOd > dOn) {
        return 1;
    } else {
        return 0;
    }
}