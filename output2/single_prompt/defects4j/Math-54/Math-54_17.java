## Fixed Function 1
protected Dfp(final DfpField field, double x) {
    // initialize as if 0
    mant = new int[field.getRadixDigits()];
    sign = 1;
    exp = 0;
    nans = FINITE;
    this.field = field;

    // Check for NaN
    if (Double.isNaN(x)) {
        sign = (byte) 1;
        nans = QNAN;
        return;
    }

    long bits = Double.doubleToLongBits(x);
    long mantissa = bits & 0x000fffffffffffffL;
    int exponent = (int) ((bits & 0x7ff0000000000000L) >> 52) - 1023;
    if (exponent == -1023) {
        // Zero or sub-normal
        if (x == 0) {
            // make sure 0 has the right sign
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
            sign = (byte) 1;
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
    int exponent = (int) (y.log10() / Math.log10(2));
    if (exponent < 0) {
        exponent--;
    }
    Dfp tempDfp = DfpMath.pow(getTwo(), exponent);
    while (tempDfp.lessThan(y)) {
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
    y = y.multiply(newInstance(4503599627370496l));
    long bits;
    if (y.lessThan(getZero())) {
        bits = 0x8000000000000000L;
        y = y.negate();
    } else {
        bits = 0;
    }
    bits |= ((long) exponent + 1023L) << 52;
    Dfp half = new Dfp(y.field, 0.5);
    if (y.greaterThan(half) || y.equals(half) && ((bits & 0x000fffffffffffffL) != 0L)) {
        y = y.add(getOne());
    }
    for (int i = 51; i >= 0; i--) {
        if (y.greaterThan(DfpMath.pow(getTwo(), i))) {
            bits |= 1L << i;
            y = y.subtract(DfpMath.pow(getTwo(), i));
        }
    }
    double result = Double.longBitsToDouble(bits);
    if (negate) {
        result = -result;
    }
    return result;
}