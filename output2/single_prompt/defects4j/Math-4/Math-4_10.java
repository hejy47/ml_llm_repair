## Fixed Function 1
public Vector3D intersection(final SubLine subLine, final boolean includeEndPoints) {
    // compute the intersection on infinite line
    Vector3D v1D = line.intersection(subLine.line);
    // check location of point with respect to first sub-line
    Location loc1 = remainingRegion.checkPoint(line.toSubSpace(v1D));
    // check location of point with respect to second sub-line
    Location loc2 = subLine.remainingRegion.checkPoint(subLine.line.toSubSpace(v1D));
    if (includeEndPoints) {
        //Fixed the condition to check if both loc1 and loc2 are not OUTSIDE
        return ((loc1 != Location.OUTSIDE) && (loc2 != Location.OUTSIDE)) ? v1D : null;
    } else {
        //Fixed the condition to check if both loc1 and loc2 are INSIDE or ON_BOUNDARY
        return ((loc1 == Location.INSIDE || loc1 == Location.ON_BOUNDARY) && (loc2 == Location.INSIDE || loc2 == Location.ON_BOUNDARY)) ? v1D : null;
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
        //Fixed the condition to check if both loc1 and loc2 are not OUTSIDE
        return ((loc1 != Location.OUTSIDE) && (loc2 != Location.OUTSIDE)) ? v2D : null;             
    } else {
        //Fixed the condition to check if both loc1 and loc2 are INSIDE or ON_BOUNDARY
        return ((loc1 == Location.INSIDE || loc1 == Location.ON_BOUNDARY) && (loc2 == Location.INSIDE || loc2 == Location.ON_BOUNDARY)) ? v2D : null;
    }
}