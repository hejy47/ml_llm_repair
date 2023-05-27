## Fixed Function 1
public Week(Date time, TimeZone zone) {
    // check if zone parameter is null before setting it
    if(zone == null){
        this(time, RegularTimePeriod.DEFAULT_TIME_ZONE, Locale.getDefault());
    } else {
        this.time = time;
        this.firstDayOfWeek = Calendar.getInstance(zone).getFirstDayOfWeek();
        this.calendar = Calendar.getInstance(zone);
        this.calendar.setTime(time);
        this.calendar.set(Calendar.DAY_OF_WEEK, this.firstDayOfWeek);
        this.start = this.calendar.getTime();
        this.end = this.next().getStart().getTime() - 1;
    }
}