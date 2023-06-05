## Fixed Function 1
@Override
String toStringHelper(boolean forAnnotations) {
    if (hasReferenceName()) {
        return getReferenceName();
    } else if (prettyPrint) {
        // Don't pretty print recursively.
        prettyPrint = false;
        // Use a tree set so that the properties are sorted.
        Set<String> propertyNames = Sets.newTreeSet();
        ObjectType current = this; // current is initialized outside of the loop
        while (current != null && !current.isNativeObjectType() && propertyNames.size() <= MAX_PRETTY_PRINTED_PROPERTIES) {
            propertyNames.addAll(current.getOwnPropertyNames());
            current = current.getImplicitPrototype(); // current is updated at the end of the loop
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int i = 0;
        for (String property : propertyNames) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(property);
            sb.append(": ");
            sb.append(getPropertyType(property).toString());
            ++i;
            if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
                sb.append(", ...");
                break;
            }
        }
        sb.append("}");
        prettyPrint = true;
        return sb.toString();
    } else {
        return "{...}";
    }
}