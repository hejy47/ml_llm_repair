## Fixed Function 1
//-----------------------------------------------------------------------
public static LocalDate fromCalendarFields(Calendar calendar) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    int yearOfEra = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    return LocalDate.of(yearOfEra, month + 1, dayOfMonth);
}

## Fixed Function 2
public static LocalDate fromDateFields(Date date) {
    if (date == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    Instant instant = date.toInstant();
    ZoneId zone = ZoneId.systemDefault();
    LocalDate localDate = instant.atZone(zone).toLocalDate();
    return localDate;
}

## Fixed Function 3
//-----------------------------------------------------------------------
public static LocalDateTime fromCalendarFields(Calendar calendar) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    int millisecond = calendar.get(Calendar.MILLISECOND);
    return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, millisecond);
}

## Fixed Function 4
public static LocalDateTime fromDateFields(Date date) {
    if (date == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    Instant instant = date.toInstant();
    ZoneId zone = ZoneId.systemDefault();
    LocalDateTime localDateTime = instant.atZone(zone).toLocalDateTime();
    return localDateTime;
}