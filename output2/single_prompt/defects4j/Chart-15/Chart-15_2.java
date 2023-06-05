## Fixed Function 1
public PiePlotState initialise(Graphics2D g2, Rectangle2D plotArea, PiePlot plot, int index, PlotRenderingInfo info) {
    PiePlotState state = new PiePlotState(info);
    state.setPassesRequired(2);
    state.setTotal(DatasetUtilities.calculatePieDatasetTotal(plot.getDataset()));
    state.setLatestAngle(plot.getStartAngle());
    state.setExploded(null); //fixes issue where state is not reset between calls to initialise()
    return state;
}

## Fixed Function 2
public double getMaximumExplodePercent() {
    double result = 0.0;
    Iterator iterator = this.dataset.getKeys().iterator();
    while (iterator.hasNext()) {
        Comparable key = iterator.next().toString();
        Number explode = (Number) this.explodePercentages.get(key);
        if (explode != null) {
            result = Math.max(result, explode.doubleValue());
        }
    }
    return result;
} 

*Note: The dataset.getKeys() method returns an Enumeration and not an Iterator. Casting to Iterator may cause issues.*