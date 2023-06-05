## Fixed Function 1
public PiePlotState initialise(Graphics2D g2, Rectangle2D plotArea, PiePlot plot, Integer index, PlotRenderingInfo info) {
    PiePlotState state = new PiePlotState(info);
    state.setPassesRequired(2);
    Dataset dataset = plot.getDataset(); // add this line
    state.setTotal(DatasetUtilities.calculatePieDatasetTotal(dataset));
    state.setLatestAngle(plot.getStartAngle());
    return state;
}

## Fixed Function 2
public double getMaximumExplodePercent() {
    double result = 0.0;
    Iterator iterator = this.dataset.getKeys().iterator();
    while (iterator.hasNext()) {
        Comparable key = (Comparable) iterator.next();
        Number explode = (Number) this.explodePercentages.get(key);
        if (explode != null) {
            result = Math.max(result, explode.doubleValue());
        }
    }
    return result;
}