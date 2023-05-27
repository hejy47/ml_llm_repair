## Fixed Function 1
public Week(Date time, TimeZone zone) {
    // argument checking...
    if (time == null || zone == null) {
        throw new IllegalArgumentException("Null argument.");
    }
    this.firstMillisecond = calculateFirstMillisecond(time, zone);
    this.lastMillisecond = calculateLastMillisecond(time, zone); 
}