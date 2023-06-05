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
                Object obj = iterator.next();
                if (obj instanceof XYAnnotation) {
                    XYAnnotation annotation = (XYAnnotation) obj;
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
                Object obj = iterator.next();
                if (obj instanceof XYAnnotation) {
                    XYAnnotation annotation = (XYAnnotation) obj;
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(annotation);
                    }
                }
            }
        }
    }
    // iterate through the datasets that map to the axis and get the union
    // of the ranges.
    Iterator datasetIterator = mappedDatasets.iterator();
    while (datasetIterator.hasNext()) {
        Object obj = datasetIterator.next();
        if (obj instanceof XYDataset) {
            XYDataset dataset = (XYDataset) obj;
            if (dataset != null) {
                XYItemRenderer renderer = getRendererForDataset(dataset);
                if (isDomainAxis) {
                    if (renderer != null) {
                        result = Range.combine(result, renderer.findDomainBounds(dataset));
                    } else {
                        result = Range.combine(result, DatasetUtilities.findDomainBounds(dataset));
                    }
                } else {
                    if (renderer != null) {
                        result = Range.combine(result, renderer.findRangeBounds(dataset));
                    } else {
                        result = Range.combine(result, DatasetUtilities.findRangeBounds(dataset));
                    }
                    Collection annotationsCollection = renderer.getAnnotations();
                    Iterator annotationIterator = annotationsCollection.iterator();
                    while (annotationIterator.hasNext()) {
                        Object annotationObj = annotationIterator.next();
                        if (annotationObj instanceof XYAnnotation) {
                            XYAnnotation annotation = (XYAnnotation) annotationObj;
                            if (annotation instanceof XYAnnotationBoundsInfo) {
                                includedAnnotations.add(annotation);
                            }
                        }
                    }
                }
            }
        }
    }
    Iterator annotationIterator = includedAnnotations.iterator();
    while (annotationIterator.hasNext()) {
        Object annotationObj = annotationIterator.next();
        if (annotationObj instanceof XYAnnotationBoundsInfo) {
            XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) annotationObj;
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