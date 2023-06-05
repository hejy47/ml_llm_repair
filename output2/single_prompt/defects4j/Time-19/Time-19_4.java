public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);
    // if the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // we need to ensure that time is always after the DST gap
        // this happens naturally for positive offsets, but not for negative
        if ((offsetLocal - offsetAdjusted) < 0) {
            // if we just return offsetAdjusted then the time is pushed
            // back before the transition, whereas it should be
            // on or after the transition
            long nextLocal = nextTransition(instantAdjusted);
            long nextAdjusted = nextTransition(instantLocal - offsetLocal + 1);
            if (nextLocal != nextAdjusted) {
                return offsetLocal;
            }
        } else {
            // offsetLocal > offsetAdjusted
            // we need to ensure that time is always before the DST overlap
            // this happens naturally for negative offsets, but not for positive
            // the check below does the latter
            // long prevLocal = previousTransition(instantAdjusted);
            // long prevAdjusted = previousTransition(instantLocal-offsetLocal+1);
            // if (prevLocal != prevAdjusted) {
            //     return offsetLocal;
            // }
        }
    } else if (offsetLocal > 0) {
        long prev = previousTransition(instantAdjusted);
        if (prev < instantAdjusted) {
            int offsetPrev = getOffset(prev);
            int diff = offsetPrev - offsetLocal;
            if (instantAdjusted - prev <= diff) {
                return offsetPrev;
            }
        }
    }
    return offsetAdjusted;
}