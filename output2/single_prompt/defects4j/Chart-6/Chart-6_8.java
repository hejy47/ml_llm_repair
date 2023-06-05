## Fixed Function 1
public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    ShapeList shapeList = (ShapeList) obj;
    return super.equals(shapeList);
} 

Explanation:
The issue with the buggy function was that it was calling the super class equals method directly without checking if the parameter obj is an instance of ShapeList. This can cause issues if the parameter obj is not of the expected type. The fixed function adds an instance check for ShapeList and then casts the obj to ShapeList before calling super.equals().