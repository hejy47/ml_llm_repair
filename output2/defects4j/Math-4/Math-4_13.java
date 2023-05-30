## Fixed Function 1
public Vector3D intersection(final SubLine subLine, final boolean includeEndPoints) {
    // compute the intersection on infinite line
    Vector3D v1D = line.intersection(subLine.line);
    // check location of point with respect to first sub-line
    Location loc1 = remainingRegion.checkPoint(line.toSubSpace(v1D));
    // check location of point with respect to second sub-line
    Location loc2 = subLine.remainingRegion.checkPoint(subLine.line.toSubSpace(v1D));
    if (includeEndPoints) {
        if ((loc1 == Location.BOUNDARY) || (loc2 == Location.BOUNDARY)) {
            return v1D;
        } else {
            return null;
        }
    } else {
        if ((loc1 == Location.INSIDE) && (loc2 == Location.INSIDE)) {
            return v1D;
        } else {
            return null;
        }
    }
}


## Fixed Function 2
public Vector2D intersection(final SubLine subLine, final boolean includeEndPoints) {
    // retrieve the underlying lines
    Line line1 = (Line) getHyperplane();
    Line line2 = (Line) subLine.getHyperplane();
    // compute the intersection on infinite line
    Vector2D v2D = line1.intersection(line2);
    // check location of point with respect to first sub-line
    Location loc1 = getRemainingRegion().checkPoint(line1.toSubSpace(v2D));
    // check location of point with respect to second sub-line
    Location loc2 = subLine.getRemainingRegion().checkPoint(line2.toSubSpace(v2D));
    if (includeEndPoints) {
        if ((loc1 == Location.BOUNDARY) || (loc2 == Location.BOUNDARY)) {
            return v2D;
        } else {
            return null;
        }
    } else {
        if ((loc1 == Location.INSIDE) && (loc2 == Location.INSIDE)) {
            return v2D;
        } else {
            return null;
        }
    }
}