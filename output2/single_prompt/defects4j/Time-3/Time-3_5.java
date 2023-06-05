## Fixed Function 1
public void add(DurationFieldType type, int amount) {
    if (type == null) {
        throw new IllegalArgumentException("Field must not be null");
    }
    setMillis(type.getField(getChronology()).add(getMillis(), amount));
}

## Fixed Function 2
public void addYears(final int years) {
    setMillis(getChronology().years().addTo(getMillis(), years));
}

## Fixed Function 3
public void addWeekyears(final int weekyears) {
    setMillis(getChronology().weekyears().addTo(getMillis(), weekyears));
}

## Fixed Function 4
public void addMonths(final int months) {
    setMillis(getChronology().months().addTo(getMillis(), months));
}

## Fixed Function 5
public void addWeeks(final int weeks) {
    setMillis(getChronology().weeks().addTo(getMillis(), weeks));
}

## Fixed Function 6
public void addDays(final int days) {
    setMillis(getChronology().days().addTo(getMillis(), days));
}

## Fixed Function 7
public void addHours(final int hours) {
    setMillis(getChronology().hours().addTo(getMillis(), hours));
}

## Fixed Function 8
public void addMinutes(final int minutes) {
    setMillis(getChronology().minutes().addTo(getMillis(), minutes));
}

## Fixed Function 9
public void addSeconds(final int seconds) {
    setMillis(getChronology().seconds().addTo(getMillis(), seconds));
}

## Fixed Function 10
public void addMillis(final int millis) {
    setMillis(getChronology().millis().addTo(getMillis(), millis));
}