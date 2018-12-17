package DataBase;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Base
{
    private static String dataBaseDefaultName="mydatabase";
    public static String getDefaultName()
    {
        return dataBaseDefaultName;
    }
    public static void setDefaultName(String s)
    {
        dataBaseDefaultName=s;
        return;
    }

    public static void importDataBase(Base target,String NameOfDataBase)
    {
        try
        {
            FileReader fr=new FileReader(NameOfDataBase+".CSV");
            BufferedReader br=new BufferedReader(fr);
            
            String str;char temp[];
            StringBuffer x=new StringBuffer();
            String studentName,courseName;
            int score;

            while((str=br.readLine())!=null)
            {
                int i=0;
                temp=str.toCharArray();
                int size=temp.length;

        
                while(i<size&&(temp[i]==' '||temp[i]=='\r'||temp[i]=='\n'))i++;
                if(i==size)continue;
                
                x.setLength(0);
                for(;i<size&&temp[i]!=',';i++)
                {
                    if(temp[i]==' ')continue;
                    x.append(temp[i]);
                }
                studentName=new String(x);
                i++;

                while(i<size&&temp[i]==' ')i++;
                if(i==size||temp[i]==',')
                {
                    System.out.println("error : The .CSV file has a wrong format");
                    System.exit(0);
                }

                x.setLength(0);
                for(;i<size&&temp[i]!=',';i++)
                {
                    if(temp[i]==' ')continue;
                    x.append(temp[i]);
                }
                courseName=new String(x);
                i++;
                while(i<size&&temp[i]==' ')i++;
               if(i==size||temp[i]==',')
               {
                   System.out.println("error : The .CSV file has a wrong format");
                   System.exit(0);
               }
                
                score=0;
                for(;i<size&&temp[i]!=',';i++)
                {
                    if(temp[i]==' ')continue;
                    if(temp[i]<'0'||temp[i]>'9')
                    {
                            System.out.println("error : The score must be number!");
                            System.exit(0);
                    }
                    score=score*10+temp[i]-'0';
                }
                target.Input(studentName, courseName, score);
            }
            br.close();
        }catch(IOException e)
        {
            if(NameOfDataBase==dataBaseDefaultName)System.out.println("Generate Data Base : "+dataBaseDefaultName);
            else 
            {
                e.printStackTrace();
                System.out.println("Can not find the target .CSV file");
                System.exit(0);
            }
        }
    }
    public static void importDataBase(String NameOfTarget,String NameOfDataBase)
    {
        
        Base now=new Base(NameOfTarget);
        importDataBase(now, NameOfDataBase);
        now.outPort();
        return;
    }

    public static Base requestObject()
    {
        return new Base();
    }
    public static Base requestObject(String s)
    {
        return new Base(s);
    }

    Base(String s)
    {
        setName(s);
        STUDENT=new HashMap<String,Student>();
        COURSE=new HashMap<String,Course>();
        importDataBase(this, DataBaseName);
        return;
    }
    Base()
    {
        STUDENT=new HashMap<String,Student>();
        COURSE=new HashMap<String,Course>();
        importDataBase(this, DataBaseName);
        return;
    }
    private  String DataBaseName=dataBaseDefaultName;
    public  void setName(String s)
    {
        DataBaseName=s;
        return;
    }
    public  String getName()
    {
        return DataBaseName;
    }
    private  HashMap<String,Student>STUDENT;
    private  HashMap<String,Course>COURSE;
    public  void Input(String studentName,String courseName,int score)
    {
        Student student; Course course;
        if(STUDENT.keySet().contains(studentName)==false)
        {
                student=new Student(studentName);
                STUDENT.put(studentName, student);
        }
        else student=STUDENT.get(studentName);
       
        if(COURSE.keySet().contains(courseName)==false)
        {
                //System.out.println("Build "+courseName);
                course=new Course(courseName);
                COURSE.put(courseName, course);
        }
        else course=COURSE.get(courseName);

        student.setScore(courseName,score);
        course.setScore(studentName, score);
    }
    public  Course getCourse(String courseName)
    {
        return COURSE.get(courseName);
    }
    public  Student getStudent(String studentName)
    {
        return STUDENT.get(studentName);
    }
    public void  outPort()
    {
        Outport.generateDatabase(DataBaseName);
        for(String k : STUDENT.keySet())
        {
            Student temp=STUDENT.get(k);
            temp.outPort();
        }
        Outport.endStream();
    }
    public void ListCourse(String s)
    {
        Student temp=STUDENT.get(s);
        if(temp==null)
        {
            System.out.println("Error : No such student \" "+s+"\" !");
        }
        else temp.outPortByTermial();
    }
    public void ListStudent(String s)
    {
        Course temp=COURSE.get(s);
        if(temp==null)
        {
            System.out.println("Error : No such Curse \" "+s+"\" !");
        }
        else temp.outPortByTermial();
    }
}


class Course
{
    Course(String s)
    {
        student=new HashMap<String,Integer>();
        sum=average=0;
        setName(s);
    }
    private double sum,average;
    private String courseName;
    public String getName()
    {
        return courseName;
    }
    public void setName(String s)
    {
        courseName=s;
        return;
    }
    
    private HashMap<String,Integer> student;
    public void setScore(String studentName,Integer score)
    {
        if(student.keySet().contains(studentName))sum-=student.get(studentName);
        student.put(studentName,score);
        sum+=score;
        average=sum/(1.0*student.size());
    }
    public int getScore(String studentName)
    {
        if(student.keySet().contains(studentName))return student.get(studentName);
        else return -1;
    }
    public double getAverage()
    {
        return average;
    }
    public void outPortByTermial()
    {
        System.out.println("Grade List of  Course "+courseName);
        for(String s : student.keySet())
        {
            System.out.println(s+" : "+student.get(s));
        }
        System.out.println("             "+"Average : "+String.format("%.2f",average));
    }
}


class Student
{
    Student(String s)
    {
        course=new HashMap<String,Integer>();
        setName(s);
        sum=average=0;
    }
    private double sum,average;
    private String studentName;
    public String getName()
    {
        return studentName;
    }
    public void setName(String s)
    {
        studentName=s;
        return;
    }

    private HashMap<String,Integer> course;
    public int getScore(String courseName)
    {
        if(course.keySet().contains(courseName))return course.get(courseName);
        else return -1;
    }
    public void setScore(String courseName,int score)
    {
        if(course.keySet().contains(courseName))sum-=course.get(courseName);
        course.put(courseName, score);
        sum+=score;
        average=sum/(1.0*course.size());
        return;
    }
    public double getAverage()
    {
        return average;
    }
    public void outPort()
    {
        for(String s : course.keySet())
        {
            Outport.outPut(studentName, s, course.get(s));
        }
    }
    public void outPortByTermial()
    {
        System.out.println("Grade List of  Student "+studentName);
        for(String s : course.keySet())
        {
            System.out.println(s+" : "+course.get(s));
        }
        System.out.println("             "+"Average : "+String.format("%.2f",average));
    }
}
class Outport
{
    private static FileWriter fw;
    public static void generateDatabase(String BaseName)
    {
        System.out.println("Update Data Base : "+BaseName+".CSV");
        try
        {
            fw=new FileWriter(BaseName+".CSV");    
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void outPut(String studentName,String courseName,int score)
    {
        try
        {
            fw.write(studentName+", "+courseName+", "+score+"\r\n");
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void endStream()
    {
        try
        {
            fw.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}