## Fixed Function 1
public Week(Date time, TimeZone zone) {
    // add argument checking
    if (time == null) {
        throw new IllegalArgumentException("Null 'time' argument.");
    }

    this.zone = zone;
    this.firstMillisecond = getFirstMillisecond();
    this.lastMillisecond = getLastMillisecond();
}