## Fixed Function 1
public int getOffsetFromLocal(long instantLocal) {
    final long instantAdjusted = instantLocal - getOffset(instantLocal);
    final int offsetAdjusted = getOffset(instantAdjusted);
    if (getOffset(instantLocal) != offsetAdjusted) {
        if ((getOffset(instantLocal) - offsetAdjusted) < 0) {
            long nextLocal = nextTransition(instantAdjusted);
            long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);
            if (nextLocal != nextAdjusted) {
                return getOffset(instantLocal);
            }
        }
    }
    return offsetAdjusted;
}