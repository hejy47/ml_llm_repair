## Fixed Function 1
public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude
    final double v1Norm = v1.getNorm();
    final double v2Norm = v2.getNorm();
    if (v1Norm == 0 || v2Norm == 0) {
        return Vector3D.ZERO;
    }
    final double scale = FastMath.max(v1Norm, v2Norm);
    final Vector3D sV1 = v1.scalarMultiply(scale / v1Norm);
    final Vector3D sV2 = v2.scalarMultiply(scale / v2Norm);
    
    // we reduce cancellation errors by preconditioning,
    // we replace v1 by v3 = v1 - rho v2 with rho chosen in order to compute
    // v3 without loss of precision. See Kahan lecture
    // "Computing Cross-Products and Rotations in 2- and 3-Dimensional Euclidean Spaces"
    // available at http://www.cs.berkeley.edu/~wkahan/MathH110/Cross.pdf
    // compute rho as an 8 bits approximation of v1.v2 / v2.v2
    final double rho = FastMath.rint(sV1.dotProduct(sV2) / sV2.dotProduct(sV2) * 256) / 256;
    final Vector3D v3 = sV1.subtract(sV2.scalarMultiply(rho));
    
    // compute cross product from v3 and v2 instead of v1 and v2
    return new Vector3D(
            v3.getY() * v2.getZ() - v3.getZ() * v2.getY(),
            v3.getZ() * v2.getX() - v3.getX() * v2.getZ(),
            v3.getX() * v2.getY() - v3.getY() * v2.getX());
}