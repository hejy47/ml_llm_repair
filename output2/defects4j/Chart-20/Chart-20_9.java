## Fixed Function 1
public ValueMarker(double value, Paint paint, Stroke stroke, Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke);
    this.value = value;
    setOutlinePaint(outlinePaint);
    setOutlineStroke(outlineStroke);
    setAlpha(alpha);
} 

# Explanation
In the original `ValueMarker` constructor, there is an incorrect use of the `super` constructor call, passing the `paint` parameter twice instead of the `outlinePaint` and `outlineStroke` parameters. Additionally, the `alpha` parameter is not used. In the fixed version, the correct parameters are passed to the `super` constructor, and the `setOutlinePaint`, `setOutlineStroke`, and `setAlpha` methods are called to correctly set the additional properties of the `ValueMarker`.