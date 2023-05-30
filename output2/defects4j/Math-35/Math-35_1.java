## Fixed Function 1
public ElitisticListPopulation(final List<Chromosome> chromosomes, final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
    if (chromosomes.size() > populationLimit) {
        // sort chromosomes by fitness and keep only the top ones according to elitismRate
        Collections.sort(chromosomes);
        int eliteSize = (int) (populationLimit * elitismRate);
        List<Chromosome> elites = new ArrayList<>(chromosomes.subList(0, eliteSize));
        this.addAll(elites);
        this.addAll(chromosomes.subList(eliteSize, chromosomes.size()));
    } else {
        this.addAll(chromosomes);
    }
}

## Fixed Function 2
public ElitisticListPopulation(final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
}