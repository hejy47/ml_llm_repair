## Fixed Function 1
public Iterator<Chromosome> iterator() {
    return chromosomes.iterator();
    // or alternatively
    // return new ArrayList<Chromosome>(chromosomes).iterator();
    // to avoid ConcurrentModificationException when iterating and modifying at the same time
}