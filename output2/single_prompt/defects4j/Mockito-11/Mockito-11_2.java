## Fixed Function 1
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MethodCall that = (MethodCall) o;
    return Objects.equals(method, that.method) &&
            Arrays.equals(arguments, that.arguments);
}

## Fixed Function 2
@Override
public int hashCode() {
    return Objects.hash(method, Arrays.hashCode(arguments));
}