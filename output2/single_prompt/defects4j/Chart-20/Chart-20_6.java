## Fixed Function 1
public ValueMarker(double value, Paint paint, Stroke stroke, Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke);
    this.setOutlinePaint(outlinePaint);
    this.setOutlineStroke(outlineStroke);
    this.setAlpha(alpha);
    this.value = value;
}