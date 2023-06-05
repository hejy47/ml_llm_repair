## Fixed Function 1
public ValueMarker(double value, Paint paint, Stroke stroke, Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke);
    this.value = value;
    setPaint(paint);
    setStroke(stroke);
    setOutlinePaint(outlinePaint);
    setOutlineStroke(outlineStroke);
    setAlpha(alpha);
}