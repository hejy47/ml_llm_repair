## Fixed Function 1
public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    double v1Norm = v1.getNorm();
    double v2Norm = v2.getNorm();
    double scale = v1Norm * v2Norm;
    if (scale == 0) {
        return Vector3D.ZERO;
    }
    double v1x = v1.getX();
    double v1y = v1.getY();
    double v1z = v1.getZ();
    double v2x = v2.getX();
    double v2y = v2.getY();
    double v2z = v2.getZ();
    double norm2Max = max(max(abs(v1x), abs(v1y)), max(abs(v1z), max(abs(v2x), abs(v2y))));
    double eps = Precision.EPSILON * norm2Max / scale;
    double l1 = ((v1x * v2x + v1y * v2y) + v1z * v2z) / v2Norm;
    double l2 = ((v1y * v2z - v1z * v2y) + v1x * v2Norm) / v2Norm;
    double l3 = ((v1z * v2x - v1x * v2z) + v1y * v2Norm) / v2Norm;
    double delta = eps * ((abs(l1) + abs(l2)) + abs(l3));
    double crossX = v1y * v2z - v1z * v2y;
    double crossY = v1z * v2x - v1x * v2z;
    double crossZ = v1x * v2y - v1y * v2x;
    if ((abs(crossX) <= delta) && (abs(crossY) <= delta) && (abs(crossZ) <= delta)) {
        double cx = ((v1y * v2z + -v1z * v2y)) + ((-v1z * v2y + v1y * v2z));
        double cy = ((v1z * v2x + -v1x * v2z)) + ((-v1x * v2z + v1z * v2x));
        double cz = ((v1x * v2y + -v1y * v2x)) + ((-v1y * v2x + v1x * v2y));
        double overrideNorm = sqrt(cx * cx + cy * cy + cz * cz) / sqrt(v1Norm * v2Norm);
        return new Vector3D(cx / overrideNorm, cy / overrideNorm, cz / overrideNorm);
    }
    return new Vector3D(crossX, crossY, crossZ);
}