## Fixed Function 1
public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            if (id.equals("GMT") || id.startsWith("+") || id.startsWith("-")) {
                DateTimeZone zone = DateTimeZone.forID(id);
                bucket.setZone(zone);
                return position + id.length();
            } else {
                // Only return valid time zone IDs
                List<String> validIDs = Arrays.asList(DateTimeZone.getAvailableIDs());
                if (validIDs.contains(id)) {
                    DateTimeZone zone = DateTimeZone.forID(id);
                    bucket.setZone(zone);
                    return position + id.length();
                } else {
                    return ~position; // invalid time zone ID
                }
            }
        }
    }
    return ~position; // no time zone ID found
}