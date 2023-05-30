## Fixed Function 1
public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy but will work correctly in all non-pathological cases
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
        return convertLocalToUTC(local, false, instantAfter);
    } else {
        return convertLocalToUTC(local, true, instantBefore);
    }
}