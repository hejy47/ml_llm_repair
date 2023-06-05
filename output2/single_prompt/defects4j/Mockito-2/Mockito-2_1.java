## Fixed Function 1
public Timer(long durationMillis) {
    if(durationMillis < 0) {
        throw new IllegalArgumentException("Duration cannot be negative");
    }
    this.durationMillis = durationMillis;
}