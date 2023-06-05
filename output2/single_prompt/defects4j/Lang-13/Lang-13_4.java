## Fixed Function 1
public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
    super(in);
    this.classLoader = classLoader;
}

@Override
protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    try {
        return Class.forName(desc.getName(), false, classLoader);
    } catch (ClassNotFoundException ex) {
        return super.resolveClass(desc);
    }
}

## Fixed Function 2
// No fix needed for this function.