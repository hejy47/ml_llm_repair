## Fixed Function 1
public void describeTo(Description description) {
    description.appendText("same(");
    appendQuoting(description);
    description.appendValue(wanted);
    appendQuoting(description);
    description.appendText(")");
}