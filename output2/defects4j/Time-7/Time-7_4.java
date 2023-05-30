## Fixed Function 1
//-----------------------------------------------------------------------
public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    
    // calculate instantLocal based on the input position
    DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(parser).toFormatter();
    ParsePosition parsePosition = new ParsePosition(position);
    DateTime parsedDateTime = formatter.parseDateTime(text, parsePosition);
    long instantLocal = parsedDateTime.getMillis();
    
    // calculate the actual instant millis
    chrono = selectChronology(chrono);
    int defaultYear = chrono.year().get(instantLocal);
    DateTimeParserBucket bucket = new DateTimeParserBucket(instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    instant.setMillis(bucket.computeMillis(false, text));
    
    // handle parsed zone information
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }
    instant.setChronology(chrono);
    if (iZone != null) {
        instant.setZone(iZone);
    }
    return newPos;
}