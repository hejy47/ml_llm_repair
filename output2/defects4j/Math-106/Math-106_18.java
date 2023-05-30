## Fixed Function 1
public Fraction parse(String source, ParsePosition pos) {
    // try to parse improper fraction
    Fraction ret = super.parse(source, pos);
    if (ret != null) {
        return ret;
    }
    int initialIndex = pos.getIndex();
    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);
    // parse whole
    Number whole = getWholeFormat().parse(source, pos);
    if (whole == null) {
        // invalid integer number
        // set index back to initial, error index should already be set
        // character examined.
        pos.setIndex(initialIndex);
        return null;
    }
    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);
    // parse numerator
    Number num = getNumeratorFormat().parse(source, pos);
    if (num == null) {
        // invalid integer number
        // set index back to initial, error index should already be set
        // character examined.
        pos.setIndex(initialIndex);
        return null;
    }
    int startIndex = pos.getIndex();
    // parse '/'
    char c = parseNextCharacter(source, pos);
    switch (c) {
        case 0:
            // no '/', return a fraction with the whole as the numerator and 1 as the denominator
            return new Fraction(whole.intValue(), 1);
        case '/':
            // found '/', continue parsing denominator
            break;
        default:
            // invalid '/'
            // set index back to initial, error index should be the last
            // character examined.
            pos.setIndex(initialIndex);
            pos.setErrorIndex(startIndex);
            return null;
    }
    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);
    // parse denominator
    Number den = getDenominatorFormat().parse(source, pos);
    if (den == null) {
        // invalid integer number
        // set index back to initial, error index should already be set
        // character examined.
        pos.setIndex(initialIndex);
        return null;
    }
    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();
    // check for leading minus sign
    if (w < 0 || n < 0 || d < 0) {
        pos.setIndex(initialIndex);
        return null;
    }
    return new Fraction(((Math.abs(w) * d) + n) * MathUtils.sign(w), d);
}