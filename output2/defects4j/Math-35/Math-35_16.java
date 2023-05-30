## Fixed Function 1
public ElitisticListPopulation(final List<Chromosome> chromosomes, final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
    if (chromosomes != null) {
        Collections.sort(chromosomes);
        int elitistIndex = (int) Math.ceil(populationLimit * elitismRate);
        List<Chromosome> elite = new ArrayList<>(chromosomes.subList(0, elitistIndex));
        setChromosomes(elite);
    }
}

## Fixed Function 2
public ElitisticListPopulation(final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
}