## Fixed Function 1
public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
    super(in);
    this.classLoader = classLoader != null ? classLoader : Thread.currentThread().getContextClassLoader();
}

## Fixed Function 2
@Override
protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    String name = desc.getName();
    try {
        return Class.forName(name, false, classLoader);
    } catch (ClassNotFoundException ex) {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != currentClassLoader) {
            return Class.forName(name, false, currentClassLoader);
        }
        throw ex;
    }
}