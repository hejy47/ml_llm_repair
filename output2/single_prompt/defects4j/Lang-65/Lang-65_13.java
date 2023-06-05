## Fixed Function 1
//-----------------------------------------------------------------------
private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }
    // ----------------- Fix for LANG-59 ---------------------- START ---------------
    // see http://issues.apache.org/jira/browse/LANG-59
    //
    // Manually truncate milliseconds, seconds and minutes, rather than using
    // Calendar methods.
    long time = val.getTimeInMillis();
    boolean roundUp = false;
    switch (field) {
        case Calendar.MILLISECOND:
            time = time / 1000 * 1000;
            break;
        case Calendar.SECOND:
            time = time / 60000 * 60000;
            break;
        case Calendar.MINUTE:
            time = time / 3600000 * 3600000;
            break;
        case DateUtils.SEMI_MONTH:
            time = time / DateUtils.MILLIS_PER_DAY * DateUtils.MILLIS_PER_DAY;
            val.setTimeInMillis(time);
            if (val.get(Calendar.DATE) > 15) {
                val.add(Calendar.DATE, -(val.get(Calendar.DATE) - 15));
            } else {
                val.add(Calendar.DATE, -val.get(Calendar.DATE));
            }
            return;
    }
    val.setTimeInMillis(time);
    // ----------------- Fix for LANG-59 ----------------------- END ----------------
    for (int[] fields : fields) {
        for (int f : fields) {
            if (f == field) {
                int offset = 0;
                if (round && (f == Calendar.HOUR_OF_DAY || f == Calendar.HOUR)) {
                    offset = val.get(f);
                    if (offset >= 12) {
                        roundUp = true;
                    }
                    if (offset >= 13) {
                        offset -= 12;
                    }
                    if (offset == 0) {
                        offset = 12;
                    }
                } else {
                    offset = val.get(f);
                    roundUp = offset > val.getActualMaximum(f) / 2;
                }
                if (offset == 0) { // i.e. DST pushed us from 12 to 1
                    offset = 1;
                }
                val.set(f, offset);
                if (roundUp) {
                    val.add(fields[0], 1);
                }
                return;
            }
        }
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}