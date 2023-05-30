## Fixed Function 1
public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setTimeZone(mTimeZone);
    c.setTime(date);
    return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
}