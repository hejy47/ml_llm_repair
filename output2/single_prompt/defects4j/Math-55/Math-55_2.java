## Fixed Function 1
public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude
    double normV1 = v1.getNorm();
    double normV2 = v2.getNorm();
    if (normV1 == 0 || normV2 == 0) {
        return Vector3D.ZERO;
    }
    Vector3D u1 = v1.scalarMultiply(1.0 / normV1);
    Vector3D u2 = v2.scalarMultiply(1.0 / normV2);

    // we reduce cancellation errors by preconditioning,
    // we replace v1 by v3 = v1 - rho v2 with rho chosen in order to compute
    // v3 without loss of precision. See Kahan lecture
    // "Computing Cross-Products and Rotations in 2- and 3-Dimensional Euclidean Spaces"
    // available at http://www.cs.berkeley.edu/~wkahan/MathH110/Cross.pdf
    double rho = u1.dotProduct(u2);
    rho = FastMath.max(-1.0, FastMath.min(1.0, rho)); // clip to ensure acos argument does not exceed [-1, 1]
    rho = FastMath.acos(rho);
    if (rho < 1.0e-10) {
        return Vector3D.ZERO;
    }
    Vector3D u3 = u1.subtract(u2.scalarMultiply(rho / FastMath.sin(rho)));
    return new Vector3D(u2.getY() * u3.getZ() - u2.getZ() * u3.getY(),
                        u2.getZ() * u3.getX() - u2.getX() * u3.getZ(),
                        u2.getX() * u3.getY() - u2.getY() * u3.getX())
           .scalarMultiply(normV1 * normV2 / FastMath.sin(rho));
}