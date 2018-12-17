import DataBase.*;
public class CheckStudent
{
    public static void main(String[] args) 
    {
        if(args.length==0)
        {
            System.out.println("fatal error : There should be at least one input");
            System.exit(0);
        }    
        Base now =Base.requestObject();
        int i;
        for(i=0;i<args.length;i++)
        {
            now.ListCourse(args[i]);
            System.out.println("-------------------------------------------");
        }
    }
}