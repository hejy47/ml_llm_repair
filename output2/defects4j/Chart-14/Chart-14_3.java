## Fixed Function 1
public boolean removeDomainMarker(int index, Marker marker, Layer layer, boolean notify) {
    ArrayList<Marker> markers;
    if (layer == Layer.FOREGROUND) {
        markers = (ArrayList<Marker>) this.foregroundDomainMarkers.get(new Integer(index));
    } else {
        markers = (ArrayList<Marker>) this.backgroundDomainMarkers.get(new Integer(index));
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
    ArrayList<Marker> markers;
    if (layer == Layer.FOREGROUND) {
        markers = (ArrayList<Marker>) this.foregroundRangeMarkers.get(new Integer(index));
    } else {
        markers = (ArrayList<Marker>) this.backgroundRangeMarkers.get(new Integer(index));
    }
    boolean removed = markers.remove(marker);
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}

## Fixed Function 3
public boolean removeDomainMarker(int index, Marker marker, Layer layer, boolean notify) {
    ArrayList<Marker> markers;
    if (layer == Layer.FOREGROUND) {
        markers = (ArrayList<Marker>) this.foregroundDomainMarkers.get(new Integer(index));
    } else {
        markers = (ArrayList<Marker>) this.backgroundDomainMarkers.get(new Integer(index));
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
    ArrayList<Marker> markers;
    if (layer == Layer.FOREGROUND) {
        markers = (ArrayList<Marker>) this.foregroundRangeMarkers.get(new Integer(index));
    } else {
        markers = (ArrayList<Marker>) this.backgroundRangeMarkers.get(new Integer(index));
    }
    boolean removed = markers.remove(marker);
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}