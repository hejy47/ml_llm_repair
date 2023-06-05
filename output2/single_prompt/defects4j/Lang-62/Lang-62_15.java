## Fixed Function 1
public String unescape(String str) {   
    int firstAmp = str.indexOf('&');
    if (firstAmp < 0) {
        return str;
    }
    StringBuffer buf = new StringBuffer(str.length());
    buf.append(str.substring(0, firstAmp));
    for (int i = firstAmp; i < str.length();) {
        char ch = str.charAt(i++);
        if (ch == '&') {
            int semi = str.indexOf(';', i);
            if (semi == -1) {
                buf.append(ch);
                continue;
            }
            String entityName = str.substring(i, semi);
            i = semi + 1;
            if (entityName.equals("lt")) {
                buf.append('<');
            } else if (entityName.equals("gt")) {
                buf.append('>');
            } else if (entityName.equals("amp")) {
                buf.append('&');
            } else if (entityName.equals("quot")) {
                buf.append('"');
            } else if (entityName.equals("apos")) {
                buf.append('\'');
            } else if (entityName.startsWith("#")) {
                try {
                    if (entityName.startsWith("#x")) {
                        buf.append(Character.toChars(Integer.parseInt(entityName.substring(2), 16)));
                    } else {
                        buf.append(Character.toChars(Integer.parseInt(entityName.substring(1))));
                    }
                } catch (NumberFormatException ex) {
                    buf.append('&');
                    buf.append(entityName);
                    buf.append(';');
                }
            } else {
                buf.append('&');
                buf.append(entityName);
                buf.append(';');
            }
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
    for (int i = firstAmp; i < len;) {
        char c = string.charAt(i++);
        if (c == '&') {
            int nextIdx = i;
            int semiColonIdx = string.indexOf(';', nextIdx);
            if (semiColonIdx == -1) {
                writer.write(c);
                continue;
            }
            String entityContent = string.substring(nextIdx, semiColonIdx);
            i = semiColonIdx + 1;
            int entityValue = -1;
            int entityContentLen = entityContent.length();
            if (entityContentLen > 1 && entityContent.charAt(0) == '#') {
                try {
                    if (entityContent.charAt(1) == 'x') {
                        entityValue = Integer.parseInt(entityContent.substring(2), 16);
                    } else {
                        entityValue = Integer.parseInt(entityContent.substring(1), 10);
                    }
                } catch (NumberFormatException e) {
                }
            }
            if (entityValue == -1) {
                writer.write('&');
                writer.write(entityContent);
                writer.write(';');
            } else {
                writer.write(entityValue);
            }
        } else {
            writer.write(c);
        }
    }
}