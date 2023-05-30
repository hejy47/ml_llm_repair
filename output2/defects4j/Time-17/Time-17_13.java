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
    if (earlierOrLater) {
        // currently in later offset
        long local = convertUTCToLocal(instantAfter - 1);
        return convertLocalToUTC(local, false, instantAfter);
    } else {
        // currently in earlier offset
        long local = convertUTCToLocal(instantBefore - 1);
        return convertLocalToUTC(local, false, instantBefore);
    }
}