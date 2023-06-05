## Fixed Function 1
private static <T extends Clusterable<T>> List<Cluster<T>> chooseInitialCenters(final Collection<T> points, final int k, final Random random) {
    final List<T> pointSet = new ArrayList<T>(points);
    final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();
    // Choose one center uniformly at random from among the data points.
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<T>(firstPoint));
    final double[] dx2 = new double[pointSet.size()];
    // Fixed: Moved dx2 array initialization before the while loop
    for (int i = 0; i < dx2.length; i++) {
        dx2[i] = Double.POSITIVE_INFINITY;
    }
    while (resultSet.size() < k) {
        // For each data point x, compute D(x), the distance between x and
        // the nearest center that has already been chosen.
        int sum = 0;
        // Fixed: Initialize minDist to infinite and update it inside the for loop
        double minDist = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pointSet.size(); i++) {
            final T p = pointSet.get(i);
            final Cluster<T> nearest = getNearestCluster(resultSet, p);
            final double d = p.distanceFrom(nearest.getCenter());
            // Fixed: Update the minimum distance with the current distance
            if (d < minDist) {
                minDist = d;
            }
            // Fixed: Update dx2[i] with the new minimum distance
            dx2[i] = minDist * minDist;
            sum += dx2[i];
        }
        // Add one new data point as a center. Each point x is chosen with
        // probability proportional to D(x)2
        final double r = random.nextDouble() * sum;
        for (int i = 0; i < dx2.length; i++) {
            if (dx2[i] >= r) {
                final T p = pointSet.remove(i);
                resultSet.add(new Cluster<T>(p));
                break;
            }
        }
    }
    return resultSet;
}