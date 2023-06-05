## Fixed Function 1
//-----------------------------------------------------------------------
public static LocalDate fromCalendarFields(Calendar calendar) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    int yearOfEra = calendar.get(Calendar.YEAR);
    int monthOfYear = calendar.get(Calendar.MONTH) + 1;
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    return LocalDate.of(yearOfEra, monthOfYear, dayOfMonth);
}

## Fixed Function 2
public static LocalDate fromDateFields(Date date) {
    if (date == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    int year = date.getYear() + 1900;
    int month = date.getMonth() + 1;
    int day = date.getDate();
    return LocalDate.of(year, month, day);
}

## Fixed Function 3
//-----------------------------------------------------------------------
public static LocalDateTime fromCalendarFields(Calendar calendar) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    int millisecond = calendar.get(Calendar.MILLISECOND);
    return LocalDateTime.of(year, month, day, hour, minute, second, millisecond);
}

## Fixed Function 4
public static LocalDateTime fromDateFields(Date date) {
    if (date == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    int year = date.getYear() + 1900;
    int month = date.getMonth() + 1;
    int day = date.getDate();
    int hour = date.getHours();
    int minute = date.getMinutes();
    int second = date.getSeconds();
    int millisecond = date.getTime() % 1000;
    return LocalDateTime.of(year, month, day, hour, minute, second, millisecond);
}