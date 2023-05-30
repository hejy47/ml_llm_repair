## Fixed Function 1
public Complex parse(String source, ParsePosition pos) {
    int initialIndex = pos.getIndex();
    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);
    // parse real
    Number re = parseNumber(source, getRealFormat(), pos);
    if (re == null) {
        // invalid real number
        // set index back to initial, error index should already be set
        // character examined.
        pos.setIndex(initialIndex);
        return null;
    }
    // parse sign
    int startIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    int sign = 0;
    switch(c) {
        case 0:
            // no sign
            // return real only complex number
            return new Complex(re.doubleValue(), 0.0);
        case '-':
            sign = -1;
            break;
        case '+':
            sign = 1;
            break;
        default:
            // invalid sign
            // set index back to initial, error index should be the last
            // character examined.
            pos.setIndex(initialIndex);
            pos.setErrorIndex(startIndex);
            return null;
    }
    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);
    // parse imaginary
    Number im = parseNumber(source, getRealFormat(), pos);
    if (im == null) {
        // invalid imaginary number
        // set index back to initial, error index should already be set
        // character examined.
        pos.setIndex(initialIndex);
        return null;
    }
    // parse imaginary character
    String imaginaryCharacter = getImaginaryCharacter();
    if (source.regionMatches(pos.getIndex(), imaginaryCharacter, 0, imaginaryCharacter.length())) {
        pos.setIndex(pos.getIndex() + imaginaryCharacter.length());
        return new Complex(re.doubleValue(), im.doubleValue() * sign);
    } else {
        // set index back to initial, error index should be the start index
        // character examined.
        pos.setIndex(initialIndex);
        pos.setErrorIndex(pos.getIndex());
        return null;
    }
}