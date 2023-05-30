## Fixed Function 1
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
        // drop 0x
        int hexDigits = str.length() - 2;
        if (str.startsWith("-")) {
            // drop -
            hexDigits--;
        }
        if (hexDigits > 8) {
            // too many for an int
            return createLong(str);
        }
        return createInteger(str);
    }
    char lastChar = str.charAt(str.length() - 1);
    String mant;
    String dec;
    String exp;
    int decPos = str.indexOf('.');
    int expPos = str.indexOf('e') + str.indexOf('E') + 1;
    if (decPos > -1) {
        if (expPos > -1 && expPos < str.length() - 1) {
            dec = str.substring(decPos + 1, expPos);
            exp = str.substring(expPos + 1);
        } else {
            dec = str.substring(decPos + 1);
            exp = null;
        }
        mant = str.substring(0, decPos);
    } else {
        if (expPos > -1 && expPos < str.length() - 1) {
            mant = str.substring(0, expPos);
            exp = str.substring(expPos + 1);
        } else {
            mant = str;
            exp = null;
        }
        dec = null;
    }
    if (!Character.isDigit(lastChar) && lastChar != '.') {
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1, str.length() - 1);
        } else {
            exp = null;
        }
        //Requesting a specific type..
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
                    Float f = NumberUtils.createFloat(numeric);
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
                    Double d = NumberUtils.createDouble(numeric);
                    if (!(d.isInfinite() || (d.floatValue() == 0.0D && !allZeros))) {
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
        //User doesn't have a preference on the return type, so let's start
        //small and go from there...
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1, str.length());
        } else {
            exp = null;
        }
        if (dec == null && exp == null) {
            //Must be an int,long,bigint
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
            //Must be a float,double,BigDec
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

## Fixed Function 2
public static BigDecimal createBigDecimal(String str) {
    if (str == null) {
        return null;
    }
    // handle JDK1.3.1 bug where "" throws IndexOutOfBoundsException
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }
    str = StringUtils.trim(str); // trim before checking for signs
    if (str.charAt(0) == '+') {
        str = str.substring(1);
    }
    // handle possible leading sign
    boolean isNegative = false;
    if (str.charAt(0) == '-') {
        isNegative = true;
        str = str.substring(1);
    }
    if (str.equals("NaN")) {
        return BigDecimal.valueOf(Double.NaN);
    } else if (str.equals("Infinity") || str.equals("+Infinity")) {
        return BigDecimal.valueOf(Double.POSITIVE_INFINITY);
    } else if (str.equals("-Infinity")) {
        return BigDecimal.valueOf(Double.NEGATIVE_INFINITY);
    }
    try {
        Long.parseLong(str); //for input validation of longs
    } catch (NumberFormatException nfe) {
        // NOPMD
    }
    // everything after this is optional.
    int decPos = str.indexOf('.');
    String intString;
    String fractionString;
    if (decPos > -1) {
        intString = str.substring(0, decPos);
        fractionString = str.substring(decPos + 1);
    } else {
        intString = str;
        fractionString = null;
    }
    if (isScale(str)) {
        int scale = Integer.parseInt(str.substring(str.indexOf('e') + 1));
        if (fractionString == null) {
            intString = intString + zeros(scale);
        } else if (fractionString.length() > scale) {
            intString = intString + fractionString.substring(0, scale);
            fractionString = fractionString.substring(scale);
        } else {
            fractionString = zeros(scale - fractionString.length()) + fractionString;
        }
    }
    if (intString.length() > 16) {
        // if parseLong throws an exception, lets not caught it and fall into the catch (NumberFormatException) below
        if (!allZeros(intString) && !allNonzeros(intString)) {
            BigDecimal value = isNegative ? BigDecimal.valueOf(-1) : BigDecimal.valueOf(1);
            BigInteger intVal = new BigInteger(intString);
            BigInteger fractionVal = fractionString == null ? BigInteger.ZERO : new BigInteger(fractionString);
            return value.multiply(new BigDecimal(intVal)).add(new BigDecimal(fractionVal, fractionString == null ? 0 : fractionString.length()));
        }
    }
    try {
        double value = Double.parseDouble(str); //for input validation of doubles
        //noinspection FloatingPointEquality
        if (value == 0 && isNegative) {
            return BigDecimal.valueOf(-0.0);
        }
        return BigDecimal.valueOf(value);
    } catch (NumberFormatException nfe) {
        NumberFormatException newNfe = new NumberFormatException("Failed to convert value '" + str + "' to a BigDecimal");
        newNfe.initCause(nfe);
        throw newNfe;
    }
}

private static boolean isScale(String str) {
    int index = str.indexOf('e');
    if (index == -1) {
        index = str.indexOf('E');
    }
    return index > -1 && str.substring(index).matches("[eE][-+]?[0-9]+");
}

private static String zeros(int count) {
    if (count < 0) {
        throw new IllegalArgumentException("Count must be greater than or equal to zero");
    }
    StringBuilder buf = new StringBuilder(count);
    for (int i = 0; i < count; i++) {
        buf.append('0');
    }
    return buf.toString();
}

private static boolean allZeros(String str) {
    if (StringUtils.isEmpty(str)) {
        return true;
    }
    for (char c : str.toCharArray()) {
        if (c != '0') {
            return false;
        }
    }
    return true;
}

private static boolean allNonzeros(String str) {
    if (StringUtils.isEmpty(str)) {
        return true;
    }
    for (char c : str.toCharArray()) {
        if (c == '0') {
            return false;
        }
    }
    return true;
}