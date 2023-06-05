## Fixed Function 1
public Iterator<Chromosome> iterator() {
    return new ArrayList<>(chromosomes).iterator();
}

Explanation: 

The original code returns the iterator of the underlying List, `chromosomes`. If the List is modified while iterating, there can be ConcurrentModificationException. 

To avoid that, a new ArrayList is created with the same elements as the original list and the iterator for the new list is returned. This way, any modification to the original list will not affect the iterator.