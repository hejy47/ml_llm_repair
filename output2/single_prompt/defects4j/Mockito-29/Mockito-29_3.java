## Fixed Function 1
public void describeTo(Description description) {
    description.appendText("same(");
    appendQuoting(description, wanted.toString());
    description.appendText(")");
}

private void appendQuoting(Description description, String str) {
    description.appendText("\"").appendText(str).appendText("\"");
}