## Fixed Function 1
//-----------------------------------------------------------------------
public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono);
    DateTimeParserBucket bucket = new DateTimeParserBucket(instantLocal, chrono, iLocale, iPivotYear, iDefaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    long millis = bucket.computeMillis(false, text);
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        instant.setMillis(millis - parsedOffset);
        instant.setChronology(chrono.withZone(parsedZone));
    } else {
        instant.setMillis(millis);
        instant.setChronology(chrono);
        if (bucket.getZone() != null) {
            instant.setZone(bucket.getZone());
        } else if (iZone != null) {
            instant.setZone(iZone);
        }
    }
    return newPos;
}