## Fixed Function 1
public Fraction parse(String source, ParsePosition pos) {
    Fraction ret = super.parse(source, pos);
    if (ret != null) {
        return ret;
    }
    int initialIndex = pos.getIndex();
    parseAndIgnoreWhitespace(source, pos);
    Number whole = getWholeFormat().parse(source, pos);
    if (whole == null) {
        pos.setIndex(initialIndex);
        return null;
    }
    parseAndIgnoreWhitespace(source, pos);
    Number num = getNumeratorFormat().parse(source, pos);
    if (num == null) {
        pos.setIndex(initialIndex);
        return null;
    }
    int startIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    switch(c) {
        case 0:
            return new Fraction(num.intValue(), 1);
        case '/':
            break;
        default:
            pos.setIndex(initialIndex);
            pos.setErrorIndex(startIndex);
            return null;
    }
    parseAndIgnoreWhitespace(source, pos);
    Number den = getDenominatorFormat().parse(source, pos);
    if (den == null) {
        pos.setIndex(initialIndex);
        return null;
    }
    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();
    if (w < 0 && n > 0 || w > 0 && n < 0) {
        n = -n;
    }
    return new Fraction(Math.abs(w) * d + n, d);
}