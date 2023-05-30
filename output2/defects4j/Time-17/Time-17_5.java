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
    return convertLocalToUTC(local, false, earlierOrLater ? instantAfter - 1 : instantBefore + 1);
    // calculate result
    // currently in later offset
    // currently in earlier offset
}

Explanation: The previous version of the function was not handling the overlap of time zones correctly. Adding or subtracting a small value (1 millisecond) to the boundaries of the overlap range fixes the problem.