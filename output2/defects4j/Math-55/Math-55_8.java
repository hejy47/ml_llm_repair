## Fixed Function 1
public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude
    // we reduce cancellation errors by preconditioning,
    // we replace v1 by v3 = v1 - rho v2 with rho chosen in order to compute
    // v3 without loss of precision. See Kahan lecture
    // "Computing Cross-Products and Rotations in 2- and 3-Dimensional Euclidean Spaces"
    // available at http://www.cs.berkeley.edu/~wkahan/MathH110/Cross.pdf
    // compute rho as an 16 bits approximation of v1.v2 / v2.v2
    // compute cross product from v3 and v2 instead of v1 and v2
    double rho = ((double) ((float) (v1.dotProduct(v2) / v2.getNormSq()))) * (1 << 16);
    Vector3D v3 = new Vector3D(1.0, v1, -rho, v2);
    return new Vector3D(1.0, v3, v2);
}