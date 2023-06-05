## Fixed Function 1
public Week(Date time, TimeZone zone) {
    // argument checking moved to the beginning
    if(time == null || zone == null) {
        throw new IllegalArgumentException("Null argument passed to Week constructor");
    }
    this(time, zone, Locale.getDefault());
}