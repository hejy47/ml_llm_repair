## Fixed Function 1
public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            if (id.equals("GMT")) {
                bucket.setZone(DateTimeZone.UTC);
            } else {
                bucket.setZone(DateTimeZone.forID(id));
            }
            return position + id.length();
        }
    }
    return ~position;
}