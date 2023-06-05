## Fixed Function 1
public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
    super(in);
    this.classLoader = classLoader;
}

// Add the following method to load classes
protected Class<?> resolveClass(String className) throws ClassNotFoundException {
    try {
        return Class.forName(className, false, classLoader);
    } catch (ClassNotFoundException ex) {
        return Class.forName(className, false, Thread.currentThread().getContextClassLoader());
    }
}

## Fixed Function 2
@Override
protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    String name = desc.getName();
    return resolveClass(name);
}