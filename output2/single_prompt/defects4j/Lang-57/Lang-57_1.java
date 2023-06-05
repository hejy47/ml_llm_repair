## Fixed Function 1
//-----------------------------------------------------------------------
public static boolean isAvailableLocale(Locale locale) {
    return cAvailableLocaleSet.contains(locale.toString());
}