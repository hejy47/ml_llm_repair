## Fixed Function 1
public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset;
    PiePlot piePlot = new PiePlot(null);
    this.pieChart = new JFreeChart(piePlot);
    this.pieChart.removeLegend();
    this.dataExtractOrder = TableOrder.BY_COLUMN;
    this.pieChart.setBackgroundPaint(null);
    TextTitle seriesTitle = new TextTitle("Series Title", new Font("SansSerif", Font.BOLD, 12));
    seriesTitle.setPosition(RectangleEdge.BOTTOM);
    this.pieChart.setTitle(seriesTitle);
    this.aggregatedItemsKey = "Other";
    this.aggregatedItemsPaint = Color.lightGray;
    this.sectionPaints = new HashMap<>();
    // Add sectionPaints for each category in dataset
    for (int i = 0; i < dataset.getRowCount(); i++) {
        Comparable key = dataset.getRowKey(i);
        Paint paint = new GradientPaint(0f, 0f, createRandomColor(), 0f, 0f, createRandomColor());
        this.sectionPaints.put(key, paint);
    }
}

// Create a random color
private Color createRandomColor() {
    Random random = new Random();
    return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
}