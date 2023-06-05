## Fixed Function 1
public void describeTo(Description description) {
    description.appendText("same(\"");
    description.appendText(wanted.toString());
    description.appendText("\")");
}