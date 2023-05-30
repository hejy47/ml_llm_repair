## Fixed Function 1
public class ClassLoaderAwareObjectInputStream extends ObjectInputStream {
    private final ClassLoader classLoader;

    public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
        super(in);
        this.classLoader = classLoader;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();
        return Class.forName(name, false, classLoader);
    }
}

## Fixed Function 2
@Override
protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    String name = desc.getName();
    try {
        return Class.forName(name, false, classLoader);
    } catch (ClassNotFoundException ex) {
        return super.resolveClass(desc);
    }
}