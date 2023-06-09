## Fixed Function 1
public String unescape(String str) {
    int firstAmp = str.indexOf('&');
    if (firstAmp < 0) {
        return str;
    }
    StringBuilder buf = new StringBuilder(str.length());
    buf.append(str.substring(0, firstAmp));
    for (int i = firstAmp; i < str.length(); i++) {
        char ch = str.charAt(i);
        if (ch == '&') {
            int semi = str.indexOf(';', i + 1);
            if (semi == -1) {
                buf.append(ch);
                continue;
            }
            int amph = str.indexOf('&', i + 1);
            if (amph != -1 && amph < semi) {
                // Then the text looks like &...&...;
                buf.append(ch);
                continue;
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
            i = semi;
        } else {
            buf.append(ch);
        }
    }
    return buf.toString();
}

## Fixed Function 2
public void unescape(Writer writer, String string) throws IOException {
    int firstAmp = string.indexOf('&');
    if (firstAmp < 0) {
        writer.write(string);
        return;
    }
    writer.write(string, 0, firstAmp);
    int len = string.length();
    for (int i = firstAmp; i < len; i++) {
        char c = string.charAt(i);
        if (c == '&') {
            int nextIdx = i + 1;
            int semiColonIdx = string.indexOf(';', nextIdx);
            if (semiColonIdx == -1) {
                writer.write(c);
                continue;
            }
            int amphersandIdx = string.indexOf('&', i + 1);
            if (amphersandIdx != -1 && amphersandIdx < semiColonIdx) {
                // Then the text looks like &...&...;
                writer.write(c);
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
                                        break;
                                    }
                                default:
                                    {
                                        entityValue = Integer.parseInt(entityContent.substring(1), 10);
                                        break;
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
                writer.write('&');
                writer.write(entityContent);
                writer.write(';');
            } else {
                writer.write(entityValue);
            }
            //move index up to the semi-colon
            i = semiColonIdx;
        } else {
            writer.write(c);
        }
    }
}