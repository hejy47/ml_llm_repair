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
                Object a = iterator.next();
                if (a instanceof XYAnnotation) {
                    XYAnnotation annotation = (XYAnnotation) a;
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(annotation);
                    }
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
                Object a = iterator.next();
                if (a instanceof XYAnnotation) {
                    XYAnnotation annotation = (XYAnnotation) a;
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(annotation);
                    }
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
                    result = Range.combine(result, r.findDomainBounds(d));
                } else {
                    result = Range.combine(result, DatasetUtilities.findDomainBounds(d));
                }
            } else {
                if (r != null) {
                    result = Range.combine(result, r.findRangeBounds(d));
                } else {
                    result = Range.combine(result, DatasetUtilities.findRangeBounds(d));
                }
            }
            Collection c = r.getAnnotations();
            Iterator i = c.iterator();
            while (i.hasNext()) {
                Object a = i.next();
                if (a instanceof XYAnnotation) {
                    XYAnnotation annotation = (XYAnnotation) a;
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(annotation);
                    }
                }
            }
        }
    }
    Iterator it = includedAnnotations.iterator();
    while (it.hasNext()) {
        Object a = it.next();
        if (a instanceof XYAnnotationBoundsInfo) {
            XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) a;
            if (xyabi.getIncludeInDataBounds()) {
                if (isDomainAxis) {
                    result = Range.combine(result, xyabi.getXRange());
                } else {
                    result = Range.combine(result, xyabi.getYRange());
                }
            }
        }
    }
    return result;
}