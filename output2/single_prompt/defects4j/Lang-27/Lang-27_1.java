## Fixed Function 1
// method to check if a string contains all zeros
private static boolean isAllZeros(String str) {
    if (str == null) {
        return true;
    }
    for (int i = str.length() - 1; i >= 0; i--) {
        if (str.charAt(i) != '0') {
            return false;
        }
    }
    return str.length() > 0;
}

// method to create a number from a string
public static Number createNumber(String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    str = str.trim();
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }
    if (str.startsWith("--")) {
        // this is protection for poorness in java.lang.BigDecimal.
        // it accepts this as a legal value, but it does not appear
        // to be in specification of class. OS X Java parses it to
        // a wrong value.
        return null;
    }
    char lastChar = str.charAt(str.length() - 1);
    int decPos = str.indexOf('.');
    int expPos = str.indexOf('e') + str.indexOf('E') + 1;
    if (expPos > 0 && decPos > 0 && expPos < decPos) {
        throw new NumberFormatException(str + " is not a valid number.");
    }
    if (!Character.isDigit(lastChar) && lastChar != '.') {
        if (expPos > 0 && expPos < str.length() - 1) {
            String numeric = str.substring(0, expPos);
            String suffix = str.substring(expPos + 1);
            if (isAllZeros(suffix)) {
                return createNumber(numeric);
            }
            switch (lastChar) {
                case 'f':
                case 'F':
                    try {
                        Float f = NumberUtils.createFloat(numeric);
                        if (!(f.isInfinite() || f.floatValue() == 0.0F)) {
                            return f;
                        }
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                    //$FALL-THROUGH$
                case 'd':
                case 'D':
                    try {
                        Double d = NumberUtils.createDouble(numeric);
                        if (!(d.isInfinite() || d.doubleValue() == 0.0D)) {
                            return d;
                        }
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                    try {
                        return NumberUtils.createBigDecimal(numeric);
                    } catch (NumberFormatException e) {
                        // ignore the bad number
                    }
                    break;
                case 'l':
                case 'L':
                    if (str.indexOf('F') > 0 || str.indexOf('f') > 0) {
                        throw new NumberFormatException(str + " is not a valid number.");
                    }
                    try {
                        return NumberUtils.createLong(numeric);
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                    return NumberUtils.createBigInteger(numeric);

                default:
                    throw new NumberFormatException(str + " is not a valid number.");
            }
        } else {
            try {
                return NumberUtils.createInteger(str);
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            try {
                return NumberUtils.createLong(str);
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            return NumberUtils.createBigInteger(str);
        }
    } else {
        // User doesn't have a preference on the return type, so let's start
        // with BigInteger and work our way down to Double
        if (expPos > 0 && expPos < str.length() - 1) {
            String numeric = str.substring(0, expPos);
            String suffix = str.substring(expPos + 1);
            if (isAllZeros(suffix)) {
                return createNumber(numeric);
            }
            try {
                return NumberUtils.createBigDecimal(str);
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            try {
                Double d = NumberUtils.createDouble(numeric + suffix);
                if (!(d.isInfinite() || d.doubleValue() == 0.0D)) {
                    return d;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            return NumberUtils.createBigInteger(str);
        } else {
            try {
                return NumberUtils.createInteger(str);
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            try {
                return NumberUtils.createLong(str);
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            try {
                return NumberUtils.createBigInteger(str);
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            return NumberUtils.createDouble(str);
        }
    }
}