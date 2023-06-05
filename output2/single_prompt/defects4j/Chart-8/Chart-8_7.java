## Fixed Function 1
public Week(Date time, TimeZone zone) {
    // defer argument checking...
    super(time, zone);
    peg(Calendar.MONDAY);
}