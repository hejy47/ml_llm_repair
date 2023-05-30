## Fixed Function 1
public Vector3D intersection(final SubLine subLine, final boolean includeEndPoints) {
    // retrieve the underlying lines
    Line line1 = getHyperplane();
    Line line2 = subLine.getHyperplane();
    // check if the lines are parallel using normalized vectors
    if (line1.getDirection().cross(line2.getDirection()).getNormSq() < 1.0e-25) {
        // lines are parallel
        return null;
    }
    // compute the intersection on infinite line
    Vector3D v1D = line1.intersection(line2);
    // check location of point with respect to first sub-line
    Location loc1 = remainingRegion.checkPoint(line1.toSubSpace(v1D));
    // check location of point with respect to second sub-line
    Location loc2 = subLine.remainingRegion.checkPoint(line2.toSubSpace(v1D));
    if (includeEndPoints) {
        return ((loc1 != Location.OUTSIDE) && (loc2 != Location.OUTSIDE)) ? v1D : null;
    } else {
        return ((loc1 == Location.INSIDE) && (loc2 == Location.INSIDE)) ? v1D : null;
    }
}

## Fixed Function 2
public Vector2D intersection(final SubLine subLine, final boolean includeEndPoints) {
    // retrieve the underlying lines
    Line line1 = getHyperplane();
    Line line2 = subLine.getHyperplane();
    // check if the lines are parallel using normalized vectors
    if (line1.getDirection().cross(line2.getDirection()).getNormSq() < 1.0e-25) {
        // lines are parallel
        return null;
    }
    // compute the intersection on infinite line
    Vector2D v2D = line1.intersection(line2);
    // check location of point with respect to first sub-line
    Location loc1 = getRemainingRegion().checkPoint(line1.toSubSpace(v2D));
    // check location of point with respect to second sub-line
    Location loc2 = subLine.getRemainingRegion().checkPoint(line2.toSubSpace(v2D));
    if (includeEndPoints) {
        return ((loc1 != Location.OUTSIDE) && (loc2 != Location.OUTSIDE)) ? v2D : null;
    } else {
        return ((loc1 == Location.INSIDE) && (loc2 == Location.INSIDE)) ? v2D : null;
    }
}