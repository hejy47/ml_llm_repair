## Fixed Function 1
public static LocalDate fromCalendarFields(Calendar calendar) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    int yearOfEra = calendar.get(Calendar.YEAR);
    int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
    return LocalDate.ofYearDay(yearOfEra, dayOfYear);
}

## Fixed Function 2
@SuppressWarnings("deprecation")
public static LocalDate fromDateFields(Date date) {
    if (date == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    // handle years in era BC
    int year = date.getYear() + 1900;
    int dayOfYear = date.getMonth() + 1;
    return LocalDate.ofYearDay(year, dayOfYear);
}

## Fixed Function 3
public static LocalDateTime fromCalendarFields(Calendar calendar) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    int yearOfEra = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    int millisecond = calendar.get(Calendar.MILLISECOND);
    
    return LocalDateTime.of(yearOfEra, month, dayOfMonth, hour, minute, second, millisecond);
}

## Fixed Function 4
public static LocalDateTime fromDateFields(Date date) {
    if (date == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    // handle years in era BC
    int year = date.getYear() + 1900;
    int month = date.getMonth() + 1;
    int dayOfMonth = date.getDate();
    int hour = date.getHours();
    int minute = date.getMinutes();
    int second = date.getSeconds();
    int millisecond = date.getTime() % 1000;
    
    return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, millisecond);
}