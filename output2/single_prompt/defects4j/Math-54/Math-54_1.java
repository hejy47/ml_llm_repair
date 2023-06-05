## Fixed Function 1
protected Dfp(final DfpField field, double x) {
    // initialize as if 0
    mant = new int[field.getRadixDigits()];
    sign = 1;
    exp = 0;
    nans = FINITE;
    this.field = field;
    long bits = Double.doubleToLongBits(x);
    long mantissa = bits & 0x000fffffffffffffL;
    int exponent = (int) ((bits & 0x7ff0000000000000L) >> 52) - 1023;
    if (exponent == -1023) {
        // Zero or sub-normal
        if (x == 0) {
            // make sure 0 has the right sign
            sign = (byte) 0;
            return;
        }
        exponent++;
        // Normalize the subnormal number
        while ((mantissa & 0x0010000000000000L) == 0) {
            exponent--;
            mantissa <<= 1;
        }
        mantissa &= 0x000fffffffffffffL;
    }
    if (exponent == 1024) {
        // infinity or NAN
        if (x != x) {
            sign = (byte) 1;
            nans = QNAN;
        } else if (x < 0) {
            sign = (byte) -1;
            nans = INFINITE;
        } else {
            nans = INFINITE;
        }
        return;
    }
    Dfp xdfp = new Dfp(field, mantissa);
    // Divide by 2^52, then add one
    xdfp = xdfp.divide(new Dfp(field, 4503599627370496l)).add(field.getOne());
    xdfp = xdfp.multiply(DfpMath.pow(field.getTwo(), exponent));
    if ((bits & 0x8000000000000000L) != 0) {
        xdfp = xdfp.negate();
    }
    System.arraycopy(xdfp.mant, 0, mant, 0, mant.length);
    sign = xdfp.sign;
    exp = xdfp.exp;
    nans = xdfp.nans;
}

## Fixed Function 2
public double toDouble() {
    if (isInfinite()) {
        if (lessThan(getZero())) {
            return Double.NEGATIVE_INFINITY;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }
    if (isNaN()) {
        return Double.NaN;
    }
    Dfp y = this;
    boolean negate = false;
    if (lessThan(getZero())) {
        y = negate();
        negate = true;
    }
        int exponent = (int) (y.log10() * 3.3219280948873626);
    if (exponent < 0) {
        exponent--;
    }
    Dfp tempDfp = DfpMath.pow(getTwo(), exponent);
    while (tempDfp.lessThan(y) || tempDfp.equals(y)) {
        tempDfp = tempDfp.multiply(2);
        exponent++;
    }
    exponent--;
        y = y.divide(DfpMath.pow(getTwo(), exponent));
    if (exponent > -1023) {
        y = y.subtract(getOne());
    }
    if (exponent < -1074) {
        return 0;
    }
    if (exponent > 1023) {
        return negate ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
    }
    y = y.multiply(newInstance(4503599627370496l)).rint();
    String str = y.toString();
    str = str.substring(0, str.length() - 1);
    long mantissa = Long.parseLong(str);
    if (mantissa == 4503599627370496L) {
        // Handle special case where we round up to next power of two
        mantissa = 0;
        exponent++;
    }
        if (exponent <= -1023) {
        exponent--;
    }
    while (exponent < -1023) {
        exponent++;
        mantissa >>>= 1;
    }
    long bits = mantissa | ((long)(exponent + 1023) << 52);
    double x = Double.longBitsToDouble(bits);
    if (negate) {
        x = -x;
    }
    return x;
}