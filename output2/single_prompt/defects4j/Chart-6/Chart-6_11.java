## Fixed Function 1
public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    ShapeList other = (ShapeList) obj;
    if (other.size() != this.size()) {
        return false;
    }
    for (int i = 0; i < size(); i++) {
        if (!this.get(i).equals(other.get(i))) {
            return false;
        }
    }
    return true;
} 

Note: This fix assumes that the ShapeList class has a size() method and a get(int index) method. The actual implementation may vary.