## Fixed Function 1
@Override
public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (o == null || getClass() != o.getClass()) {
        return false;
    }
    // Cast object to class type before comparing methods
    MethodCall other = (MethodCall) o;
    return method.equals(other.method);
}

## Fixed Function 2
@Override
public int hashCode() {
    return Objects.hash(method);
}