## Fixed Function 1
public static synchronized FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
    Object key = new Object[]{style, timeZone, locale};
    FastDateFormat format = (FastDateFormat) cDateInstanceCache.get(key);

    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        DateFormat df = DateFormat.getDateInstance(style, locale);
        String pattern = null;
        if (df instanceof SimpleDateFormat) {
            pattern = ((SimpleDateFormat) df).toPattern();
        }
        if (pattern == null) {
            pattern = ((SimpleDateFormat)DateFormat.getDateInstance(style, Locale.US)).toPattern();
        }
        format = new FastDateFormat(pattern, timeZone, locale);
        
        cDateInstanceCache.put(key, format);
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

        DateFormat df = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        String pattern = null;
        if (df instanceof SimpleDateFormat) {
            pattern = ((SimpleDateFormat) df).toPattern();
        }
        if (pattern == null) {
            pattern = ((SimpleDateFormat)DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US)).toPattern();
        }
        format = new FastDateFormat(pattern, timeZone, locale);
        
        cDateTimeInstanceCache.put(key, format);
    }

    return format;
}