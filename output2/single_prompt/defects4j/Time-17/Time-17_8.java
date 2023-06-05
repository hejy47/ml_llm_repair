## Fixed Function 1
public long adjustOffset(long instant, boolean earlierOrLater) {
    long offsetBefore = getOffset(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long offsetAfter = getOffset(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    if (offsetBefore == offsetAfter) {
        // no transition
        return instant;
    }
    // Calculate local time from input instant
    long localTime = instant + offsetBefore;
    // Calculate local time after the transition
    long transitionTime = convertLocalToUTC(localTime, false, earlierOrLater ? offsetAfter : offsetBefore);
    // return the corresponding UTC time
    return transitionTime - (earlierOrLater ? offsetAfter : offsetBefore);
}