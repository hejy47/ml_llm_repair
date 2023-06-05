## Fixed Function 1
public static synchronized FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
    Object key = new Integer(style);
    if (timeZone != null) {
        key = new Pair(key, timeZone);
    }
    if (locale != null) {
        key = new Pair(key, locale);
    }
    FastDateFormat format = (FastDateFormat) cDateInstanceCache.get(key);
    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try {
            DateFormat formatter = DateFormat.getDateInstance(style, locale);
            String pattern = ((SimpleDateFormat) formatter).toPattern(); // Cast to SimpleDateFormat to access toPattern() method
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
    Object key = new Pair(new Integer(dateStyle), new Integer(timeStyle));
    if (timeZone != null) {
        key = new Pair(key, timeZone);
    }
    if (locale != null) {
        key = new Pair(key, locale);
    }
    FastDateFormat format = (FastDateFormat) cDateTimeInstanceCache.get(key);
    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try {
            DateFormat formatter = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
            String pattern = ((SimpleDateFormat) formatter).toPattern(); // Cast to SimpleDateFormat to access toPattern() method
            format = getInstance(pattern, timeZone, locale);
            cDateTimeInstanceCache.put(key, format);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("No date time pattern for locale: " + locale);
        }
    }
    return format;
}