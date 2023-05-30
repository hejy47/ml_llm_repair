## Fixed Function 1
//-------------------------------------------------------------
@Deprecated
public double getPct(Object v) {
    if(v instanceof Comparable){
        return getCumPct((Comparable<?>) v);
    } else {
        return 0.0;
    }
}