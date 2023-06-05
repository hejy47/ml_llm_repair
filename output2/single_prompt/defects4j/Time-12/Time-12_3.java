## Fixed Function 1
//-----------------------------------------------------------------------
public static LocalDate fromCalendarFields(Calendar calendar) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    int yearOfEra = calendar.get(Calendar.YEAR);
    int monthValue = calendar.get(Calendar.MONTH) + 1; // month is 0-based in the Calendar API
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    return LocalDate.of(yearOfEra, monthValue, dayOfMonth);
}

## Fixed Function 2
public static LocalDate fromDateFields(Date date) {
    if (date == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    LocalDate localDate = new LocalDate(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
    if (localDate.getYear() <= 0) {
        // handle years in era BC
        localDate = localDate.withYear(1 - localDate.getYear());
    }
    return localDate;
}

## Fixed Function 3
//-----------------------------------------------------------------------
public static LocalDateTime fromCalendarFields(Calendar calendar) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    int yearOfEra = calendar.get(Calendar.YEAR);
    int monthValue = calendar.get(Calendar.MONTH) + 1; // month is 0-based in the Calendar API
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    int millisecond = calendar.get(Calendar.MILLISECOND);
    return LocalDateTime.of(yearOfEra, monthValue, dayOfMonth, hour, minute, second, millisecond * 1000000);
}

## Fixed Function 4
public static LocalDateTime fromDateFields(Date date) {
    if (date == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    LocalDateTime localDateTime = new LocalDateTime(date.getYear() + 1900, date.getMonth() + 1, date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds(), (((int) (date.getTime() % 1000)) + 1000) % 1000 * 1000000);
    if (localDateTime.getYear() <= 0) {
        // handle years in era BC
        localDateTime = localDateTime.withYear(1 - localDateTime.getYear());
    }
    return localDateTime;
}