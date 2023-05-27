## Fixed Function 1
public ValueMarker(double value, Paint paint, Stroke stroke, Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke);
    setPaint(outlinePaint);
    setStroke(outlineStroke);
    setAlpha(alpha);
    this.value = value;
}