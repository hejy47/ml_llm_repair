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
        int count = 0;  // to keep track of the number of properties added
        for (ObjectType current = this; current != null && !current.isNativeObjectType() && count <= MAX_PRETTY_PRINTED_PROPERTIES; current = current.getImplicitPrototype()) {
            for (String property : current.getOwnPropertyNames()) {
                if (!propertyNames.contains(property)) {  // add only unique properties
                    propertyNames.add(property);
                    count++;
                    if (count > MAX_PRETTY_PRINTED_PROPERTIES) {
                        break;
                    }
                }
            }
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
            i++;
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