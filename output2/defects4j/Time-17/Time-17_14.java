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
    // work out range of instants that have duplicate local times
    long local = convertUTCToLocal(instant);
    if (earlierOrLater) {
        local = findEarlierOrLaterLocal(local, instantBefore, instantAfter);
    } else {
        local = findEarlierOrLaterLocal(local, instantAfter, instantBefore);
    }
    return convertLocalToUTC(local, false);
    // calculate result
    // currently in later offset
    // currently in earlier offset
}

private long findEarlierOrLaterLocal(long targetLocal, long lowerInclusive, long upperExclusive) {
    // binary search to refine estimate
    while (lowerInclusive + 1 < upperExclusive) {
        long mid = (lowerInclusive + upperExclusive) / 2;
        long midLocal = convertUTCToLocal(mid);
        if (midLocal == targetLocal) {
            // target found
            return mid;
        } else if (midLocal < targetLocal) {
            lowerInclusive = mid;
        } else {
            upperExclusive = mid;
        }
    }
    return lowerInclusive;
}