## Fixed Function 1
public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            // Check if the remaining string after the matched ID is a valid time zone offset
            String remainingStr = str.substring(id.length());
            if (DateTimeUtils.isValidTimeZoneOffset(remainingStr)) {
                bucket.setZone(DateTimeZone.forOffsetMillis(DateTimeUtils.getOffsetFromFormattedString(remainingStr)));
                return position + id.length() + remainingStr.length();
            } else {
                // If it is not a valid time zone offset, set the time zone using the ID
                bucket.setZone(DateTimeZone.forID(id));
                return position + id.length();
            }
        }
    }
    return ~position;
}