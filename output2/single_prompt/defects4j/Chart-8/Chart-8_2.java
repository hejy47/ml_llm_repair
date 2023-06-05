## Fixed Function 1
public Week(Date time, TimeZone zone) {
    // perform argument checking
    if (time == null || zone == null) {
        throw new IllegalArgumentException("Null argument not allowed.");
    }
    this.time = time;
    this.startOfWeek = calculateStartOfWeek(zone, Locale.getDefault());
    this.zone = zone;
}