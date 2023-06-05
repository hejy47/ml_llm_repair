## Fixed Function 1
@Override
public OpenMapRealVector ebeDivide(RealVector v) {
    checkVectorDimensions(v.getDimension());
    OpenMapRealVector res = new OpenMapRealVector(this);
    Iterator iter = entries.iterator();
    while (iter.hasNext()) {
        iter.advance();
        double value = iter.value() / v.getEntry(iter.key());
        if (!Double.isNaN(value) && !Double.isInfinite(value)) {
            res.setEntry(iter.key(), value);
        }
    }
    return res;
}

## Fixed Function 2
@Override
public OpenMapRealVector ebeMultiply(RealVector v) {
    checkVectorDimensions(v.getDimension());
    OpenMapRealVector res = new OpenMapRealVector(this);
    Iterator iter = entries.iterator();
    while (iter.hasNext()) {
        iter.advance();
        double value = iter.value() * v.getEntry(iter.key());
        if (!Double.isNaN(value) && !Double.isInfinite(value)) {
            res.setEntry(iter.key(), value);
        }
    }
    return res;
}