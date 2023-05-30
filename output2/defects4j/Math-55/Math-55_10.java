## Fixed Function 1
public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    double v1x = v1.getX();
    double v1y = v1.getY();
    double v1z = v1.getZ();
    double v2x = v2.getX();
    double v2y = v2.getY();
    double v2z = v2.getZ();

    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude
    double scale = Math.max(Math.max(Math.abs(v1x), Math.abs(v1y)), Math.abs(v1z));
    scale = Math.max(scale, Math.max(Math.abs(v2x), Math.max(Math.abs(v2y), Math.abs(v2z))));
    scale = 1.0 / scale;
    v1x *= scale;
    v1y *= scale;
    v1z *= scale;
    v2x *= scale;
    v2y *= scale;
    v2z *= scale;

    // we reduce cancellation errors by preconditioning,
    // we replace v1 by v3 = v1 - rho v2 with rho chosen in order to compute
    // v3 without loss of precision. See Kahan lecture
    // "Computing Cross-Products and Rotations in 2- and 3-Dimensional Euclidean Spaces"
    // available at http://www.cs.berkeley.edu/~wkahan/MathH110/Cross.pdf
    // compute rho as an 8 bits approximation of v1.v2 / v2.v2
    double rho = ((v1x * v2x + v1y * v2y) + v1z * v2z) / 3;
    double v3x = v1x - rho * v2x;
    double v3y = v1y - rho * v2y;
    double v3z = v1z - rho * v2z;

    // compute cross product from v3 and v2 instead of v1 and v2
    return new Vector3D(v3y * v2z - v3z * v2y, v3z * v2x - v3x * v2z, v3x * v2y - v3y * v2x);
}