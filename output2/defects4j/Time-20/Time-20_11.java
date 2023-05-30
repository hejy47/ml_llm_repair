## Fixed Function 1
public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            if (id.equals("GMT") || id.equals("UTC")) {
                bucket.setZone(DateTimeZone.forID(id));
            } else {
                try {
                    bucket.setZone(DateTimeZone.forID(id));
                } catch (IllegalArgumentException e) {
                    // ignore invalid time zone ID and continue searching for a valid one
                    continue;
                }
            }
            return position + id.length();
        }
    }
    return ~position;
}