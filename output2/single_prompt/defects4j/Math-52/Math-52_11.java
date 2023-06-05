## Fixed Function 1
public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {
    // norms computation
    double u1u1 = u1.getNormSq();
    double u2u2 = u2.getNormSq();
    double v1v1 = v1.getNormSq();
    double v2v2 = v2.getNormSq();
    if ((u1u1 == 0) || (u2u2 == 0) || (v1v1 == 0) || (v2v2 == 0)) {
        throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR);
    }
    // normalize v1 in order to have (v1'|v1') = (u1|u1)
    v1 = new Vector3D(FastMath.sqrt(u1u1 / v1v1), v1);
    // adjust v2 in order to have (u1|u2) = (v1'|v2') and (v2'|v2') = (u2|u2)
    double u1u2 = u1.dotProduct(u2);
    double v1v2 = v1.dotProduct(v2);
    double coeffU = u1u2 / u1u1;
    double coeffV = v1v2 / u1u1;
    double beta = FastMath.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha = coeffU - beta * coeffV;
    v2 = new Vector3D(alpha, v1, beta, v2);
    // preliminary computation
    Vector3D u3 = u1.crossProduct(u2);
    if (u3.getNorm() == 0) {
        // the (u1, u2) vector is already aligned with u3, choose another vector
        u3 = new Vector3D(u2.getY(), -u2.getX(), 0);
        if (u3.getNorm() == 0) {
            u3 = new Vector3D(u2.getZ(), 0, -u2.getX());
        }
    }
    Vector3D v3 = v1.crossProduct(v2);
    if (v3.getNorm() == 0) {
        // the (v1, v2) vector is already aligned with v3, choose another vector
        v3 = new Vector3D(v2.getY(), -v2.getX(), 0);
        if (v3.getNorm() == 0) {
            v3 = new Vector3D(v2.getZ(), 0, -v2.getX());
        }
    }
    Vector3D k = u3.crossProduct(v3);
    double normK = k.getNorm();
    if (normK == 0) {
        // the (u1, u2) plane is parallel to the (v1, v2) plane
        // choose (u1, u3) to define the rotation and compute the angle
        double c = u1.dotProduct(u3);
        double s = u1.dotProduct(u2.crossProduct(u3));
        if (s < 0) {
            // ensure the angle is positive
            c = -c;
            s = -s;
        }
        double angle = FastMath.atan2(s, c);
        q0 = FastMath.cos(0.5 * angle);
        q1 = FastMath.sin(0.5 * angle) * u3.getX();
        q2 = FastMath.sin(0.5 * angle) * u3.getY();
        q3 = FastMath.sin(0.5 * angle) * u3.getZ();
        return;
    }
    // compute the vectorial part
    double inv = 0.5 / normK;
    q1 = inv * k.getX();
    q2 = inv * k.getY();
    q3 = inv * k.getZ();
    // compute the scalar part
    double s = u3.dotProduct(v3) / normK;
    double angle = FastMath.atan2(normK, s);
    q0 = FastMath.cos(0.5 * angle);
    q1 *= FastMath.sin(0.5 * angle);
    q2 *= FastMath.sin(0.5 * angle);
    q3 *= FastMath.sin(0.5 * angle);
}