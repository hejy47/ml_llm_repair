private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);
    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();
    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();
    Matcher patternMatcher = formatPattern.matcher(pattern);
    if (!patternMatcher.find()) {  // Use find() instead of lookingAt() to extract the match
        throw new IllegalArgumentException("Invalid pattern");
    }
    currentFormatField = patternMatcher.group();
    Strategy currentStrategy = getStrategy(currentFormatField);
    while (patternMatcher.find()) {  // Use find() to move to the next match
        String nextFormatField = patternMatcher.group();
        nextStrategy = getStrategy(nextFormatField);
        if (currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        currentFormatField = nextFormatField;
        currentStrategy = nextStrategy;
    }
    if (currentStrategy.addRegex(this, regex)) {
        collector.add(currentStrategy);
    }
    currentFormatField = null;
    strategies = collector.toArray(new Strategy[collector.size()]);
    parsePattern = Pattern.compile(regex.toString());
}