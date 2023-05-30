## Fixed Function 1
public static synchronized FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
    Object key = new Triple(Integer.valueOf(style), timeZone, locale);
    FastDateFormat format = (FastDateFormat) cDateInstanceCache.get(key);
    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try {
            format = FastDateFormat.getInstance(DateFormat.getDateInstance(style, locale).toPattern(), timeZone, locale);
            cDateInstanceCache.put(key, format);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("No date pattern for locale: " + locale);
        }
    }
    return format;
}

## Fixed Function 2
public static synchronized FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
    Object key = new FourTuple(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
    FastDateFormat format = (FastDateFormat) cDateTimeInstanceCache.get(key);
    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try {
            String pattern;
            if (timeStyle == -1) {
                pattern = DateFormat.getDateInstance(dateStyle, locale).toPattern();
            } else {
                pattern = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale).toPattern();
            }
            format = FastDateFormat.getInstance(pattern, timeZone, locale);
            cDateTimeInstanceCache.put(key, format);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("No date time pattern for locale: " + locale);
        }
    }
    return format;
}