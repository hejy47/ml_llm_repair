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
    return super.equals(obj);
} 

Explanation: 
The current implementation only checks if the input object is an instance of ShapeList and if so, it returns the result of the superclass implementation of the equals method. However, this can result in incorrect behavior if the superclass implementation does not correctly compare all relevant fields of the ShapeList object. 

By modifying the equals method to also compare the size of the ShapeLists, we can ensure that two ShapeLists are only considered equal if they have the same number of shapes. We also cast the input object as a ShapeList so that we can compare their sizes.