## Fixed Function 1
private void updateBounds(TimePeriod period, int index) {
    long start = period.getStart().getTime();
    long end = period.getEnd().getTime();
    long middle = start + ((end - start) / 2);
    if (this.minStartIndex >= 0) {
        long minStart = getDataItem(this.minStartIndex).getPeriod().getStart().getTime();
        if (start < minStart) {
            this.minStartIndex = index;
        }
    } else {
        this.minStartIndex = index;
    }
    if (this.maxStartIndex >= 0) {
        long maxStart = getDataItem(this.maxStartIndex).getPeriod().getPeriodMiddle().getTime();
        if (start > maxStart) {
            this.maxStartIndex = index;
        }
    } else {
        this.maxStartIndex = index;
    }
    if (this.minMiddleIndex >= 0) {
        long minMiddle = getDataItem(this.minMiddleIndex).getPeriod().getPeriodMiddle().getTime();
        if (middle < minMiddle) {
            this.minMiddleIndex = index;
        }
    } else {
        this.minMiddleIndex = index;
    }
    if (this.maxMiddleIndex >= 0) {
        long maxMiddle = getDataItem(this.maxMiddleIndex).getPeriod().getPeriodMiddle().getTime();
        if (middle > maxMiddle) {
            this.maxMiddleIndex = index;
        }
    } else {
        this.maxMiddleIndex = index;
    }
    if (this.minEndIndex >= 0) {
        long minEnd = getDataItem(this.minEndIndex).getPeriod().getEnd().getTime();
        if (end < minEnd) {
            this.minEndIndex = index;
        }
    } else {
        this.minEndIndex = index;
    }
    if (this.maxEndIndex >= 0) {
        long maxEnd = getDataItem(this.maxEndIndex).getPeriod().getEnd().getTime();
        if (end > maxEnd) {
            this.maxEndIndex = index;
        }
    } else {
        this.maxEndIndex = index;
    }
}