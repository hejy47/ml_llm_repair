## Fixed Function 1
protected Dfp(final DfpField field, double x) {
    // initialize as if 0
    mant = new int[field.getRadixDigits()];
    sign = 1;
    exp = 0;
    nans = FINITE;
    this.field = field;

    if (Double.isNaN(x)) {
        sign = (byte) 1;
        nans = QNAN;
        return;
    }

    if ( Double.isInfinite(x)) {
        sign = x > 0 ? (byte) 1 : (byte) -1;
        nans = INFINITE;
        return;
    }

    if (x == 0.0) {
        return;
    }

    if (x < 0) {
        sign = -1;
        x = -x;
    } else {
        sign = 1;
    }

    int exponent = (int) Math.floor(Math.log(x) / Math.log(2));
    x = x * Math.pow(2, -exponent);

    Dfp xdfp = new Dfp(field);
    xdfp.exp = exponent + 1;

    for (int i = 0; i < field.getRadixDigits(); i++) {
        x *= 2.0;
        if (x >= 1.0) {
            xdfp.mant[i] = 1;
            x -= 1.0;
        } else {
            xdfp.mant[i] = 0;
        }
    }
    xdfp.normalize();
    if (sign == -1) {
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
        return sign > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
    }
    if (isNaN()) {
        return Double.NaN;
    }
    Dfp y = abs();
    boolean negate = sign < 0;
    int exponent = exp;
    Dfp tempDfp;
    if (lessThan(DfpMath.getOne())) {
        tempDfp = y.multiply(DfpMath.pow(DfpMath.getTwo(), 1074));
        exponent -= 1076;
    } else {
        tempDfp = y;
    }
    tempDfp = tempDfp.multiply(DfpMath.pow(DfpMath.getTwo(), 52 - getRadixDigits()));
    long bits = tempDfp.mant[getRadixDigits() - 1];
    for (int i = getRadixDigits() - 2; i >= 0; i--) {
        bits <<= 32;
        bits |= tempDfp.mant[i] & 0xffffffffL;
    }
    bits >>>= 12;
    exponent += (int) (bits >>> 52);
    bits &= 0x000fffffffffffffL;
    bits |= ((long) (exponent + 1023)) << 52;
    double x = Double.longBitsToDouble(bits);
    return negate ? -x : x;
}