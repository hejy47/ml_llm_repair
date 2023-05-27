## Fixed Function 1
public boolean removeDomainMarker(int index, Marker marker, Layer layer, boolean notify) {
    List<Marker> markers;
    if (layer == Layer.FOREGROUND) {
        markers = this.foregroundDomainMarkers.get(Integer.valueOf(index));
    } else {
        markers = this.backgroundDomainMarkers.get(Integer.valueOf(index));
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
    List<Marker> markers;
    if (layer == Layer.FOREGROUND) {
        markers = this.foregroundRangeMarkers.get(Integer.valueOf(index));
    } else {
        markers = this.backgroundRangeMarkers.get(Integer.valueOf(index));
    }
    boolean removed = markers.remove(marker);
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}

## Fixed Function 3
public boolean removeDomainMarker(int index, Marker marker, Layer layer, boolean notify) {
    List<Marker> markers;
    if (layer == Layer.FOREGROUND) {
        markers = this.foregroundDomainMarkers.get(Integer.valueOf(index));
    } else {
        markers = this.backgroundDomainMarkers.get(Integer.valueOf(index));
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
    List<Marker> markers;
    if (layer == Layer.FOREGROUND) {
        markers = this.foregroundRangeMarkers.get(Integer.valueOf(index));
    } else {
        markers = this.backgroundRangeMarkers.get(Integer.valueOf(index));
    }
    boolean removed = markers.remove(marker);
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}