import DataBase.*;
public class Input
{
    public static void main(String[] args) 
    {
        if(args.length!=3)
        {
            System.out.println("error : Invalid parameter format");
            System.exit(0);
        }
        String studentName,courseName;
        int score;
        studentName=args[0];
        courseName=args[1];
        score=Integer.parseInt(args[2]);
        Base now=Base.requestObject();
        now.Input(studentName, courseName, score);
        now.outPort();
        return;
    }
}