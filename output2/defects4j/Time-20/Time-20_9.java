## Fixed Function 1
public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            if (id.equals("GMT") && str.length() > 3 && 
                Character.toUpperCase(str.charAt(3)) == 'T') {
                // handle case where "GMT" is part of ISO8601 offset
                continue;
            }
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }
    return ~position;
}