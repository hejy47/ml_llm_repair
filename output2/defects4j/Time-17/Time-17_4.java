## Fixed Function 1
public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    if (instantBefore == instantAfter) {
        // not an overlap (less than is a gap, equal is normal case)
        return instant;
    }
    // Determine whether to use the earlier or later offset
    long local = convertUTCToLocal(instant);
    boolean useLaterOffset = local < convertUTCToLocal(instantAfter);
    return convertLocalToUTC(local, false, useLaterOffset ^ earlierOrLater);
}