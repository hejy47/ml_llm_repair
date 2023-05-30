private static void modify(Calendar val, int field, boolean round) {
    if (val.getTimeInMillis() > 818075684000000L) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }
    if (field == Calendar.MILLISECOND) {
        return;
    }
    // Manually truncate milliseconds, seconds and minutes, rather than using
    // Calendar methods.
    Date date = val.getTime();
    long time = date.getTime();
    boolean done = false;
    // truncate milliseconds
    if (round && val.get(Calendar.MILLISECOND) >= 500) {
        time = time + 1000L;
    }
    int millisecs = 0;
    // truncate seconds
    if (field == Calendar.SECOND) {
        millisecs = val.get(Calendar.MILLISECOND);
        if (round && millisecs >= 500) {
            time = time + 1000L;
        }
    }
    val.setTimeInMillis(time);
    int seconds = 0;
    // truncate minutes
    if (field == Calendar.MINUTE) {
        millisecs = val.get(Calendar.MILLISECOND);
        seconds = val.get(Calendar.SECOND);
        if (round && seconds >= 30) {
            time = time + 60L * 1000L;
        }
        val.setTimeInMillis(time);
    }
    done = (field == Calendar.SECOND);
    boolean roundUp = false;
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                if (round && !done) {
                    if (field == DateUtils.SEMI_MONTH) {
                        int hourOfDay = val.get(Calendar.HOUR_OF_DAY);
                        if (hourOfDay >= 12) {
                            val.add(Calendar.DATE, 1);
                        }
                    } else if (field == Calendar.HOUR_OF_DAY) {
                        int minute = val.get(Calendar.MINUTE);
                        if (minute >= 30) {
                            val.add(Calendar.HOUR_OF_DAY, 1);
                        }
                    } else {
                        int offset = 60;
                        if (field == Calendar.DAY_OF_MONTH) {
                            offset = 24;
                        } else if (field == Calendar.MONTH) {
                            offset = 12;
                        }
                        int value = val.get(field);
                        if (value >= offset / 2) {
                            val.add(fields[i][0], 1);
                        }
                    }
                }
                return;
            }
        }
        int min = val.getActualMinimum(fields[i][0]);
        int max = val.getActualMaximum(fields[i][0]);
        int offset = 0;
        boolean roundUp = false;
        if (val.get(fields[i][0]) < min + (max - min) / 2) {
            offset = val.get(fields[i][0]) - min;
        } else {
            offset = max - val.get(fields[i][0]);
            roundUp = true;
        }
        if (offset != 0) {
            val.set(fields[i][0], val.get(fields[i][0]) - offset);
            if (fields[i][0] == Calendar.MONTH) {
                // If we're adjusting the month, we need to re-adjust the day
                int domMax = val.getActualMaximum(Calendar.DAY_OF_MONTH);
                int dom = val.get(Calendar.DAY_OF_MONTH);
                if (dom > domMax) {
                    val.set(Calendar.DAY_OF_MONTH, domMax);
                }
            }
            if (roundUp) {
                val.add(fields[i][0], 1);
            }
        }
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}