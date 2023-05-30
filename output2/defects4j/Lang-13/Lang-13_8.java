## Fixed Function 1
public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
    super(in);
    if (classLoader == null) {
        this.classLoader = Thread.currentThread().getContextClassLoader();
    } else {
        this.classLoader = classLoader;
    }
}

## Fixed Function 2
@Override
protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    String name = desc.getName();
    try {
        return Class.forName(name, false, classLoader);
    } catch (ClassNotFoundException ex) {
        return Class.forName(name, false, getClass().getClassLoader());
    }
}