## Fixed Function 1
public Week(Date time, TimeZone zone) {
    // add argument checking
    if (time == null) {
        throw new IllegalArgumentException("Null 'time' argument.");
    }
    if (zone == null) {
        throw new IllegalArgumentException("Null 'zone' argument.");
    }
    this.time = time;
    this.firstDayOfWeek = Week.DEFAULT_FIRST_DAY_OF_WEEK;
    this.startTime = calculateFirstDayOfTheWeek(zone);
    this.endTime = calculateLastDayOfTheWeek(zone);
}