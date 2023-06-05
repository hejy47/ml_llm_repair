## Fixed Function 1
public OngoingInjecter filterCandidate(final Collection<Object> mocks, final Field field, final Object fieldInstance) {
    if (mocks.size() == 1) {
        final Object matchingMock = mocks.iterator().next();
        return new OngoingInjecter() {

            public boolean thenInject() {
                try {
                    if (field.isAccessible() || Modifier.isPublic(field.getModifiers())) {
                        field.set(fieldInstance, matchingMock);
                    } else {
                        field.setAccessible(true);
                        try {
                            field.set(fieldInstance, matchingMock);
                        } finally {
                            field.setAccessible(false);
                        }
                    }
                } catch (Exception e) {
                    throw new MockitoException("Problems injecting dependency in " + field.getName(), e);
                }
                return true;
            }
        };
    }
    return new OngoingInjecter() {

        public boolean thenInject() {
            return false;
        }
    };
}

## Fixed Function 2
public boolean thenInject() {
    try {
        if (field.isAccessible() || Modifier.isPublic(field.getModifiers())) {
            field.set(fieldInstance, matchingMock);
        } else {
            field.setAccessible(true);
            try {
                field.set(fieldInstance, matchingMock);
            } finally {
                field.setAccessible(false);
            }
        }
    } catch (Exception e) {
        throw new MockitoException("Problems injecting dependency in " + field.getName(), e);
    }
    return true;
}