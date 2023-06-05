## Fixed Function 1
public ElitisticListPopulation(final List<Chromosome> chromosomes, final int populationLimit, final double elitismRate) {
    super(populationLimit);
    List<Chromosome> sortedList = new ArrayList<>(chromosomes);
    sortedList.sort(Collections.reverseOrder()); // reverse order
    int eliteCount = (int) Math.ceil(populationLimit * elitismRate);
    List<Chromosome> elites = sortedList.subList(0, eliteCount);
    addAll(elites);
}

## Fixed Function 2
public ElitisticListPopulation(final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
}