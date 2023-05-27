## Fixed Function 1
public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    ShapeList otherList = (ShapeList) obj;
    if (size() != otherList.size()) {
        return false;
    }
    for (int i = 0; i < size(); i++) {
        if (!get(i).equals(otherList.get(i))) {
            return false;
        }
    }
    return true;
}