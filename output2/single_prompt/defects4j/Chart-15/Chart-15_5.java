## Fixed Function 1
public PiePlotState initialize(Graphics2D g2, Rectangle2D plotArea, PiePlot plot, Integer index, PlotRenderingInfo info) {
    PiePlotState state = new PiePlotState(info);
    state.setPassesRequired(2);
    state.setTotal(DatasetUtilities.calculatePieDatasetTotal(plot.getDataset()));
    state.setLatestAngle(plot.getStartAngle());
    state.setExploded(new boolean[plot.getDataset().getItemCount()]);
    return state;
}

## Fixed Function 2
public double getMaximumExplodePercent() {
    double result = 0.0;
    Iterator iterator = this.getPieDataset().getKeys().iterator();
    while (iterator.hasNext()) {
        Comparable key = (Comparable) iterator.next();
        Number explode = (Number) this.explodePercentages.get(key);
        if (explode != null) {
            result = Math.max(result, explode.doubleValue());
        }
    }
    return result;
}