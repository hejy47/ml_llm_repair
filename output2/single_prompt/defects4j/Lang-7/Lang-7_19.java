## Fixed Function 1
```
public static Number createNumber(String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }
    if (str.startsWith("--")) {
        return null;
    }
    if (str.startsWith("0x") || str.startsWith("-0x") || str.startsWith("0X") || str.startsWith("-0X")) {
        String numeric = str.substring(0, str.length() - 1);
        try {
            if (str.length() - 2 > 8) {
                return createLong(numeric);
            }
            return createInteger(numeric);
        } catch (NumberFormatException nfe) {
            if (str.length() - 2 > 16) {
                return createBigInteger(numeric);
            } else {
                return createLong(numeric);
            }
        }
    }
    char lastChar = str.charAt(str.length() - 1);
    String mant;
    String dec;
    String exp;
    int decPos = str.indexOf('.');
    int expPos = str.indexOf('e') + str.indexOf('E') + 1;
    if (decPos > -1) {
        if (expPos > -1) {
            if (expPos < decPos || expPos > str.length()) {
                throw new NumberFormatException(str + " is not a valid number.");
            }
            dec = str.substring(decPos + 1, expPos);
        } else {
            dec = str.substring(decPos + 1);
        }
        mant = str.substring(0, decPos);
    } else {
        if (expPos > -1) {
            if (expPos > str.length()) {
                throw new NumberFormatException(str + " is not a valid number.");
            }
            mant = str.substring(0, expPos);
        } else {
            mant = str;
        }
        dec = null;
    }
    if (!Character.isDigit(lastChar) && lastChar != '.') {
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1, str.length() - 1);
        } else {
            exp = null;
        }
        String numeric = str.substring(0, str.length() - 1);
        boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
        switch(lastChar) {
            case 'l':
            case 'L':
                if (dec == null && exp == null && (numeric.charAt(0) == '-' && isDigits(numeric.substring(1)) || isDigits(numeric))) {
                    try {
                        return createLong(numeric);
                    } catch (NumberFormatException nfe) {
                        // NOPMD
                        // Too big for a long
                    }
                    return createBigInteger(numeric);
                }
                throw new NumberFormatException(str + " is not a valid number.");
            case 'f':
            case 'F':
                try {
                    Float f = createFloat(numeric);
                    if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                        //If it's too big for a float or the float value = 0 and the string
                        //has non-zeros in it, then float does not have the precision we want
                        return f;
                    }
                } catch (NumberFormatException nfe) {
                    // NOPMD
                    // ignore the bad number
                }
            //$FALL-THROUGH$
            case 'd':
            case 'D':
                try {
                    Double d = createDouble(numeric);
                    if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (NumberFormatException nfe) {
                    // NOPMD
                    // ignore the bad number
                }
                try {
                    return createBigDecimal(numeric);
                } catch (NumberFormatException e) {
                    // NOPMD
                    // ignore the bad number
                }
            //$FALL-THROUGH$
            default:
                throw new NumberFormatException(str + " is not a valid number.");
        }
    } else {
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1, str.length());
        } else {
            exp = null;
        }
        if (dec == null && exp == null) {
            try {
                return createInteger(str);
            } catch (NumberFormatException nfe) {
                // NOPMD
                // ignore the bad number
            }
            try {
                return createLong(str);
            } catch (NumberFormatException nfe) {
                // NOPMD
                // ignore the bad number
            }
            return createBigInteger(str);
        } else {
            boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
            try {
                Float f = createFloat(str);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
                // NOPMD
                // ignore the bad number
            }
            try {
                Double d = createDouble(str);
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                    return d;
                }
            } catch (NumberFormatException nfe) {
                // NOPMD
                // ignore the bad number
            }
            return createBigDecimal(str);
        }
    }
}
```

## Fixed Function 2
```
public static BigDecimal createBigDecimal(String str) {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }
    try {
        return new BigDecimal(str);
    } catch (NumberFormatException nfe) {
        if (str.startsWith("+")) {
            return new BigDecimal(str.substring(1));
        } else if (str.startsWith("-+")) {
            return new BigDecimal(str.substring(2));
        } else if (str.startsWith("--")) {
            // this is protection for poorness in java.lang.BigDecimal.
            // it accepts this as a legal value, but it does not appear
            // to be in specification of class. OS X Java parses it to
            // a wrong value.
            return new BigDecimal(str.replace("--", "-"));
        } else {
            // throw the exception
            throw nfe;
        }
    }
}
```