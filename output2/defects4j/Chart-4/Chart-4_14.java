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
            for (Object annotation : annotations) {
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
            for (Object annotation : annotations) {
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add((XYAnnotation) annotation);
                }
            }
        }
    }
    // iterate through the datasets that map to the axis and get the union
    // of the ranges.
    for (XYDataset d : mappedDatasets) {
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
            Collection<XYAnnotation> c = r.getAnnotations();
            for (XYAnnotation a : c) {
                if (a instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(a);
                }
            }
        }
    }
    for (XYAnnotation annotation : includedAnnotations) {
        if (annotation instanceof XYAnnotationBoundsInfo) {
            XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) annotation;
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