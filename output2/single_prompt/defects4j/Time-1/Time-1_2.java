## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (durationField == null || !durationField.isSupported()) {
        throw new UnsupportedOperationException("Duration field is not supported");
    }
    return 1;
}

## Fixed Function 2
public Partial(DateTimeFieldType[] types, int[] values, Chronology chronology) {
    super();
    chronology = DateTimeUtils.getChronology(chronology).withUTC();
    iChronology = chronology;
    if (types == null) {
        throw new IllegalArgumentException("Types array must not be null");
    }
    if (values == null) {
        throw new IllegalArgumentException("Values array must not be null");
    }
    if (values.length != types.length) {
        throw new IllegalArgumentException("Values array must be the same length as the types array");
    }
    if (types.length == 0) {
        iTypes = types; 
        iValues = values;
        return;
    }
    for (int i = 0; i < types.length; i++) {
        if (types[i] == null) {
            throw new IllegalArgumentException("Types array must not contain null: index " + i);
        }
    }

    // Sort by unit duration descending
    Arrays.sort(types, new Comparator<DateTimeFieldType>() {
        @Override
        public int compare(DateTimeFieldType fieldType1, DateTimeFieldType fieldType2) {
            DurationFieldType type1 = fieldType1.getDurationType();
            DurationFieldType type2 = fieldType2.getDurationType();
            int compare = type2.compareTo(type1);
            if (compare == 0 && fieldType1.getRangeDurationType() != null && fieldType2.getRangeDurationType() != null) {
                DurationFieldType rangeType1 = fieldType1.getRangeDurationType();
                DurationFieldType rangeType2 = fieldType2.getRangeDurationType();
                compare = rangeType2.compareTo(rangeType1);
            }
            return compare;
        }
    });

    // Check for duplicates and order
    for (int i = 0; i < types.length - 1; i++) {
        DateTimeFieldType current = types[i];
        DateTimeFieldType next = types[i + 1];
        if (current.equals(next)) {
            throw new IllegalArgumentException("Types array must not contain duplicate: " + current.getName() + " and " + next.getName());
        }
        if (current.getDurationType().compareTo(next.getDurationType()) < 0) {
            throw new IllegalArgumentException("Types array must be in order largest-smallest: " + current.getName() + " < " + next.getName());
        } else if (current.getDurationType().compareTo(next.getDurationType()) == 0) {
            if (current.getRangeDurationType() != null) {
                if (next.getRangeDurationType() == null) {
                    throw new IllegalArgumentException("Types array must be in order largest-smallest: " + current.getName() + " < " + next.getName());
                }
                if (current.getRangeDurationType().compareTo(next.getRangeDurationType()) < 0) {
                    throw new IllegalArgumentException("Types array must be in order largest-smallest: " + current.getName() + " < " + next.getName());
                } else if (current.getRangeDurationType().equals(next.getRangeDurationType())) {
                    throw new IllegalArgumentException("Types array must not contain duplicate: " + current.getName() + " and " + next.getName());
                }
            }
        }
    }

    iTypes = types.clone();
    chronology.validate(this, values);
    iValues = values.clone();
}