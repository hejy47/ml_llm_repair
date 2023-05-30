## Fixed Function 1
public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude
    double norm1 = v1.getNorm();
    double norm2 = v2.getNorm();
    double rescaleFactor = FastMath.sqrt(norm1 * norm2);
    Vector3D v3 = v1.cross(v2).scalarMultiply(1.0 / rescaleFactor);
    return v3;
}