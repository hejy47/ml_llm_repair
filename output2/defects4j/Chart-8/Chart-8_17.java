## Fixed Function 1
public Week(Date time, TimeZone zone) {
    // Check for null arguments
    if (time == null || zone == null) {
        throw new IllegalArgumentException("Null argument.");
    }
    this.time = time;
    this.firstDayOfWeek = Calendar.getInstance(zone).getFirstDayOfWeek();
    this.locale = Locale.getDefault();
}