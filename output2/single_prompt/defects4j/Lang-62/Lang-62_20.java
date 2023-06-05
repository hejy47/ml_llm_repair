## Fixed Function 1
public String unescape(String str) {
    int firstAmp = str.indexOf('&');
    if (firstAmp < 0) {
        return str;
    }
    StringBuilder buf = new StringBuilder(str.substring(0, firstAmp));
    for (int i = firstAmp; i < str.length();) {
        if (str.charAt(i) == '&') {
            int semi = str.indexOf(';', i + 1);
            if (semi == -1) {
                buf.append('&');
                break;
            }
            String entityName = str.substring(i + 1, semi);
            int entityValue;
            if (entityName.length() == 0) {
                entityValue = -1;
            } else if (entityName.charAt(0) == '#') {
                if (entityName.length() == 1) {
                    entityValue = -1;
                } else {
                    char charAt1 = entityName.charAt(1);
                    try {
                        if (charAt1 == 'x' || charAt1 == 'X') {
                            entityValue = Integer.parseInt(entityName.substring(2), 16);
                        } else {
                            entityValue = Integer.parseInt(entityName.substring(1));
                        }
                    } catch (NumberFormatException ex) {
                        entityValue = -1;
                    }
                }
            } else {
                entityValue = this.entityValue(entityName);
            }
            if (entityValue == -1) {
                buf.append('&');
                buf.append(entityName);
                buf.append(';');
            } else {
                buf.append((char) entityValue);
            }
            i = semi + 1;
        } else {
            buf.append(str.charAt(i));
            i++;
        }
    }
    return buf.toString();
}

## Fixed Function 2
public void unescape(Writer writer, String string) throws IOException {
    int len = string.length();
    StringBuilder builder = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
        char c = string.charAt(i);
        if (c == '&') {
            int nextIdx = i + 1;
            int semiColonIdx = string.indexOf(';', nextIdx);
            if (semiColonIdx == -1) {
                builder.append(c);
                continue;
            }
            int amphersandIdx = string.indexOf('&', i + 1);
            if (amphersandIdx != -1 && amphersandIdx < semiColonIdx) {
                // Then the text looks like &...&...;
                builder.append(c);
                continue;
            }
            String entityContent = string.substring(nextIdx, semiColonIdx);
            int entityValue = -1;
            int entityContentLen = entityContent.length();
            if (entityContentLen > 0) {
                if (entityContent.charAt(0) == '#') {
                    //escaped value content is an integer (decimal or hexidecimal)
                    if (entityContentLen > 1) {
                        char isHexChar = entityContent.charAt(1);
                        try {
                            switch(isHexChar) {
                                case 'X':
                                case 'x':
                                    {
                                        entityValue = Integer.parseInt(entityContent.substring(2), 16);                                                
                                    }
                                default:
                                    {
                                        entityValue = Integer.parseInt(entityContent.substring(1), 10);                                                
                                    }
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                } else {
                    //escaped value content is an entity name
                    entityValue = this.entityValue(entityContent);
                }
            }
            if (entityValue == -1) {
                builder.append('&');
                builder.append(entityContent);
                builder.append(';');
            } else {
                builder.append((char) entityValue);
            }
            //move index up to the semi-colon
            i = semiColonIdx;
        } else {
            builder.append(c);
        }
    }
    writer.write(builder.toString());
}