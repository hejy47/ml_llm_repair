## Fixed Function 1
//-----------------------------------------------------------------------
public static boolean isAvailableLocale(Locale locale) {
    for(Locale loc: Locale.getAvailableLocales()){
        if(loc.equals(locale)){
            return true;
        }
    }
    return false;
}