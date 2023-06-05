## Fixed Function 1
// Java and JavaScript
//--------------------------------------------------------------------------
public static String escapeJava(String str) {
    return escapeJavaStyleString(str, true); // escape single quotes as well
}

## Fixed Function 2
public static void escapeJava(Writer out, String str) throws IOException {
    escapeJavaStyleString(out, str, true); // escape single quotes as well
}

## Fixed Function 3
public static String escapeJavaScript(String str) {
    return escapeJavaScriptStyleString(str, false); // do not escape single quotes
}

## Fixed Function 4
public static void escapeJavaScript(Writer out, String str) throws IOException {
    escapeJavaScriptStyleString(out, str, false); // do not escape single quotes
}

## Fixed Function 5
private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes) {
    if (str == null) {
        return null;
    }
    try {
        StringWriter writer = new StringWriter(str.length() * 2);
        escapeJavaStyleString(writer, str, false, escapeSingleQuotes); // fix call to include escapeSingleQuotes parameter
        return writer.toString();
    } catch (IOException ioe) {
        // this should never ever happen while writing to a StringWriter
        ioe.printStackTrace();
        return null;
    }
}

## Fixed Function 6
private static void escapeJavaStyleString(Writer out, String str, boolean escapeForwardSlash, boolean escapeSingleQuote) throws IOException {
    // fix function signature to include escapeForwardSlash parameter
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (str == null) {
        return;
    }
    int sz;
    sz = str.length();
    for (int i = 0; i < sz; i++) {
        char ch = str.charAt(i);
        // handle unicode
        if (ch > 0xfff) {
            out.write("\\u" + hex(ch));
        } else if (ch > 0xff) {
            out.write("\\u0" + hex(ch));
        } else if (ch > 0x7f) {
            out.write("\\u00" + hex(ch));
        } else if (ch < 32) {
            switch(ch) {
                case '\b':
                    out.write('\\');
                    out.write('b');
                    break;
                case '\n':
                    out.write('\\');
                    out.write('n');
                    break;
                case '\t':
                    out.write('\\');
                    out.write('t');
                    break;
                case '\f':
                    out.write('\\');
                    out.write('f');
                    break;
                case '\r':
                    out.write('\\');
                    out.write('r');
                    break;
                case '/':
                    if (escapeForwardSlash) { // fix to check for escapeForwardSlash parameter
                        out.write('\\');
                    }
                    out.write('/');
                    break;
                default:
                    if (ch > 0xf) {
                        out.write("\\u00" + hex(ch));
                    } else {
                        out.write("\\u000" + hex(ch));
                    }
                    break;
            }
        } else {
            switch(ch) {
                case '\'':
                    if (escapeSingleQuote) {
                        out.write('\\');
                    }
                    out.write('\'');
                    break;
                case '"':
                    out.write('\\');
                    out.write('"');
                    break;
                case '\\':
                    out.write('\\');
                    out.write('\\');
                    break;
                default:
                    out.write(ch);
                    break;
            }
        }
    }
}

// Additional note: there is a similar buggy function in the Lang project called escapeJsonString that needs the same fix as escapeJavaStyleString, except with the addition of an escapeForwardSlash parameter.