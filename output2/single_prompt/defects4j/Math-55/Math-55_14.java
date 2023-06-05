## Fixed Function 1
public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    double v1Norm = v1.getNorm();
    double v2Norm = v2.getNorm();

    // if either vector has zero norm, cross product is zero
    if (v1Norm == 0 || v2Norm == 0) {
        return Vector3D.ZERO;
    }

    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude
    double rescaleFactor = FastMath.sqrt(v1Norm * v2Norm);
    Vector3D v1Scaled = v1.scalarMultiply(rescaleFactor / v1Norm);
    Vector3D v2Scaled = v2.scalarMultiply(rescaleFactor / v2Norm);

    // compute rho as an 8 bits approximation of v1Scaled.v2Scaled / v2Scaled.v2Scaled
    double rho = FastMath.max(-128.0, FastMath.min(128.0, v1Scaled.dotProduct(v2Scaled) / v2Scaled.dotProduct(v2Scaled)));
    rho = Math.rint(rho);

    // compute v3 = v1Scaled - rho v2Scaled and cross product from v3 and v2Scaled
    Vector3D v3 = v1Scaled.subtract(v2Scaled.scalarMultiply(rho));
    return new Vector3D(
        (v3.getY() * v2Scaled.getZ()) - (v3.getZ() * v2Scaled.getY()),
        (v3.getZ() * v2Scaled.getX()) - (v3.getX() * v2Scaled.getZ()),
        (v3.getX() * v2Scaled.getY()) - (v3.getY() * v2Scaled.getX())
    );
}