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
    FontMetrics fm = g2.getFontMetrics(font); // Changed to get the font metrics of the current font
    Rectangle2D labelBounds = TextUtilities.getTextBounds(label, g2, fm);
    Shape hotspot = null;
    if (edge == RectangleEdge.TOP) {
        AffineTransform t = AffineTransform.getRotateInstance(getLabelAngle(), labelBounds.getCenterX(), labelBounds.getCenterY());
        Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
        labelBounds = rotatedLabelBounds.getBounds2D();
        double w = labelBounds.getWidth(); // Using doubles instead of floats
        double h = labelBounds.getHeight(); // Using doubles instead of floats
        double labelx = dataArea.getCenterX(); // Using doubles instead of floats
        double labely = state.getCursor() - insets.getBottom() - h / 2.0; // Using doubles instead of floats
        TextUtilities.drawRotatedString(label, g2, labelx, labely, TextAnchor.CENTER, getLabelAngle(), TextAnchor.CENTER);
        hotspot = new Rectangle2D.Double(labelx - w / 2.0, labely - h / 2.0, w, h); // Using doubles instead of floats
        state.cursorUp(insets.getTop() + labelBounds.getHeight() + insets.getBottom());
    } else if (edge == RectangleEdge.BOTTOM) {
        AffineTransform t = AffineTransform.getRotateInstance(getLabelAngle(), labelBounds.getCenterX(), labelBounds.getCenterY());
        Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
        labelBounds = rotatedLabelBounds.getBounds2D();
        double w = labelBounds.getWidth(); // Using doubles instead of floats
        double h = labelBounds.getHeight(); // Using doubles instead of floats
        double labelx = dataArea.getCenterX(); // Using doubles instead of floats
        double labely = state.getCursor() + insets.getTop() + h / 2.0; // Using doubles instead of floats
        TextUtilities.drawRotatedString(label, g2, labelx, labely, TextAnchor.CENTER, getLabelAngle(), TextAnchor.CENTER);
        hotspot = new Rectangle2D.Double(labelx - w / 2.0, labely - h / 2.0, w, h); // Using doubles instead of floats
        state.cursorDown(insets.getTop() + labelBounds.getHeight() + insets.getBottom());
    } else if (edge == RectangleEdge.LEFT) {
        AffineTransform t = AffineTransform.getRotateInstance(getLabelAngle() - Math.PI / 2.0, labelBounds.getCenterX(), labelBounds.getCenterY());
        Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
        labelBounds = rotatedLabelBounds.getBounds2D();
        double w = labelBounds.getWidth(); // Using doubles instead of floats
        double h = labelBounds.getHeight(); // Using doubles instead of floats
        double labelx = state.getCursor() - insets.getRight() - w / 2.0; // Using doubles instead of floats
        double labely = dataArea.getCenterY(); // Using doubles instead of floats
        TextUtilities.drawRotatedString(label, g2, labelx, labely, TextAnchor.CENTER, getLabelAngle() - Math.PI / 2.0, TextAnchor.CENTER);
        hotspot = new Rectangle2D.Double(labelx - w / 2.0, labely - h / 2.0, w, h); // Using doubles instead of floats
        state.cursorLeft(insets.getLeft() + labelBounds.getWidth() + insets.getRight());
    } else if (edge == RectangleEdge.RIGHT) {
        AffineTransform t = AffineTransform.getRotateInstance(getLabelAngle() + Math.PI / 2.0, labelBounds.getCenterX(), labelBounds.getCenterY());
        Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
        labelBounds = rotatedLabelBounds.getBounds2D();
        double w = labelBounds.getWidth(); // Using doubles instead of floats
        double h = labelBounds.getHeight(); // Using doubles instead of floats
        double labelx = state.getCursor() + insets.getLeft() + w / 2.0; // Using doubles instead of floats
        double labely = dataArea.getCenterY(); // Using doubles instead of floats
        TextUtilities.drawRotatedString(label, g2, labelx, labely, TextAnchor.CENTER, getLabelAngle() + Math.PI / 2.0, TextAnchor.CENTER);
        hotspot = new Rectangle2D.Double(labelx - w / 2.0, labely - h / 2.0, w, h); // Using doubles instead of floats
        state.cursorRight(insets.getLeft() + labelBounds.getWidth() + insets.getRight());
    }
    if (plotState != null && hotspot != null) {
        ChartRenderingInfo owner = plotState.getOwner();
        EntityCollection entities = owner.getEntityCollection();
        if (entities != null) {
            entities.add(new AxisLabelEntity(this, hotspot, this.labelToolTip, this.labelURL));
        }
    }
    return state;
}