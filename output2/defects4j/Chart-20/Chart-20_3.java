## Fixed Function 1
public ValueMarker(double value, Paint paint, Stroke stroke, Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke);
    this.outlinePaint = outlinePaint;
    this.outlineStroke = outlineStroke;
    this.alpha = alpha;
    this.value = value;
}