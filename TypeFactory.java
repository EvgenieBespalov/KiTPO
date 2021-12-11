package kitpolw1;

import java.util.ArrayList;



public class TypeFactory 
{

    
    public static ArrayList<String> getTypeNameList() 
    {
        final ArrayList<String> list = new ArrayList<>();
        for (Types type : Types.values())
            list.add(String.valueOf(type));
        return list;
    }

    public static TypeBuilder getBuilder(Types type) 
    {
        switch (type) 
        {
            case Integer: 
            {
                return new IntegerType();
            }
            case String: 
            {
                return new StringType();
            }
            default: 
            {
                System.out.println("Wrong type, return null");
                return null;
            }
        }
    }
}
