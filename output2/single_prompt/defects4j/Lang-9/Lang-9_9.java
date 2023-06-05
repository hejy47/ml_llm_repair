private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);
    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();
    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();
    Matcher patternMatcher = formatPattern.matcher(pattern);
    if (!patternMatcher.find()) { // Fixed here, using find() instead of lookingAt() to allow matching at any position in the string
        throw new IllegalArgumentException("Invalid pattern");
    }
    currentFormatField = patternMatcher.group();
    Strategy currentStrategy = getStrategy(currentFormatField);
    do {
        patternMatcher.region(patternMatcher.end(), patternMatcher.regionEnd());
        if (!patternMatcher.find()) { // Fixed here, using find() instead of lookingAt() to allow matching at any position in the string
            nextStrategy = null;
            break;
        }
        String nextFormatField = patternMatcher.group();
        nextStrategy = getStrategy(nextFormatField);
        if (currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        currentFormatField = nextFormatField;
        currentStrategy = nextStrategy;
    } while (nextStrategy != null);
    if (currentStrategy.addRegex(this, regex)) {
        collector.add(currentStrategy);
    }
    currentFormatField = null;
    strategies = collector.toArray(new Strategy[collector.size()]);
    parsePattern = Pattern.compile(regex.toString());
}