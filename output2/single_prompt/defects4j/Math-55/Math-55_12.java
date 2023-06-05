## Fixed Function 1
public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    double rho = v1.dot(v2) / v2.normSq();
    Vector3D v3 = new Vector3D(v1.getX() - rho * v2.getX(), v1.getY() - rho * v2.getY(), v1.getZ() - rho * v2.getZ());
    return new Vector3D(v3.getY() * v2.getZ() - v3.getZ() * v2.getY(), v3.getZ() * v2.getX() - v3.getX() * v2.getZ(), v3.getX() * v2.getY() - v3.getY() * v2.getX());
}

The fixed function computes the value of `rho` correctly and uses it to calculate `v3` before computing the cross product using the `v3` and `v2` vectors.