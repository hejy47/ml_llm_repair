## Fixed Function 1
public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    double rho = v1.dotProduct(v2) / v2.normSq();
    Vector3D v3 = new Vector3D(1.0, v1, -rho, v2);
    return new Vector3D(v3.y * v2.z - v3.z * v2.y,
                        v3.z * v2.x - v3.x * v2.z,
                        v3.x * v2.y - v3.y * v2.x);
}