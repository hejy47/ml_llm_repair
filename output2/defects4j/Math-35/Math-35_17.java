## Fixed Function 1
public ElitisticListPopulation(final List<Chromosome> chromosomes, final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
    List<Chromosome> sortedChromosomes = new ArrayList<>(chromosomes);
    sortedChromosomes.sort(Comparator.comparingDouble(Chromosome::getFitnessValue));
    int eliteCount = (int) Math.ceil(elitismRate * populationLimit);
    for (int i = 0; i < eliteCount; i++) {
        if (i >= sortedChromosomes.size()) {
            break;
        }
        addChromosome(sortedChromosomes.get(i));
    }
}

## Fixed Function 2
public ElitisticListPopulation(final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
}