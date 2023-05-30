## Fixed Function 1
public OngoingInjecter filterCandidate(final Collection<Object> mocks, final Field field, final Object fieldInstance) {
    if (mocks.size() == 1) {
        final Object matchingMock = mocks.iterator().next();
        return new OngoingInjecter() {

            public Object thenInject() {
                try {
                    if (!new BeanPropertySetter(fieldInstance, field).set(matchingMock)) {
                        new FieldSetter(fieldInstance, field).set(matchingMock);
                    }
                } catch (RuntimeException e) {
                    new Reporter().cannotInjectDependency(field, matchingMock, e);
                }
                return matchingMock;
            }
        };
    } else if (mocks.size() > 1) {
        return new OngoingInjecter() {

            public Object thenInject() {
                return null;
            }
        };
    }
    return null;
}

## Fixed Function 2
OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance);

## Fixed Function 3
public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
    List<Object> mockNameMatches = new ArrayList<Object>();
    for (Object mock : mocks) {
        if (field.getName().equals(mockUtil.getMockName(mock).toString())) {
            mockNameMatches.add(mock);
        }
    }
    if (mockNameMatches.size() > 0) {
        return next.filterCandidate(mockNameMatches, field, fieldInstance);
    } else {
        return next.filterCandidate(mocks, field, fieldInstance);
    }
}

## Fixed Function 4
public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
    List<Object> mockTypeMatches = new ArrayList<Object>();
    for (Object mock : mocks) {
        if (field.getType().isAssignableFrom(mock.getClass())) {
            mockTypeMatches.add(mock);
        }
    }
    if (mockTypeMatches.size() > 0) {
        return next.filterCandidate(mockTypeMatches, field, fieldInstance);
    } else {
        return next.filterCandidate(mocks, field, fieldInstance);
    }
}

## Fixed Function 5
private boolean injectMockCandidatesOnFields(Set<Object> mocks, Object instance, boolean injectionOccurred, List<Field> orderedInstanceFields) {
    for (Iterator<Field> it = orderedInstanceFields.iterator(); it.hasNext(); ) {
        Field field = it.next();
        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, instance);
        if (injecter != null) {
            Object injected = injecter.thenInject();
            if (injected != null) {
                injectionOccurred |= true;
                mocks.remove(injected);
                it.remove();
            }
        }
    }
    return injectionOccurred;
}