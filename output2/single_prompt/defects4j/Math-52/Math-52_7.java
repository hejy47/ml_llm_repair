## Fixed Function 1
public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {
    // norms computation
    double u1u1 = u1.getNormSq();
    double u2u2 = u2.getNormSq();
    double v1v1 = v1.getNormSq();
    double v2v2 = v2.getNormSq();
    if (FastMath.abs(u1u1) < Precision.SAFE_MIN ||
        FastMath.abs(u2u2) < Precision.SAFE_MIN ||
        FastMath.abs(v1v1) < Precision.SAFE_MIN ||
        FastMath.abs(v2v2) < Precision.SAFE_MIN) {
        throw new MathIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR);
    }
    // normalize v1 in order to have (v1'|v1') = (u1|u1)
    v1 = v1.scalarMultiply(FastMath.sqrt(u1u1 / v1v1));
    // adjust v2 in order to have (u1|u2) = (v1'|v2') and (v2'|v2') = (u2|u2)
    double u1u2 = u1.dotProduct(u2);
    double v1v2 = v1.dotProduct(v2);
    double coeffU = u1u2 / u1u1;
    double coeffV = v1v2 / u1u1;
    double beta = FastMath.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha = coeffU - beta * coeffV;
    v2 = v2.combine(alpha, v1, beta, v2);
    // preliminary computation
    Vector3D u3 = u1.crossProduct(u2);
    double uu = u3.getNormSq();
    if (uu < Precision.SAFE_MIN) {
        // the (q1, q2, q3) vector is close to the (u1, u2) plane
        // we try other vectors
        u3 = Vector3D.crossProduct(u1, v1.vectorialProduct(v2)).normalize();
        uu = u3.getNormSq();
        if (uu < Precision.SAFE_MIN) {
            // the (q1, q2, q3) vector is also close to the (u1, u3) plane,
            // it is almost aligned with u1: we try (u2, u3) and (v2, v3)
            u3 = Vector3D.crossProduct(u2, v2.vectorialProduct(v1)).normalize();
            uu = u3.getNormSq();
            if (uu < Precision.SAFE_MIN) {
                // the (q1, q2, q3) vector is aligned with everything
                // this is really the identity rotation
                q0 = 1.0;
                q1 = 0.0;
                q2 = 0.0;
                q3 = 0.0;
                return;
            }
        }
    }
    Vector3D k = new Vector3D(u3.getX() / uu, u3.getY() / uu, u3.getZ() / uu);
    double c = v1.dotProduct(v2);
    double s = FastMath.sqrt(1 - c * c);
    if (s < Precision.SAFE_MIN) {
        // v1 and v2 are colinear
        q0 = FastMath.sqrt(0.5 * (1 + c));
        double coef = 0.25 / q0;
        q1 = coef * (v1.getX() + v2.getX());
        q2 = coef * (v1.getY() + v2.getY());
        q3 = coef * (v1.getZ() + v2.getZ());
    } else {
        // v1 and v2 are not colinear
        double angle = FastMath.atan2(s, c);
        q0 = FastMath.cos(0.5 * angle);
        double coef = FastMath.sin(0.5 * angle) / s;
        q1 = coef * (v1.getY() * v2.getZ() - v1.getZ() * v2.getY());
        q2 = coef * (v1.getZ() * v2.getX() - v1.getX() * v2.getZ());
        q3 = coef * (v1.getX() * v2.getY() - v1.getY() * v2.getX());
    }
}