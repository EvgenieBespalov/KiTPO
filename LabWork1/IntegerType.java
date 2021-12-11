package kitpolw1;

public class IntegerType implements TypeBuilder 
{
    private int range;

    IntegerType() 
    {
        range = 10000;
    }

    IntegerType(int range) 
    {
        this.range = range;
    }

    @Override
    public String getTypeName() 
    {
        return "Integer";
    }

    @Override
    public Object create() 
    {
        return (int)(Math.random() * range);
    }

    @Override
    public Comparator getTypeComparator() 
    {
        return (o1, o2) -> (int)o1 - (int)o2;
    }
}
