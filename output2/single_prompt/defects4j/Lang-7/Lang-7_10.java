## Fixed Function 1
```
// must handle Long, Float, Integer, Double, Short,
//                  BigDecimal, BigInteger and Byte
// useful methods:
// Byte.decode(String)
// Byte.valueOf(String,int radix)
// Byte.valueOf(String)
// Double.valueOf(String)
// Float.valueOf(String)
// Float.valueOf(String)
// Integer.valueOf(String,int radix)
// Integer.valueOf(String)
// Integer.decode(String)
// Integer.getInteger(String)
// Integer.getInteger(String,int val)
// Integer.getInteger(String,Integer val)
// Integer.valueOf(String)
// Double.valueOf(String)
// new Byte(String)
// Long.valueOf(String)
// Long.getLong(String)
// Long.getLong(String,int)
// Long.getLong(String,Integer)
// Long.valueOf(String,int)
// Long.valueOf(String)
// Short.valueOf(String)
// Short.decode(String)
// Short.valueOf(String,int)
// Short.valueOf(String)
// new BigDecimal(String)
// new BigInteger(String)
// new BigInteger(String,int radix)
// Possible inputs:
// 45 45.5 45E7 4.5E7 Hex Oct Binary xxxF xxxD xxxf xxxd
// plus minus everything. Prolly more. A lot are not separable.
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
   			if (lastChar == 'd' || lastChar == 'D') {
                try {
                    return createDouble(str);
                } catch (NumberFormatException nfe) {
                    // NOPMD
                    // ignore the bad number
                }
            }
   			if (expPos > -1) {
                if (expPos < str.length() - 1) {
                    exp = str.substring(expPos + 1, str.length());
                } else {
                    exp = null;
                }
                mant = str.substring(0, expPos);
            }
   			if (decPos > -1) {
                if (decPos < str.length() - 1) {
                    dec = str.substring(decPos + 1);
                } else {
                    dec = null;
                }
                mant = str.substring(0, decPos);
   			}
   			if ("fFdD".indexOf(lastChar) > -1 || exp != null) {
                try {
                    return createBigDecimal(str);
                } catch (NumberFormatException nfe) {
                    // NOPMD
                    // ignore the bad number
                }
            }
   			
   			if (dec == null && !StringUtils.contains(str, '.') && !StringUtils.contains(str, 'e') && 
                   !StringUtils.contains(str, 'E') && isInfinite(str) == null) {
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
   			}
   			
   			try {
                BigDecimal bigDec = createBigDecimal(str);
                if (bigDec.compareTo(BigDecimal.valueOf(Long.MIN_VALUE)) >= 0 &&
                		bigDec.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) <= 0) {
                    return Long.valueOf(bigDec.longValueExact());
                }
            } catch (NumberFormatException nfe) {
                // NOPMD
                // ignore the bad number
            }
   			return createBigInteger(str);
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
    // handle JDK1.3.1 bug where "" throws IndexOutOfBoundsException
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }
    if (isInfinite(str)) {
        throw new NumberFormatException("Infinite or NaN value can't be converted to BigDecimal.");
    }
    return new BigDecimal(str);
}

private static Boolean isInfinite(final String str) {
    if (str == null) {
        return null;
    }
    final int len = str.length();
    if (len == 0) {
        return null;
    }
    final char c = str.charAt(0);
    final boolean isNaN = c == 'N' && str.equals("NaN");
    final boolean isInfinite = c == 'I' && str.equals("Infinity");
    final boolean isNegative = len > 1 && (str.charAt(1) == '-' || str.charAt(1) == '+');
    return Boolean.valueOf(isNaN || isInfinite && !isNegative);
}
```