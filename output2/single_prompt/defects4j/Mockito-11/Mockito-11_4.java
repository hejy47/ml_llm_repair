## Fixed Function 1
@Override
public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
        return false;
    }

    // Cast the object to the same class
    MockitoMethod other = (MockitoMethod) o;

    // Check if method objects are equal
    return Objects.equals(method, other.method);
}

## Fixed Function 2
@Override
public int hashCode() {
    return Objects.hash(method);
}