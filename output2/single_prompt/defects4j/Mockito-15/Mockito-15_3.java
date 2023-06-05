## Fixed Function 1
public OngoingInjecter filterCandidate(final Collection<Object> mocks, final Field field, final Object fieldInstance) {
    if (mocks.size() == 1) {
        final Object matchingMock = mocks.iterator().next();
        return new OngoingInjecter() {

            @Override
            public boolean thenInject() {
                try {
                    new FieldSetter(fieldInstance, field).set(matchingMock);
                    return true;
                } catch (Exception e) {
                    throw new MockitoException("Problems injecting dependency in " + field.getName(), e);
                }
            }
        };
    }
    return new OngoingInjecter() {

        @Override
        public boolean thenInject() {
            return false;
        }
    };
}

## Fixed Function 2
public boolean thenInject() {
    try {
        new FieldSetter(fieldInstance, field).set(matchingMock);
        return true;
    } catch (Exception e) {
        throw new MockitoException("Problems injecting dependency in " + field.getName(), e);
    }
}