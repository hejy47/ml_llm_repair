## Fixed Function 1
public Range getDataRange(ValueAxis axis) {
    Range result = null;
    List mappedDatasets = new ArrayList();
    List includedAnnotations = new ArrayList();
    boolean isDomainAxis = true;
    // is it a domain axis?
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        mappedDatasets.addAll(getDatasetsMappedToDomainAxis(new Integer(domainIndex)));
        if (domainIndex == 0) {
            // grab the plot's annotations
            Iterator iterator = this.annotations.iterator();
            while (iterator.hasNext()) {
                XYAnnotation annotation = (XYAnnotation) iterator.next();
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(annotation);
                }
            }
        }
    }
    // or is it a range axis?
    int rangeIndex = getRangeAxisIndex(axis);
    if (rangeIndex >= 0) {
        isDomainAxis = false;
        mappedDatasets.addAll(getDatasetsMappedToRangeAxis(new Integer(rangeIndex)));
        if (rangeIndex == 0) {
            Iterator iterator = this.annotations.iterator();
            while (iterator.hasNext()) {
                XYAnnotation annotation = (XYAnnotation) iterator.next();
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(annotation);
                }
            }
        }
    }
    // iterate through the datasets that map to the axis and get the union
    // of the ranges.
    Iterator iterator = mappedDatasets.iterator();
    while (iterator.hasNext()) {
        XYDataset d = (XYDataset) iterator.next();
        if (d != null) {
            XYItemRenderer r = getRendererForDataset(d);
            if (isDomainAxis) {
                if (r != null) {
                    Range domainBounds = r.findDomainBounds(d);
                    if (domainBounds != null) { // fix: check for null
                        result = Range.combine(result, domainBounds);
                    }
                } else {
                    result = Range.combine(result, DatasetUtilities.findDomainBounds(d));
                }
            } else {
                if (r != null) {
                    Range rangeBounds = r.findRangeBounds(d);
                    if (rangeBounds != null) { // fix: check for null
                        result = Range.combine(result, rangeBounds);
                    }
                } else {
                    result = Range.combine(result, DatasetUtilities.findRangeBounds(d));
                }
            }
            Collection c = r.getAnnotations();
            if (c != null) { // fix: check for null
                Iterator i = c.iterator();
                while (i.hasNext()) {
                    XYAnnotation a = (XYAnnotation) i.next();
                    if (a instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(a);
                    }
                }
            }
        }
    }
    Iterator it = includedAnnotations.iterator();
    while (it.hasNext()) {
        XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) it.next();
        if (xyabi.getIncludeInDataBounds()) {
            if (isDomainAxis) {
                Range xRange = xyabi.getXRange();
                if (xRange != null) { // fix: check for null
                    result = Range.combine(result, xRange);
                }
            } else {
                Range yRange = xyabi.getYRange();
                if (yRange != null) { // fix: check for null
                    result = Range.combine(result, yRange);
                }
            }
        }
    }
    return result;
}