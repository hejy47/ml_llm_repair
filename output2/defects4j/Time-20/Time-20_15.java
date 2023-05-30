## Fixed Function 1
public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            int idLength = id.length();
            if (str.length() == idLength || !Character.isLetterOrDigit(str.charAt(idLength))) {
                bucket.setZone(DateTimeZone.forID(id));
                return position + idLength;
            }
        }
    }
    return ~position;
}