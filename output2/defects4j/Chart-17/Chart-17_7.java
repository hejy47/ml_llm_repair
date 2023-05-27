## Fixed Function 1
public Object clone() throws CloneNotSupportedException {
    Chart clone = (Chart) super.clone();
    clone.data = Arrays.copyOf(this.data, this.data.length);
    return clone;
} 

Note: This fix assumes that the "Chart" class has a private field called "data" that is an array of some data type. It also assumes that the data type of the array is cloneable or immutable, as necessary.