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
    double normU3 = u3.getNorm();
    double normV3 = v3.getNorm();
    if (normU3 == 0 || normV3 == 0) {
        // the (q1, q2, q3) vector is aligned with everything
        // this is really the identity rotation
        q0 = 1.0;
        q1 = 0.0;
        q2 = 0.0;
        q3 = 0.0;
        return;
    }
    double dotProduct = u3.dotProduct(v3) / (normU3 * normV3);
    double delta = dotProduct + 1;
    if (delta < SINGULARITY_THRESHOLD) {
        // special case u3 and v3 are parallel
        Vector3D w = u2.crossProduct(v2);
        double normW = w.getNorm();
        if (normW == 0) {
            // u2 and v2 are also parallel, we can select any orthogonal vectors
            w = u1.orthogonal();
        }
        q0 = 0;
        q1 = w.getX();
        q2 = w.getY();
        q3 = w.getZ();
        return;
    }
    // compute the vectorial part
    Vector3D w = u3.crossProduct(v3);
    double normW = w.getNorm();
    q1 = w.getX() / normW;
    q2 = w.getY() / normW;
    q3 = w.getZ() / normW;
    // compute the scalar part
    q0 = FastMath.sqrt(delta / 2.0);
    double inverse = 1 / (2.0 * q0 * normW);
    q1 *= inverse;
    q2 *= inverse;
    q3 *= inverse;
}