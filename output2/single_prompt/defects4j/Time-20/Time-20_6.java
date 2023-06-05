## Fixed Function 1
public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            DateTimeZone zone = null;
            try {
                zone = DateTimeZone.forID(id);
            } catch (IllegalArgumentException e) {
                // Zone ID is invalid, ignore it
            }
            
            if (zone != null) {
                bucket.setZone(zone);
                return position + id.length();
            }
        }
    }
    return ~position;
}