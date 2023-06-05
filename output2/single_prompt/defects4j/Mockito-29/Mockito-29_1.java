## Fixed Function 1
public void describeTo(Description description) {
    description.appendText("same(\"");
    appendQuoting(description);
    description.appendText(wanted.toString());
    appendQuoting(description);
    description.appendText("\")");
} 

(Note: This fix assumes that the method appendQuoting correctly appends a quotation mark before and after a string.)