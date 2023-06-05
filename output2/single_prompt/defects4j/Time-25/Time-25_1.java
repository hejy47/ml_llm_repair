## Fixed Function 1
public int getOffsetFromLocal(long instantLocal) {
    final int offsetLocal = getOffset(instantLocal);
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);
    if (offsetLocal != offsetAdjusted) {
        if ((offsetLocal - offsetAdjusted) < 0) {
            long nextLocal = nextTransition(instantAdjusted);
            long nextAdjusted = nextTransition(instantAdjusted - offsetAdjusted);
            if (nextLocal != nextAdjusted) {
                return offsetLocal;
            }
        }
    }
    return offsetAdjusted;
}