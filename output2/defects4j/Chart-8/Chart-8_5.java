## Fixed Function 1
public Week(Date time, TimeZone zone) {
    // argument checking first
    if (zone == null) {
        throw new IllegalArgumentException("Null 'zone' argument.");
    }
    this.time = time;
    this.firstDayOfWeek = Calendar.getInstance(zone).getFirstDayOfWeek();
    this.zone = zone;
    this.locale = Locale.getDefault();
}