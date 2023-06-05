## Fixed Function 1
public boolean removeDomainMarker(int index, Marker marker, Layer layer, boolean notify) {
    boolean removed = false;
    if (layer == Layer.FOREGROUND) {
        List<Marker> markers = this.foregroundDomainMarkers.get(index);  // removed unnecessary casting
        if (markers != null) {
            removed = markers.remove(marker);  // check if marker is found and removed
        }
    } else {
        List<Marker> markers = this.backgroundDomainMarkers.get(index);  // removed unnecessary casting
        if (markers != null) {
            removed = markers.remove(marker);  // check if marker is found and removed
        }
    }
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}

## Fixed Function 2
public boolean removeRangeMarker(int index, Marker marker, Layer layer, boolean notify) {
    boolean removed = false;
    if (marker == null) {
        throw new IllegalArgumentException("Null 'marker' argument.");
    }
    if (layer == Layer.FOREGROUND) {
        List<Marker> markers = this.foregroundRangeMarkers.get(index);  // removed unnecessary casting
        if (markers != null) {
            removed = markers.remove(marker);  // check if marker is found and removed
        }
    } else {
        List<Marker> markers = this.backgroundRangeMarkers.get(index);  // removed unnecessary casting
        if (markers != null) {
            removed = markers.remove(marker);  // check if marker is found and removed
        }
    }
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}

## Fixed Function 3
public boolean removeDomainMarker(int index, Marker marker, Layer layer, boolean notify) {
    boolean removed = false;
    if (layer == Layer.FOREGROUND) {
        List<Marker> markers = this.foregroundDomainMarkers.get(index);  // removed unnecessary casting
        if (markers != null) {
            removed = markers.remove(marker);  // check if marker is found and removed
        }
    } else {
        List<Marker> markers = this.backgroundDomainMarkers.get(index);  // removed unnecessary casting
        if (markers != null) {
            removed = markers.remove(marker);  // check if marker is found and removed
        }
    }
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}

## Fixed Function 4
public boolean removeRangeMarker(int index, Marker marker, Layer layer, boolean notify) {
    boolean removed = false;
    if (marker == null) {
        throw new IllegalArgumentException("Null 'marker' argument.");
    }
    if (layer == Layer.FOREGROUND) {
        List<Marker> markers = this.foregroundRangeMarkers.get(index);  // removed unnecessary casting
        if (markers != null) {
            removed = markers.remove(marker);  // check if marker is found and removed
        }
    } else {
        List<Marker> markers = this.backgroundRangeMarkers.get(index);  // removed unnecessary casting
        if (markers != null) {
            removed = markers.remove(marker);  // check if marker is found and removed
        }
    }
    if (removed && notify) {
        fireChangeEvent();
    }
    return removed;
}