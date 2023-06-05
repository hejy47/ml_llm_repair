## Fixed Function 1
public static synchronized FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
    Object key = new Pair(style, timeZone != null ? timeZone.getID() : null, locale != null ? locale.toString() : null);
    FastDateFormat format = (FastDateFormat) cDateInstanceCache.get(key);
    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        DateFormat formatter = DateFormat.getDateInstance(style, locale);
        if (timeZone != null) {
            formatter.setTimeZone(timeZone);
        }
        format = new FastDateFormat(formatter);
        cDateInstanceCache.put(key, format);
    }
    return format;
}

## Fixed Function 2
public static synchronized FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
    Object key = new Triple(dateStyle, timeStyle, 
                            timeZone != null ? timeZone.getID() : null,
                            locale != null ? locale.toString() : null);
    FastDateFormat format = (FastDateFormat) cDateTimeInstanceCache.get(key);
    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        DateFormat dateFormat = DateFormat.getDateInstance(dateStyle, locale);
        DateFormat timeFormat = DateFormat.getTimeInstance(timeStyle, locale);
        if (timeZone != null) {
            dateFormat.setTimeZone(timeZone);
            timeFormat.setTimeZone(timeZone);
        }
        format = new FastDateFormat(new CompositeFormat(dateFormat, timeFormat));
        cDateTimeInstanceCache.put(key, format);
    }
    return format;
}