## Fixed Function 1
public StringBuffer format(Calendar calendar, StringBuffer buf) {
    Calendar cal;
    if (mTimeZoneForced) {
        cal = (Calendar) calendar.clone();
        cal.setTimeZone(mTimeZone);
    } else {
        cal = calendar;
    }
    return applyRules(cal, buf);
}