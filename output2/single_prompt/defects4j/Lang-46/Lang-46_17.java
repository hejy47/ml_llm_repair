## Fixed Function 1
// Java and JavaScript
//--------------------------------------------------------------------------
public static String escapeJava(String str) {
    return escapeJavaStyleString(str, true); // fixed to escape double quotes as well
}

## Fixed Function 2
public static void escapeJava(Writer out, String str) throws IOException {
    escapeJavaStyleString(out, str, true); // fixed to escape double quotes as well
}

## Fixed Function 3
public static String escapeJavaScript(String str) {
    return escapeJavaScriptStyleString(str, false); // fixed to use correct function name
}

## Fixed Function 4
public static void escapeJavaScript(Writer out, String str) throws IOException {
    escapeJavaScriptStyleString(out, str, false); // fixed to use correct function name
}

## Fixed Function 5
private static String escapeJavaStyleString(String str, boolean escapeQuotes) { // fixed typo in parameter name
    if (str == null) {
        return null;
    }
    try {
        StringWriter writer = new StringWriter(str.length() * 2);
        escapeJavaStyleString(writer, str, escapeQuotes);
        return writer.toString();
    } catch (IOException ioe) {
        // this should never ever happen while writing to a StringWriter
        ioe.printStackTrace();
        return null;
    }
}

## Fixed Function 6
private static void escapeJavaStyleString(Writer out, String str, boolean escapeQuotes) throws IOException { // fixed typo in parameter name
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
                case '"':
                    if (escapeQuotes) {
                        out.write('\\');
                    }
                    out.write('"');
                    break;
                case '\\':
                    out.write('\\');
                    out.write('\\');
                    break;
                case '/':
                    out.write('\\');
                    out.write('/');
                    break;
                default:
                    out.write(ch);
                    break;
            }
        }
    }
}

## Fixed Function 7 - add missing function
private static void escapeJavaScriptStyleString(Writer out, String str, boolean escapeSingleQuote) throws IOException {
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
                case '/':
                    out.write('\\');
                    out.write('/');
                    break;
                default:
                    out.write(ch);
                    break;
            }
        }
    }
}