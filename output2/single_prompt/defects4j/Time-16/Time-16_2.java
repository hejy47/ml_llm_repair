## Fixed Function 1
//-----------------------------------------------------------------------
public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    Chronology chrono = instant.getChronology();
    DateTimeParserBucket bucket;
    if (iZone != null) {
        chrono = chrono.withZone(iZone);
        bucket = new DateTimeParserBucket(instant.getMillis(), chrono, iLocale, iPivotYear, iDefaultYear);
    } else {
        long instantMillis = instant.getMillis();
        long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
        chrono = selectChronology(chrono);
        bucket = new DateTimeParserBucket(instantLocal, chrono, iLocale, iPivotYear, iDefaultYear);
    }
    int newPos = parser.parseInto(bucket, text, position);
    instant.setMillis(bucket.computeMillis(false, text));
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }
    instant.setChronology(chrono);
    return newPos;
}