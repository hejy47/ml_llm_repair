## Fixed Function 1
public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
    super(in);
    this.classLoader = classLoader;
}

protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    String name = desc.getName();
    try {
        return Class.forName(name, false, classLoader);
    } catch (ClassNotFoundException ex) {
        Thread currentThread = Thread.currentThread();
        return Class.forName(name, false, currentThread.getContextClassLoader());
    }
}