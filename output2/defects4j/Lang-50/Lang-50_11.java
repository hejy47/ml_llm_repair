## Fixed Function 1
public static synchronized FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
    Object key = new Pair(Integer.valueOf(style), timeZone, locale);
    FastDateFormat format = (FastDateFormat) cDateInstanceCache.get(key);
    if (format == null) {
        format = FastDateFormat.getDateTimeInstance(style, DateFormat.MEDIUM, timeZone, locale);
        format = (FastDateFormat) format.clone();
        cDateInstanceCache.put(key, format);
    }
    return format;
}

## Fixed Function 2
public static synchronized FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
    Object key = new Pair(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
    FastDateFormat format = (FastDateFormat) cDateTimeInstanceCache.get(key);
    if (format == null) {
        format = FastDateFormat.getInstance(getPatternForStyle(dateStyle, timeStyle, locale), timeZone, locale);
        cDateTimeInstanceCache.put(key, format);
    }
    return format;
}

//New method for Fixed Function 2
private static String getPatternForStyle(int dateStyle, int timeStyle, Locale locale) {
    DateFormat formatter = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
    if (formatter instanceof SimpleDateFormat) {
        return ((SimpleDateFormat) formatter).toPattern();
    }
    throw new IllegalArgumentException("No date time pattern for locale: " + locale);
}