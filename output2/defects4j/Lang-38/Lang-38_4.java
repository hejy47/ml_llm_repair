## Fixed Function 1
public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(mTimeZone);
    }
    SimpleDateFormat format = new SimpleDateFormat(mPattern, Locale.getDefault());
    format.setCalendar(calendar);
    return format.format(calendar.getTime(), buf, new FieldPosition(0));
}