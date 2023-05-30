## Fixed Function 1
public static String formatPeriod(long startMillis, long endMillis, String format, boolean padWithZeros, TimeZone timezone) {
    long millis = endMillis - startMillis;
    if (millis < 28 * DateUtils.MILLIS_PER_DAY) {
        return formatDuration(millis, format, padWithZeros);
    }
    Token[] tokens = lexx(format);
    // timezones get funky around 0, so normalizing everything to GMT
    // stops the hours being off
    Calendar start = Calendar.getInstance(timezone);
    start.setTimeInMillis(startMillis);
    Calendar end = Calendar.getInstance(timezone);
    end.setTimeInMillis(endMillis);
    // initial estimates
    int milliseconds = end.get(Calendar.MILLISECOND) - start.get(Calendar.MILLISECOND);
    int seconds = end.get(Calendar.SECOND) - start.get(Calendar.SECOND);
    int minutes = end.get(Calendar.MINUTE) - start.get(Calendar.MINUTE);
    int hours = end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY);
    int days = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);
    int months = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
    int years = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
    // each initial estimate is adjusted in case it is under 0
    while (milliseconds < 0) {
        milliseconds += 1000;
        seconds -= 1;
    }
    while (seconds < 0) {
        seconds += 60;
        minutes -= 1;
    }
    while (minutes < 0) {
        minutes += 60;
        hours -= 1;
    }
    while (hours < 0) {
        hours += 24;
        days -= 1;
    }
    while (days < 0) {
        end.add(Calendar.MONTH, -1);
        days += end.getActualMaximum(Calendar.DAY_OF_MONTH);
        months -= 1;
    }
    while (months < 0) {
        months += 12;
        years -= 1;
    }
    milliseconds -= reduceAndCorrect(start, end, Calendar.MILLISECOND, milliseconds);
    seconds -= reduceAndCorrect(start, end, Calendar.SECOND, seconds);
    minutes -= reduceAndCorrect(start, end, Calendar.MINUTE, minutes);
    hours -= reduceAndCorrect(start, end, Calendar.HOUR_OF_DAY, hours);
    days -= reduceAndCorrect(start, end, Calendar.DAY_OF_MONTH, days);
    months -= reduceAndCorrect(start, end, Calendar.MONTH, months);
    years -= reduceAndCorrect(start, end, Calendar.YEAR, years);
    // This next block of code adds in values that
    // aren't requested. This allows the user to ask for the
    // number of months and get the real count and not just 0->11.
    if (!Token.containsTokenWithValue(tokens, y)) {
        if (Token.containsTokenWithValue(tokens, M)) {
            months += 12 * years;
            years = 0;
        } else {
            // TODO: this is a bit weak, needs work to know about leap years
            days += 365 * years;
            years = 0;
        }
    }
    if (!Token.containsTokenWithValue(tokens, M)) {
        days += end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
        months = 0;
    }
    if (!Token.containsTokenWithValue(tokens, d)) {
        hours += 24 * days;
        days = 0;
    }
    if (!Token.containsTokenWithValue(tokens, H)) {
        minutes += 60 * hours;
        hours = 0;
    }
    if (!Token.containsTokenWithValue(tokens, m)) {
        seconds += 60 * minutes;
        minutes = 0;
    }
    if (!Token.containsTokenWithValue(tokens, s)) {
        milliseconds += 1000 * seconds;
        seconds = 0;
    }
    return format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
}

## Fixed Function 2
static int reduceAndCorrect(Calendar start, Calendar end, int field, int difference) {
    end.add(field, -difference);
    int endValue = end.get(field);
    int startValue = start.get(field);
    if (endValue < startValue) {
        end.add(field, difference);
        return startValue - endValue;
    } else {
        return 0;
    }
}