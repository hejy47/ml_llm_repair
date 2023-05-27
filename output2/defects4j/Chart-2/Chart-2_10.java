## Fixed Function 1
public static Range iterateDomainBounds(XYDataset dataset, boolean includeInterval) {
    if (dataset == null) {
        throw new IllegalArgumentException("Null 'dataset' argument.");
    }
    double minimum = Double.POSITIVE_INFINITY;
    double maximum = Double.NEGATIVE_INFINITY;
    int seriesCount = dataset.getSeriesCount();
    double lvalue;
    double uvalue;
    // Fix the for loop to iterate over all items in all series
    for (int series = 0; series < seriesCount; series++) {
        int itemCount = dataset.getItemCount(series);
        for (int item = 0; item < itemCount; item++) {
            if (includeInterval && dataset instanceof IntervalXYDataset) {
                IntervalXYDataset intervalXYData = (IntervalXYDataset) dataset;
                lvalue = intervalXYData.getStartXValue(series, item);
                uvalue = intervalXYData.getEndXValue(series, item);
            } else {
                lvalue = dataset.getXValue(series, item);
                uvalue = lvalue;
            }
            if (!Double.isNaN(lvalue)) {
                minimum = Math.min(minimum, lvalue);
            }
            if (!Double.isNaN(uvalue)) {
                maximum = Math.max(maximum, uvalue);
            }
        }
    }
    if (minimum > maximum) {
        return null;
    } else {
        return new Range(minimum, maximum);
    }
}

## Fixed Function 2
public static Range iterateRangeBounds(XYDataset dataset, boolean includeInterval) {
    double minimum = Double.POSITIVE_INFINITY;
    double maximum = Double.NEGATIVE_INFINITY;
    int seriesCount = dataset.getSeriesCount();
    // handle three cases by dataset type
    for (int series = 0; series < seriesCount; series++) {
        int itemCount = dataset.getItemCount(series);
        for (int item = 0; item < itemCount; item++) {
            if (includeInterval && dataset instanceof IntervalXYDataset) {
                IntervalXYDataset ixyd = (IntervalXYDataset) dataset;
                double lvalue = ixyd.getStartYValue(series, item);
                double uvalue = ixyd.getEndYValue(series, item);
                if (!Double.isNaN(lvalue)) {
                    minimum = Math.min(minimum, lvalue);
                }
                if (!Double.isNaN(uvalue)) {
                    maximum = Math.max(maximum, uvalue);
                }
            } else if (includeInterval && dataset instanceof OHLCDataset) {
                OHLCDataset ohlc = (OHLCDataset) dataset;
                double lvalue = ohlc.getLowValue(series, item);
                double uvalue = ohlc.getHighValue(series, item);
                if (!Double.isNaN(lvalue)) {
                    minimum = Math.min(minimum, lvalue);
                }
                if (!Double.isNaN(uvalue)) {
                    maximum = Math.max(maximum, uvalue);
                }
            } else {
                double value = dataset.getYValue(series, item);
                if (!Double.isNaN(value)) {
                    minimum = Math.min(minimum, value);
                    maximum = Math.max(maximum, value);
                }
            }
        }
    }
    if (minimum == Double.POSITIVE_INFINITY) {
        return null;
    } else {
        return new Range(minimum, maximum);
    }
}