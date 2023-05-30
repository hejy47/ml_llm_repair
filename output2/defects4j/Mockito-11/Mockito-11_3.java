## Fixed Function 1
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MyClass myClass = (MyClass) o;

    return method != null ? method.equals(myClass.method) : myClass.method == null;
}

## Fixed Function 2
@Override
public int hashCode() {
    return Objects.hash(method);
}