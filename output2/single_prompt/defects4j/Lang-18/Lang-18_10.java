// Parse the pattern
//-----------------------------------------------------------------------
protected List<Rule> parsePattern() {
    DateFormatSymbols symbols = new DateFormatSymbols(mLocale);
    List<Rule> rules = new ArrayList<Rule>();
    String[] ERAs = symbols.getEras();
    String[] months = symbols.getMonths();
    String[] shortMonths = symbols.getShortMonths();
    String[] weekdays = symbols.getWeekdays();
    String[] shortWeekdays = symbols.getShortWeekdays();
    String[] AmPmStrings = symbols.getAmPmStrings();
    int length = mPattern.length();
    int[] indexRef = new int[1];
    for (int i = 0; i < length; ) { // Fixed: removed i++
        indexRef[0] = i;
        String token = parseToken(mPattern, indexRef);
        i = indexRef[0];
        int tokenLen = token.length();
        if (tokenLen == 0) {
            break;
        }
        Rule rule;
        char c = token.charAt(0);
        switch(c) {
            case 'G': // Fixed: added missing cases for switch statement
            case 'y':
            case 'M':
            case 'd':
            case 'h':
            case 'H':
            case 'm':
            case 's':
            case 'S':
            case 'E':
            case 'D':
            case 'F':
            case 'w':
            case 'W':
            case 'a':
            case 'k':
            case 'K':
            case 'z':
            case 'Z':
                if (c == '\'' || i <= length) { // Fixed: added condition to check for single quote or end of index
                    i++;
                }
                break;
            case '\'':
                String sub = token.substring(1);
                if (sub.length() == 1) {
                    rule = new CharacterLiteral(sub.charAt(0));
                } else {
                    rule = new StringLiteral(sub);
                }
                rules.add(rule); // Fixed: added missing statement to add rule
                i++;
                continue;
            default:
                throw new IllegalArgumentException("Illegal pattern component: " + token);
        }
        if (rule == null) {
            rule = new ConstantWidth(token);
        }
        rules.add(rule);
    }
    return rules;
}