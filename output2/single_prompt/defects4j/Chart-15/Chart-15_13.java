## Fixed Function 1
public PiePlotState initialise(Graphics2D g2, Rectangle2D plotArea, PiePlot plot, int index, PlotRenderingInfo info) {
    PiePlotState state = new PiePlotState(info);
    state.setPassesRequired(2);
    state.setTotal(DatasetUtilities.calculatePieDatasetTotal(plot.getDataset()));
    state.setLatestAngle(plot.getStartAngle());
    state.setExplodedPieArea(new ArrayList());
    return state;
}

## Fixed Function 2
public double getMaximumExplodePercent() {
    double result = 0.0;
    Iterator iterator = this.explodePercentages.entrySet().iterator();
    while (iterator.hasNext()) {
        Map.Entry entry = (Map.Entry) iterator.next();
        Number explode = (Number) entry.getValue();
        if (explode != null) {
            result = Math.max(result, explode.doubleValue());
        }
    }
    return result;
}