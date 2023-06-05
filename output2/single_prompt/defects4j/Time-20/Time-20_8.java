## Fixed Function 1
public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            int length = id.length();
            if (length == 3 || length == 4 || length == 5) { // zone ID must have a defined length
                bucket.setZone(DateTimeZone.forID(id));
                return position + length;
            }
        }
    }
    return ~position;
}