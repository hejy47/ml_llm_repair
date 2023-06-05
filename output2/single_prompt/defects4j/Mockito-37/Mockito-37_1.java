## Fixed Function 1
public void validate(Answer<?> answer, Invocation invocation) {
    if (answer instanceof ThrowsException) {
        validateException((ThrowsException) answer, invocation);
    }
    else if (answer instanceof Returns) {
        validateReturnValue((Returns) answer, invocation);
    }
    else if (answer instanceof DoesNothing) {
        validateDoNothing((DoesNothing) answer, invocation);
    }
} 

Note: Added "else" before the second and third if statements to ensure that only one validation is executed per invocation.