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
    double labelx, labely;
    float w = (float) labelBounds.getWidth();
    float h = (float) labelBounds.getHeight();

    if (edge == RectangleEdge.TOP) {
        labelx = (float) dataArea.getCenterX();
        labely = (float) (state.getCursor() - insets.getBottom() - h / 2.0);
        hotspot = new Rectangle2D.Double(labelx - w / 2.0, labely - h / 2.0, w, h);
        TextUtilities.drawRotatedString(label, g2, (float) hotspot.getCenterX(), (float) hotspot.getCenterY(), TextAnchor.CENTER, getLabelAngle(), TextAnchor.CENTER);
        state.cursorUp(insets.getTop() + hotspot.getHeight() + insets.getBottom());
    } else if (edge == RectangleEdge.BOTTOM) {
        labelx = (float) dataArea.getCenterX();
        labely = (float) (state.getCursor() + insets.getTop() + h / 2.0);
        hotspot = new Rectangle2D.Double(labelx - w / 2.0, labely - h / 2.0, w, h);
        TextUtilities.drawRotatedString(label, g2, (float) hotspot.getCenterX(), (float) hotspot.getCenterY(), TextAnchor.CENTER, getLabelAngle(), TextAnchor.CENTER);
        state.cursorDown(insets.getTop() + hotspot.getHeight() + insets.getBottom());
    } else if (edge == RectangleEdge.LEFT) {
        labelx = (float) (state.getCursor() - insets.getRight() - w / 2.0);
        labely = (float) dataArea.getCenterY();
        hotspot = new Rectangle2D.Double(labelx - w / 2.0, labely - h / 2.0, w, h);
        TextUtilities.drawRotatedString(label, g2, (float) hotspot.getCenterX(), (float) hotspot.getCenterY(), TextAnchor.CENTER, getLabelAngle() - Math.PI / 2.0, TextAnchor.CENTER);
        state.cursorLeft(insets.getLeft() + hotspot.getWidth() + insets.getRight());
    } else if (edge == RectangleEdge.RIGHT) {
        labelx = (float) (state.getCursor() + insets.getLeft() + w / 2.0);
        labely = (float) (dataArea.getY() + dataArea.getHeight() / 2.0);
        hotspot = new Rectangle2D.Double(labelx - w / 2.0, labely - h / 2.0, w, h);
        TextUtilities.drawRotatedString(label, g2, (float) hotspot.getCenterX(), (float) hotspot.getCenterY(), TextAnchor.CENTER, getLabelAngle() + Math.PI / 2.0, TextAnchor.CENTER);
        state.cursorRight(insets.getLeft() + hotspot.getWidth() + insets.getRight());
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