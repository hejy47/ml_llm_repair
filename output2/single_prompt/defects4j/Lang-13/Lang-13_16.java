## Fixed Function 1
public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
    super(in);
    this.classLoader = classLoader;
}

@Override
protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    String name = desc.getName();
    try {
        Class<?> clazz = Class.forName(name, false, classLoader);
        if (clazz != null) {
            return clazz;
        }
    } catch (ClassNotFoundException ex) {
        // ignore and fallback to default behavior
    }
    return super.resolveClass(desc);
}

## Fixed Function 2
@Override
protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    String name = desc.getName();
    Class<?> clazz;
    try {
        clazz = Class.forName(name, false, classLoader);
    } catch (ClassNotFoundException ex) {
        clazz = Class.forName(name, false, Thread.currentThread().getContextClassLoader());
    }
    return clazz;
}