## Fixed Function 1
public boolean removeDomainMarker(int index, Marker marker, Layer layer, boolean notify) {
    if (marker == null) {
        throw new IllegalArgumentException("Null 'marker' argument.");
    }
    ArrayList markers;
    if (layer == Layer.FOREGROUND) {
        markers = this.foregroundDomainMarkers.get(new Integer(index));
    } else {
        markers = this.backgroundDomainMarkers.get(new Integer(index));
    }
    boolean removed = markers.remove(marker);
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}

## Fixed Function 2
public boolean removeRangeMarker(int index, Marker marker, Layer layer, boolean notify) {
    if (marker == null) {
        throw new IllegalArgumentException("Null 'marker' argument.");
    }
    ArrayList markers;
    if (layer == Layer.FOREGROUND) {
        markers = this.foregroundRangeMarkers.get(new Integer(index));
    } else {
        markers = this.backgroundRangeMarkers.get(new Integer(index));
    }
    boolean removed = markers.remove(marker);
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}

## Fixed Function 3
public boolean removeDomainMarker(int index, Marker marker, Layer layer, boolean notify) {
    if (marker == null) {
        throw new IllegalArgumentException("Null 'marker' argument.");
    }
    ArrayList markers;
    if (layer == Layer.FOREGROUND) {
        markers = this.foregroundDomainMarkers.get(new Integer(index));
    } else {
        markers = this.backgroundDomainMarkers.get(new Integer(index));
    }
    boolean removed = markers.remove(marker);
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}

## Fixed Function 4
public boolean removeRangeMarker(int index, Marker marker, Layer layer, boolean notify) {
    if (marker == null) {
        throw new IllegalArgumentException("Null 'marker' argument.");
    }
    ArrayList markers;
    if (layer == Layer.FOREGROUND) {
        markers = this.foregroundRangeMarkers.get(new Integer(index));
    } else {
        markers = this.backgroundRangeMarkers.get(new Integer(index));
    }
    boolean removed = markers.remove(marker);
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}