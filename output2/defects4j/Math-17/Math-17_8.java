## Fixed Function 1
public Dfp multiply(final int x) {
    Dfp result = newInstance();
    for (int i = 0; i < size(); i++) {
        result.data[i] = data[i].multiply(x);
    }
    result = result.normalize();
    result.exp = exp;
    return result;
}