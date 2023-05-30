## Fixed Function 1
//-----------------------------------------------------------------------
public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    Chronology chrono = instant.getChronology();
    DateTimeParserBucket bucket = new DateTimeParserBucket(instant, chrono, iLocale, iPivotYear);
    int newPos = parser.parseInto(bucket, text, position);
    instant.setMillis(bucket.computeMillis(false, text));
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
        instant.setChronology(chrono);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
        instant.setChronology(chrono);
    }
    if (iZone != null) {
        instant.setZone(iZone);
    }
    return newPos;
}