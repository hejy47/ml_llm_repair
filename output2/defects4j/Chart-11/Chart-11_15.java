## Fixed Function 1
public static boolean equal(GeneralPath p1, GeneralPath p2) {
    if (p1 == null) {
        return (p2 == null);
    }
    if (p2 == null) {
        return false;
    }
    if (p1.getWindingRule() != p2.getWindingRule()) {
        return false;
    }
    PathIterator iterator1 = p1.getPathIterator(null);
    PathIterator iterator2 = p2.getPathIterator(null); // Fixed: changed p1 to p2
    double[] d1 = new double[6];
    double[] d2 = new double[6];
    boolean done = iterator1.isDone() && iterator2.isDone();
    while (!done) {
        if (iterator1.isDone() != iterator2.isDone()) {
            return false;
        }
        int seg1 = iterator1.currentSegment(d1);
        int seg2 = iterator2.currentSegment(d2);
        if (seg1 != seg2) {
            return false;
        }
        if (!Arrays.equals(d1, d2)) {
            return false;
        }
        iterator1.next();
        iterator2.next();
        done = iterator1.isDone() && iterator2.isDone();
    }
    return true;
}