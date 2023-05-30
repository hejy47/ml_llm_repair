## Fixed Function 1
public ElitisticListPopulation(final List<Chromosome> chromosomes, final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
    List<Chromosome> sortedChr = new ArrayList<>(chromosomes);
    Collections.sort(sortedChr, Collections.reverseOrder());
    int eliteCount = (int)(populationLimit * elitismRate);
    for (int i = 0; i < eliteCount; i++) {
        addChromosome(sortedChr.get(i));
    }
}

## Fixed Function 2
public ElitisticListPopulation(final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
}