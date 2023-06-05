## Fixed Function 1
public static LocalDate fromCalendarFields(Calendar calendar) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    int yearOfEra = calendar.get(Calendar.YEAR);
    int era = (calendar.get(Calendar.ERA) == GregorianCalendar.AD) ? 1 : 0;
    return LocalDate.of(yearOfEra, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)).withEra(era);
}

## Fixed Function 2
@SuppressWarnings("deprecation")
public static LocalDate fromDateFields(Date date) {
    if (date == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    LocalDate localDate = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
    if(date.getYear() < 0) {
        localDate = localDate.withEra(Era.BCE);
    }
    return localDate;
}

## Fixed Function 3
public static LocalDateTime fromCalendarFields(Calendar calendar) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    int yearOfEra = calendar.get(Calendar.YEAR);
    int era = (calendar.get(Calendar.ERA) == GregorianCalendar.AD) ? 1 : 0;
    return LocalDateTime.of(yearOfEra, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND)).withEra(era);
}

## Fixed Function 4
@SuppressWarnings("deprecation")
public static LocalDateTime fromDateFields(Date date) {
    if (date == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    LocalDate localDate = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
    if(date.getYear() < 0) {
        localDate = localDate.withEra(Era.BCE);
    }
    return LocalDateTime.of(localDate, LocalTime.of(date.getHours(), date.getMinutes(), date.getSeconds(), (((int) (date.getTime() % 1000)) + 1000) % 1000));
}