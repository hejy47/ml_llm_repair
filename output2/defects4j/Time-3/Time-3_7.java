## Fixed Function 1
public void add(DurationFieldType type, int amount) {
    if (type == null) {
        throw new IllegalArgumentException("Field must not be null");
    }
    setMillis(type.getField(getChronology()).add(getMillis(), amount, getMillis()));
}

## Fixed Function 2
public void addYears(final int years) {
    setMillis(getChronology().years().add(getMillis(), years, getMillis()));
}

## Fixed Function 3
public void addWeekyears(final int weekyears) {
    setMillis(getChronology().weekyears().add(getMillis(), weekyears, getMillis()));
}

## Fixed Function 4
public void addMonths(final int months) {
    setMillis(getChronology().months().add(getMillis(), months, getMillis()));
}

## Fixed Function 5
public void addWeeks(final int weeks) {
    setMillis(getChronology().weeks().add(getMillis(), weeks, getMillis()));
}

## Fixed Function 6
public void addDays(final int days) {
    setMillis(getChronology().days().add(getMillis(), days, getMillis()));
}

## Fixed Function 7
public void addHours(final int hours) {
    setMillis(getChronology().hours().add(getMillis(), hours, getMillis()));
}

## Fixed Function 8
public void addMinutes(final int minutes) {
    setMillis(getChronology().minutes().add(getMillis(), minutes, getMillis()));
}

## Fixed Function 9
public void addSeconds(final int seconds) {
    setMillis(getChronology().seconds().add(getMillis(), seconds, getMillis()));
}

## Fixed Function 10
public void addMillis(final int millis) {
    setMillis(getChronology().millis().add(getMillis(), millis, getMillis()));
}