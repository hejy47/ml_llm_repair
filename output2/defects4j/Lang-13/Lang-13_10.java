## Fixed Function 1
public class ClassLoaderAwareObjectInputStream extends ObjectInputStream {

    private ClassLoader classLoader;

    public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
        super(in);
        this.classLoader = classLoader;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();
        try {
            return Class.forName(name, false, classLoader);
        } catch (ClassNotFoundException ex) {
            return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
        }
    }
}

## Fixed Function 2 (no changes needed)
This function doesn't have any issues.