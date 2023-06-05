## Fixed Function 1
public boolean removeDomainMarker(int index, Marker marker, Layer layer, boolean notify) {
    ArrayList markers;
    if (layer == Layer.FOREGROUND) {
        markers = (ArrayList) this.foregroundDomainMarkers.get(Integer.valueOf(index));
    } else {
        markers = (ArrayList) this.backgroundDomainMarkers.get(Integer.valueOf(index));
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
        markers = (ArrayList) this.foregroundRangeMarkers.get(Integer.valueOf(index));
    } else {
        markers = (ArrayList) this.backgroundRangeMarkers.get(Integer.valueOf(index));
    }
    boolean removed = markers.remove(marker);
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}

## Fixed Function 3
public boolean removeDomainMarker(int index, Marker marker, Layer layer, boolean notify) {
    ArrayList markers;
    if (layer == Layer.FOREGROUND) {
        markers = (ArrayList) this.foregroundDomainMarkers.get(Integer.valueOf(index));
    } else {
        markers = (ArrayList) this.backgroundDomainMarkers.get(Integer.valueOf(index));
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
        markers = (ArrayList) this.foregroundRangeMarkers.get(Integer.valueOf(index));
    } else {
        markers = (ArrayList) this.backgroundRangeMarkers.get(Integer.valueOf(index));
    }
    boolean removed = markers.remove(marker);
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}