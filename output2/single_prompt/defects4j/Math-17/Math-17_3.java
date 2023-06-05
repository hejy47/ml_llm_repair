## Fixed Function 1
public Dfp multiply(final int x) {
    Dfp num = new Dfp(this);

    // Handle zero multiplication
    if (num.sign == DfpConstants.INFINITE && num.isNaN()) {
        return num;
    }

    num.sign *= x;
    num.exp += 1;
    num = num.round();

    return num;
}