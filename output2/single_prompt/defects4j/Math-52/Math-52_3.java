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
    Vector3D v3 = v1.crossProduct(v2);
    Vector3D k = u3.crossProduct(v3);
    double normK = k.getNorm();
    if (normK == 0) {
        // the (q1, q2, q3) vector is aligned with one of the reference vectors (u1, u2 or v1, v2)
        if (u1.crossProduct(v1).getNorm() == 0) {
            // the (q1, q2, q3) vector is aligned with u1 and v1
            // we try other vectors
            v3 = v1.crossProduct(u3);
            k = u3.crossProduct(v3);
            normK = k.getNorm();
            if (normK == 0) {
                // the (q1, q2, q3) vector is aligned with u1 and u3,
                // it is almost aligned with u1: we try (u2, u3) and (v2, v3)
                v3 = v2.crossProduct(u3);
                k = u3.crossProduct(v3);
                normK = k.getNorm();
                if (normK == 0) {
                    // the (q1, q2, q3) vector is aligned with everything
                    // this is really the identity rotation
                    q0 = 1.0;
                    q1 = 0.0;
                    q2 = 0.0;
                    q3 = 0.0;
                    return;
                }
                // we will have to use u2 and v2 to compute the scalar part
                u1 = u2;
                v1 = v2;
            }
        } else {
            // the (q1, q2, q3) vector is aligned with u2 and v2,
            // it is almost aligned with u2: we try (u1, u3) and (v1, v3)
            v3 = v1.crossProduct(u3);
            k = u3.crossProduct(v3);
            normK = k.getNorm();
            if (normK == 0) {
                // the (q1, q2, q3) vector is aligned with u2 and u3,
                // it is almost aligned with u2: we try (u1, u3) and (v1, v3)
                v3 = v2.crossProduct(u3);
                k = u3.crossProduct(v3);
                normK = k.getNorm();
                if (normK == 0) {
                    // the (q1, q2, q3) vector is aligned with everything
                    // this is really the identity rotation
                    q0 = 1.0;
                    q1 = 0.0;
                    q2 = 0.0;
                    q3 = 0.0;
                    return;
                }
                // we will have to use u1 and v1 to compute the scalar part
                u2 = u1;
                v2 = v1;
            }
            // we will have to use v2 and v3 to compute the scalar part
            Vector3D tmp = u1;
            u1 = u2;
            u2 = tmp;
            tmp = v1;
            v1 = v2;
            v2 = v3;
            v3 = tmp;
        }
    }
    // compute the vectorial part
    double inv = 1.0 / (normK * FastMath.sqrt(u3.getNormSq() / u1u1));
    k = new Vector3D(inv, k);
    q1 = k.getX();
    q2 = k.getY();
    q3 = k.getZ();
    q0 = FastMath.sqrt(1.0 - q1 * q1 - q2 * q2 - q3 * q3);
    if (u1.equals(v1)) {
        // we have chosen u1 and v1 consistent with the sign of q0
        q0 = -q0;
    }
}