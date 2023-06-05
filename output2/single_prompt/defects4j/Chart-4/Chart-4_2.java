public Range getDataRange(ValueAxis axis) {
    Range result = null;
    List<XYDataset> mappedDatasets = new ArrayList<>(); //added type parameter for List
    List<XYAnnotation> includedAnnotations = new ArrayList<>(); //changed type of List
    boolean isDomainAxis = true;
    // is it a domain axis?
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        mappedDatasets.addAll(getDatasetsMappedToDomainAxis(domainIndex)); //no need to create Integer object
        if (domainIndex == 0) {
            // grab the plot's annotations
            for (Object annotation : this.annotations) { //used enhanced for loop
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
        mappedDatasets.addAll(getDatasetsMappedToRangeAxis(rangeIndex)); //no need to create Integer object
        if (rangeIndex == 0) {
            for (Object annotation : this.annotations) { //used enhanced for loop
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add((XYAnnotation) annotation);
                }
            }
        }
    }
    Iterator<XYDataset> iterator = mappedDatasets.iterator(); //added type parameter
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
            Collection<XYAnnotation> c = r.getAnnotations(); //added type parameter
            Iterator<XYAnnotation> i = c.iterator(); //added type parameter
            while (i.hasNext()) {
                XYAnnotation a = i.next();
                if (a instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(a);
                }
            }
        }
    }
    Iterator<XYAnnotation> it = includedAnnotations.iterator(); //added type parameter
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