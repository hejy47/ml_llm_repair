protected AxisState drawLabel(String label, Graphics2D g2, Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge, AxisState state, PlotRenderingInfo plotState) {
    // it is unlikely that 'state' will be null, but check anyway...
    if (state == null) {
        throw new IllegalArgumentException("Null 'state' argument.");
    }
    if ((label == null) || (label.equals(""))) {
        return state;
    }
    Font font = getLabelFont();
    RectangleInsets insets = getLabelInsets();
    g2.setFont(font);
    g2.setPaint(getLabelPaint());
    FontMetrics fm = g2.getFontMetrics();
    Rectangle2D labelBounds = TextUtilities.getTextBounds(label, g2, fm);
    Shape hotspot = null;
    double labelx = 0, labely = 0, angle = 0;
    if (edge == RectangleEdge.TOP) {
        angle = -getLabelAngle();
        labelx = dataArea.getCenterX();
        labely = state.getCursor() - insets.getBottom() - labelBounds.getHeight() / 2.0;
        hotspot = new Rectangle2D.Double(labelx - labelBounds.getWidth() / 2.0, labely - labelBounds.getHeight() / 2.0, labelBounds.getWidth(), labelBounds.getHeight());
        state.cursorUp(insets.getTop() + labelBounds.getHeight() + insets.getBottom());
    } else if (edge == RectangleEdge.BOTTOM) {
        angle = getLabelAngle();
        labelx = dataArea.getCenterX();
        labely = state.getCursor() + insets.getTop() + labelBounds.getHeight() / 2.0;
        hotspot = new Rectangle2D.Double(labelx - labelBounds.getWidth() / 2.0, labely - labelBounds.getHeight() / 2.0, labelBounds.getWidth(), labelBounds.getHeight());
        state.cursorDown(insets.getTop() + labelBounds.getHeight() + insets.getBottom());
    } else if (edge == RectangleEdge.LEFT) {
        angle = -getLabelAngle() - Math.PI / 2.0;
        labelx = state.getCursor() - insets.getRight() - labelBounds.getWidth() / 2.0;
        labely = dataArea.getCenterY();
        hotspot = new Rectangle2D.Double(labelx - labelBounds.getWidth() / 2.0, labely - labelBounds.getHeight() / 2.0, labelBounds.getWidth(), labelBounds.getHeight());
        state.cursorLeft(insets.getLeft() + labelBounds.getWidth() + insets.getRight());
    } else if (edge == RectangleEdge.RIGHT) {
        angle = getLabelAngle() + Math.PI / 2.0;
        labelx = state.getCursor() + insets.getLeft() + labelBounds.getWidth() / 2.0;
        labely = dataArea.getCenterY();
        hotspot = new Rectangle2D.Double(labelx - labelBounds.getWidth() / 2.0, labely - labelBounds.getHeight() / 2.0, labelBounds.getWidth(), labelBounds.getHeight());
        state.cursorRight(insets.getLeft() + labelBounds.getWidth() + insets.getRight());
    }
    AffineTransform t = AffineTransform.getRotateInstance(angle, labelx, labely);
    Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
    TextUtilities.drawRotatedString(label, g2, (float) rotatedLabelBounds.getBounds2D().getCenterX(), (float) rotatedLabelBounds.getBounds2D().getCenterY(), TextAnchor.CENTER, angle, TextAnchor.CENTER);
    if (plotState != null && hotspot != null) {
        ChartRenderingInfo owner = plotState.getOwner();
        EntityCollection entities = owner.getEntityCollection();
        if (entities != null) {
            entities.add(new AxisLabelEntity(this, hotspot, this.labelToolTip, this.labelURL));
        }
    }
    return state;
}