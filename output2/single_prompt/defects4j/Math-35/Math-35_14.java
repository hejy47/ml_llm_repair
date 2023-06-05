## Fixed Function 1
public ElitisticListPopulation(final List<Chromosome> chromosomes, final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
    if (chromosomes.size() > populationLimit) {
        this.chromosomes = PopulationUtils.getSortedElites(
            populationLimit, elitismRate, chromosomes
        );
    } else {
        this.chromosomes = new ArrayList<>(chromosomes);
    }
}

## Fixed Function 2
public ElitisticListPopulation(final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
    this.chromosomes = new ArrayList<>(populationLimit);
}