## Fixed Function 1
public static synchronized FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
    Object key = new Triple(style, timeZone, locale);
    FastDateFormat format = (FastDateFormat) cDateInstanceCache.get(key);
    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance(style, locale);
       
        // Apply time zone if provided
        if (timeZone != null) {
            formatter.setTimeZone(timeZone);
        }
        
        String pattern = formatter.toPattern();
        format = getInstance(pattern, timeZone, locale);
        cDateInstanceCache.put(key, format);
    }
    return format;
}

## Fixed Function 2
public static synchronized FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
    Object key = new Quadruple(dateStyle, timeStyle, timeZone, locale);
    FastDateFormat format = (FastDateFormat) cDateTimeInstanceCache.get(key);
    if (format == null) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        
        // Apply time zone if provided
        if (timeZone != null) {
            formatter.setTimeZone(timeZone);
        }
        
        String pattern = formatter.toPattern();
        format = getInstance(pattern, timeZone, locale);
        cDateTimeInstanceCache.put(key, format);
    }
    return format;
}