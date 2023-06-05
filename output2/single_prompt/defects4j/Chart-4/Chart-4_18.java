public Range getDataRange(ValueAxis axis) {
    Range result = null;
    List<XYDataset> mappedDatasets = new ArrayList<>();
    List<XYAnnotation> includedAnnotations = new ArrayList<>();
    boolean isDomainAxis = true;
    // is it a domain axis?
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        mappedDatasets.addAll(getDatasetsMappedToDomainAxis(domainIndex));
        if (domainIndex == 0) {
            // grab the plot's annotations
            for (Object annotation : this.annotations) {
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add((XYAnnotation) annotation);
                }
            }
        }
    }
    // or is it a range axis?
    int rangeIndex = getRangeAxisIndex(axis);
    if (rangeIndex >= 0) {
        isDomainAxis = false;
        mappedDatasets.addAll(getDatasetsMappedToRangeAxis(rangeIndex));
        if (rangeIndex == 0) {
            for (Object annotation : this.annotations) {
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add((XYAnnotation) annotation);
                }
            }
        }
    }
    // iterate through the datasets that map to the axis and get the union
    // of the ranges.
    Iterator<XYDataset> iterator = mappedDatasets.iterator();
    while (iterator.hasNext()) {
        XYDataset d = iterator.next();
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
            Collection<? extends XYAnnotation> c = r.getAnnotations();
            Iterator<? extends XYAnnotation> i = c.iterator();
            while (i.hasNext()) {
                XYAnnotation a = i.next();
                if (a instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(a);
                }
            }
        }
    }
    Iterator<XYAnnotation> it = includedAnnotations.iterator();
    while (it.hasNext()) {
        XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) it.next();
        if (xyabi.getIncludeInDataBounds()) {
            if (isDomainAxis) {
                result = Range.combine(result, xyabi.getXRange());
            } else {
                result = Range.combine(result, xyabi.getYRange());
            }
        }
    }
    return result;
}