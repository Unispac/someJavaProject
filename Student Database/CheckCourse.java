import DataBase.*;
public class CheckCourse
{
    public static void main(String[] args) 
    {
        if(args.length==0)
        {
            System.out.println("fatal error : There should be at least one input");
            System.exit(0);
        }    
        Base now = Base.requestObject();
        int i;
        for(i=0;i<args.length;i++)
        {
            now.ListStudent(args[i]);
            System.out.println("-------------------------------------------");
        }
    }
}