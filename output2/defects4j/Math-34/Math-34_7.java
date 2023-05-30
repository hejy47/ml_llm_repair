## Fixed Function 1
public Iterator<Chromosome> iterator() {
    return new Iterator<Chromosome>() {
        private final Iterator<Chromosome> iterator = chromosomes.iterator();

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Chromosome next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove() method is not supported in the iterator");
        }
    };
}