## Fixed Function 1
private boolean toStringEquals(Matcher m, Object arg) {
    return StringDescription.asString(m).equals(arg.toString());
}