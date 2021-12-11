package kitpolw1;

public interface TypeBuilder<T>
{
    String getTypeName();
    Object create();
    Comparator getTypeComparator();
}
