## Fixed Function 1
public LookupTranslator(final CharSequence[]... lookup) {
    lookupMap = new HashMap<CharSequence, CharSequence>();
    int _shortest = Integer.MAX_VALUE;
    int _longest = 0;
    if (lookup != null) {
        for (final CharSequence[] seq : lookup) {
            if(seq.length != 2) continue; // validate input and skip to next if invalid
            this.lookupMap.put(seq[0], seq[1]);
            final int sz = seq[0].length();
            if (sz < _shortest) {
                _shortest = sz;
            }
            if (sz > _longest) {
                _longest = sz;
            }
        }
    }
    shortest = _shortest;
    longest = _longest;
}

## Fixed Function 2
@Override
public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
    int max = longest;
    if (index + longest > input.length()) {
        max = input.length() - index;
    }
    // descend so as to get a greedy algorithm
    for (int i = max; i >= shortest; i--) {
        if(index + i > input.length()) continue; // skip if out of bounds
        final CharSequence subSeq = input.subSequence(index, index + i);
        final CharSequence result = lookupMap.get(subSeq);
        if (result != null) {
            out.write(result.toString());
            return i;
        }
    }
    return 0;
}