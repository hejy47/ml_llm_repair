## Fixed Function 1
public void smartNullPointerException(Location location) {
    throw new SmartNullPointerException(join("You have a NullPointerException here:", location, "Because this method was *not* stubbed correctly."));
}

## Fixed Function 2
public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    if (new ObjectMethodsGuru().isToString(method)) {
        return "SmartNull returned by unstubbed " + formatMethodCall() + " method on mock";
    }
    new Reporter().smartNullPointerException(new Location());
    return null;
}