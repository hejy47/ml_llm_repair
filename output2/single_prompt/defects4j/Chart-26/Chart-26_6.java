protected AxisState drawLabel(String label, Graphics2D g2, Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge, AxisState state, PlotRenderingInfo plotState) {
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
    double labelx = 0.0, labely = 0.0;
    if (edge == RectangleEdge.TOP) {
        hotspot = computeTopLabelHotspot(g2, edge, labelBounds, insets, dataArea, state);
    } else if (edge == RectangleEdge.BOTTOM) {
        hotspot = computeBottomLabelHotspot(g2, edge, labelBounds, insets, dataArea, state);
    } else if (edge == RectangleEdge.LEFT) {
        hotspot = computeLeftLabelHotspot(g2, edge, labelBounds, insets, dataArea, state);
    } else if (edge == RectangleEdge.RIGHT) {
        hotspot = computeRightLabelHotspot(g2, edge, labelBounds, insets, dataArea, state);
    }
    if (plotState != null && hotspot != null) {
        ChartRenderingInfo owner = plotState.getOwner();
        EntityCollection entities = owner.getEntityCollection();
        if (entities != null) {
            entities.add(new AxisLabelEntity(this, hotspot, getLabelToolTip(), getLabelURL()));
        }
    }
    return state;
}

private Shape computeTopLabelHotspot(Graphics2D g2, RectangleEdge edge, Rectangle2D labelBounds, RectangleInsets insets, Rectangle2D dataArea, AxisState state) {
    AffineTransform t = AffineTransform.getRotateInstance(getLabelAngle(), labelBounds.getCenterX(), labelBounds.getCenterY());
    Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
    Rectangle2D rotatedBounds = rotatedLabelBounds.getBounds2D();
    double startX = dataArea.getCenterX();
    double startY = state.getCursor() - insets.getBottom() - rotatedBounds.getHeight() / 2.0;
    TextUtilities.drawRotatedString(getLabel(), g2, (float) startX, (float) startY, TextAnchor.CENTER, getLabelAngle(), TextAnchor.CENTER);
    state.cursorUp(insets.getTop() + rotatedBounds.getHeight() + insets.getBottom());
    return new Rectangle2D.Double(startX - rotatedBounds.getWidth() / 2.0, startY - rotatedBounds.getHeight() / 2.0,
            rotatedBounds.getWidth(), rotatedBounds.getHeight());
}

private Shape computeBottomLabelHotspot(Graphics2D g2, RectangleEdge edge, Rectangle2D labelBounds, RectangleInsets insets, Rectangle2D dataArea, AxisState state) {
    AffineTransform t = AffineTransform.getRotateInstance(getLabelAngle(), labelBounds.getCenterX(), labelBounds.getCenterY());
    Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
    Rectangle2D rotatedBounds = rotatedLabelBounds.getBounds2D();
    double startX = dataArea.getCenterX();
    double startY = state.getCursor() + insets.getTop() + rotatedBounds.getHeight() / 2.0;
    TextUtilities.drawRotatedString(getLabel(), g2, (float) startX, (float) startY, TextAnchor.CENTER, getLabelAngle(), TextAnchor.CENTER);
    state.cursorDown(insets.getTop() + rotatedBounds.getHeight() + insets.getBottom());
    return new Rectangle2D.Double(startX - rotatedBounds.getWidth() / 2.0, startY - rotatedBounds.getHeight() / 2.0,
            rotatedBounds.getWidth(), rotatedBounds.getHeight());
}

private Shape computeLeftLabelHotspot(Graphics2D g2, RectangleEdge edge, Rectangle2D labelBounds, RectangleInsets insets, Rectangle2D dataArea, AxisState state) {
    AffineTransform t = AffineTransform.getRotateInstance(getLabelAngle() - Math.PI / 2.0, labelBounds.getCenterX(), labelBounds.getCenterY());
    Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
    Rectangle2D rotatedBounds = rotatedLabelBounds.getBounds2D();
    double startX = state.getCursor() - insets.getRight() - rotatedBounds.getWidth() / 2.0;
    double startY = dataArea.getCenterY();
    TextUtilities.drawRotatedString(getLabel(), g2, (float) startX, (float) startY, TextAnchor.CENTER, getLabelAngle() - Math.PI / 2.0, TextAnchor.CENTER);
    state.cursorLeft(insets.getLeft() + rotatedBounds.getWidth() + insets.getRight());
    return new Rectangle2D.Double(startX - rotatedBounds.getWidth() / 2.0, startY - rotatedBounds.getHeight() / 2.0,
            rotatedBounds.getWidth(), rotatedBounds.getHeight());
}

private Shape computeRightLabelHotspot(Graphics2D g2, RectangleEdge edge, Rectangle2D labelBounds, RectangleInsets insets, Rectangle2D dataArea, AxisState state) {
    AffineTransform t = AffineTransform.getRotateInstance(getLabelAngle() + Math.PI / 2.0, labelBounds.getCenterX(), labelBounds.getCenterY());
    Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
    Rectangle2D rotatedBounds = rotatedLabelBounds.getBounds2D();
    double startX = state.getCursor() + insets.getLeft() + rotatedBounds.getWidth() / 2.0;
    double startY = dataArea.getCenterY();
    TextUtilities.drawRotatedString(getLabel(), g2, (float) startX, (float) startY, TextAnchor.CENTER, getLabelAngle() + Math.PI / 2.0, TextAnchor.CENTER);
    state.cursorRight(insets.getLeft() + rotatedBounds.getWidth() + insets.getRight());
    return new Rectangle2D.Double(startX - rotatedBounds.getWidth() / 2.0, startY - rotatedBounds.getHeight() / 2.0,
            rotatedBounds.getWidth(), rotatedBounds.getHeight());
}