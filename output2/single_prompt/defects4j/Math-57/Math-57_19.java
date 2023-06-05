private static <T extends Clusterable<T>> List<Cluster<T>> chooseInitialCenters(final Collection<T> points, final int k, final Random random) {
    final List<T> pointSet = new ArrayList<>(points);
    final List<Cluster<T>> resultSet = new ArrayList<>(k);
    // Choose one center uniformly at random from among the data points.
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<>(firstPoint));
    final double[] dx2 = new double[pointSet.size()];

    while (resultSet.size() < k) {
        // For each data point x, compute D(x), the distance between x and
        // the nearest center that has already been chosen.
        int sum = 0;
        for (int i = 0; i < pointSet.size(); i++) {
            final T p = pointSet.get(i);
            final double d = distanceToNearestCluster(p, resultSet);
            sum += d * d;
            dx2[i] = sum;
        }
        // Add one new data point as a center. Each point x is chosen with
        // probability proportional to D(x)2
        final double r = random.nextDouble() * sum;
        for (int i = 0; i < dx2.length; i++) {
            if (dx2[i] >= r) {
                final T p = pointSet.remove(i);
                resultSet.add(new Cluster<>(p));
                break;
            }
        }
    }
    return resultSet;
}

// returns the distance between a point and its nearest cluster
private static <T extends Clusterable<T>> double distanceToNearestCluster(final T point, final List<Cluster<T>> clusters) {
    double minDistance = Double.POSITIVE_INFINITY;
    for (final Cluster<T> cluster : clusters) {
        final double distance = point.distanceFrom(cluster.getCenter());
        minDistance = Math.min(minDistance, distance);
    }
    return minDistance;
}