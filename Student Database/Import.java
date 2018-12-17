import DataBase.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class Import
{
    public static void main(String[] args) 
    {
        if(args.length==0)
        {
            System.out.println("fatal error : no input file");
            System.exit(0);
        }
        StringBuffer x=new StringBuffer();
        int i;
        for(i=0;i<args.length;i++)x.append(args[i]);
        String filename=new String(x);
        Base.importDataBase(Base.getDefaultName(),filename);
    }
}