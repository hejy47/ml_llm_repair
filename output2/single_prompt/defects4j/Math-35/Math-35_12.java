## Fixed Function 1
public ElitisticListPopulation(final List<Chromosome> chromosomes, final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
    this.addChromosomes(chromosomes);
    this.setElitismStrategy(new ElitisticListPopulation.ElitismPercent<E>(elitismRate));
}

## Fixed Function 2
public ElitisticListPopulation(final int populationLimit, final double elitismRate) {
    super(populationLimit);
    this.elitismRate = elitismRate;
    this.setElitismStrategy(new ElitisticListPopulation.ElitismPercent<E>(elitismRate));
}