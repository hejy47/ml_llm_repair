## Fixed Function 1
public long adjustOffset(long instant, boolean earlierOrLater) {
    long newInstant = instant;
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
    long newLocal;
    boolean isCurrentOffsetEarlier = (convertLocalToUTC(local, false) == instant);
    if (isCurrentOffsetEarlier && !earlierOrLater) {
        newLocal = convertUTCToLocal(instantBefore);
        newInstant = convertLocalToUTC(newLocal, false);
    } else if (!isCurrentOffsetEarlier && earlierOrLater) {
        newLocal = convertUTCToLocal(instantAfter);
        newInstant = convertLocalToUTC(newLocal, false);
    }
    return newInstant;
    // calculate result
    // currently in later offset
    // currently in earlier offset
}