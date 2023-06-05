## Fixed Function 1
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    RealMatrix squareRootMatrix = dec.getSquareRoot();
    if(squareRootMatrix != null){
        return squareRootMatrix;
    } else {
        throw new IllegalArgumentException("Input matrix is not positively definite");
    }
}