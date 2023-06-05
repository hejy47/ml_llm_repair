## Fixed Function 1
//-------------------------------------------------------------
@Deprecated
public double getPct(Object v) {
    if (v instanceof Comparable) {
        return getCumPct((Comparable<?>) v);
    }
    throw new IllegalArgumentException("Object must implement Comparable");
}