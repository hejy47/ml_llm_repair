## Fixed Function 1
public Week(Date time, TimeZone zone) {
    // check for null argument and use the provided zone
    if (time == null || zone == null) {
        throw new IllegalArgumentException("Null argument.");
    }
    this.time = time;
    this.firstDayOfWeek = Calendar.getInstance(zone).getFirstDayOfWeek();
    this.zone = zone;
    this.locale = Locale.getDefault();
}