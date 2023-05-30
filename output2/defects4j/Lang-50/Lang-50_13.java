## Fixed Function 1
public static synchronized FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
    Object key = new Object[]{style, timeZone, locale};
    FastDateFormat format = (FastDateFormat) cDateInstanceCache.get(key);
    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try {
            DateFormat formatter = DateFormat.getDateInstance(style, locale);
            if (formatter instanceof SimpleDateFormat) {
                String pattern = ((SimpleDateFormat) formatter).toPattern();
                format = getInstance(pattern, timeZone, locale);
                cDateInstanceCache.put(key, format);
            } else {
                throw new IllegalArgumentException("No date pattern for locale: " + locale);
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("No date pattern for locale: " + locale);
        }
    }
    return format;
}

## Fixed Function 2
public static synchronized FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
    Object key = new Object[]{dateStyle, timeStyle, timeZone, locale};
    FastDateFormat format = (FastDateFormat) cDateTimeInstanceCache.get(key);
    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try {
            DateFormat formatter = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
            if (formatter instanceof SimpleDateFormat) {
                String pattern = ((SimpleDateFormat) formatter).toPattern();
                format = getInstance(pattern, timeZone, locale);
                cDateTimeInstanceCache.put(key, format);
            } else {
                throw new IllegalArgumentException("No date time pattern for locale: " + locale);
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("No date time pattern for locale: " + locale);
        }
    }
    return format;
}