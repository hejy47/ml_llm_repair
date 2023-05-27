## Fixed Function 1
public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    ShapeList shapeList = (ShapeList) obj;
    if (shapeList.size() != this.size()) {
        return false;
    }
    for (int i = 0; i < this.size(); i++) {
        if (!this.get(i).equals(shapeList.get(i))) {
            return false;
        }
    }
    return true;
}