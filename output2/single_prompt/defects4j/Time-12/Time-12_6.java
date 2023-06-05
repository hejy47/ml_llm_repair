## Fixed Function 1
//-----------------------------------------------------------------------
public static LocalDate fromCalendarFields(Calendar calendar) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    int yearOfEra = calendar.get(Calendar.YEAR);
    int era = calendar.get(Calendar.ERA);
    if (era == GregorianCalendar.BC) {
        yearOfEra = 1 - yearOfEra;
    }
    return new LocalDate(yearOfEra, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
}

## Fixed Function 2
@SuppressWarnings("deprecation")
public static LocalDate fromDateFields(Date date) {
    if (date == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    // handle years in era BC
    int year = date.getYear() + 1900;
    if (year <= 0) { // if year is before 1 AD, adjust for the year 0
        year = 1 - year;
    }
    return new LocalDate(year, date.getMonth() + 1, date.getDate());
}

## Fixed Function 3
//-----------------------------------------------------------------------
public static LocalDateTime fromCalendarFields(Calendar calendar) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    int yearOfEra = calendar.get(Calendar.YEAR);
    int era = calendar.get(Calendar.ERA);
    if (era == GregorianCalendar.BC) {
        yearOfEra = 1 - yearOfEra;
    }
    return new LocalDateTime(yearOfEra, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
}

## Fixed Function 4
@SuppressWarnings("deprecation")
public static LocalDateTime fromDateFields(Date date) {
    if (date == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    // handle years in era BC
    int year = date.getYear() + 1900;
    if (year <= 0) { // if year is before 1 AD, adjust for the year 0
        year = 1 - year;
    }
    return new LocalDateTime(year, date.getMonth() + 1, date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds(), (((int) (date.getTime() % 1000)) + 1000) % 1000);
}