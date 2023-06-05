## Fixed Function 1
public ElitisticListPopulation(final List<Chromosome> chromosomes, final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
    List<Chromosome> sorted = new ArrayList<>(chromosomes);
    sorted.sort(Collections.reverseOrder());
    for (int i = 0, stop = (int) FastMath.ceil(populationLimit * elitismRate); i < stop; i++) {
        addChromosome(sorted.get(i));
    }
}

## Fixed Function 2
public ElitisticListPopulation(final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
}