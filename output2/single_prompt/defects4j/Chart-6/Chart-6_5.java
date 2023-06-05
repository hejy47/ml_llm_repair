## Fixed Function 1
public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    ShapeList shapeList = (ShapeList) obj;
    return this.size() == shapeList.size() && super.equals(obj);
}