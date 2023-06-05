## Fixed Function 1
public static synchronized FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
    Object key = new Pair(style, timeZone, locale);
    FastDateFormat format = (FastDateFormat) cDateInstanceCache.get(key);
    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try {
            SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance(style, locale);
            String pattern = formatter.toPattern();
            format = getInstance(pattern, timeZone, locale);
            cDateInstanceCache.put(key, format);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("No date pattern for locale: " + locale);
        }
    }
    return format;
}

## Fixed Function 2
public static synchronized FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
    Object key = new Pair(dateStyle, timeStyle, timeZone, locale);
    FastDateFormat format = (FastDateFormat) cDateTimeInstanceCache.get(key);
    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try {
            SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
            String pattern = formatter.toPattern();
            format = getInstance(pattern, timeZone, locale);
            cDateTimeInstanceCache.put(key, format);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("No date time pattern for locale: " + locale);
        }
    }
    return format;
}