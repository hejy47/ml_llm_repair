## Fixed Function 1
public ElitisticListPopulation(final List<Chromosome> chromosomes, final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
    List<Chromosome> sortedChromosomes = new ArrayList<>(chromosomes);
    sortedChromosomes.sort(Collections.reverseOrder());
    int eliteCount = (int) (populationLimit * elitismRate);
    for (int i = 0; i < eliteCount; i++) {
        addChromosome(sortedChromosomes.get(i));
    }
}

## Fixed Function 2
public ElitisticListPopulation(final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
}