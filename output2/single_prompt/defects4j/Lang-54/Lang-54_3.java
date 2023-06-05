## Fixed Function 1
//-----------------------------------------------------------------------
public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    if (len != 2 && len != 5 && len < 7) {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    if (len == 2) {
        return new Locale(str, "");
    } else {
        if (str.charAt(2) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }

        for(int i = 3; i < len; i++) {
            char ch = str.charAt(i);
            if((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z') && (ch != '_')) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
        }

        if (len == 5) {
            return new Locale(str.substring(0, 2), str.substring(3, 5));
        } else {
            if (str.charAt(5) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }

            return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
        }
    }
}