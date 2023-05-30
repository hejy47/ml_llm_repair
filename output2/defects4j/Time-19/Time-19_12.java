public int getOffsetFromLocal(long instantLocal) {
    final int offsetLocal = getOffset(instantLocal);
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);
    if (offsetLocal != offsetAdjusted) {
        if ((offsetLocal - offsetAdjusted) < 0) {
            // fix: add the offset difference to instantLocal instead of subtracting it from instantAdjusted
            long nextLocal = nextTransition(instantLocal + (offsetLocal - offsetAdjusted));
            long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);
            if (nextLocal != nextAdjusted) {
                return offsetLocal;
            }
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