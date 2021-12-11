package kitpolw1;

public class StringType<T> implements TypeBuilder 
{
    private int range;

    StringType() 
    {
        range = 10;
    }

    StringType(int range) 
    {
        this.range = range;
    }

    @Override
    public String getTypeName() 
    {
        return "String";
    }

    @Override
    public Object create() 
    {
        int numberOfChar;
        StringBuilder s = new StringBuilder();

        for(int i = 0; i < range; i++) 
        {
            if(Math.random() * 1 < 0.5) 
            {
                numberOfChar = (int)Math.round(Math.random() * 25 + 65);
            }
            else 
            {
                numberOfChar = (int)Math.round(Math.random() * 25 + 97);
            }

            s.append((char) numberOfChar);
        }

        return s.toString();
    }

    @Override
    public Comparator getTypeComparator() 
    {
        return (o1, o2) -> ((String)o1).compareTo((String)o2);
    }
}
