import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellType;

public class excelReaderForSQLite
{
    private String dataBaseName;
    private String excelFileName;
    private String sheetName;
    private String tableName;
    private String [] type;
    private Sheet sourceSheet;
    private excelReader myExcel;
    private SQLiteManager mySQLite;
    private excelReaderForSQLite(String[] args)
    {
        dataBaseName=args[0];
        excelFileName=args[1];
        if(args.length>=3)sheetName=args[2];
        else sheetName=null;
        if(args.length==4)tableName=args[3];
        else tableName=null;

        myExcel=new excelReader(excelFileName);
        mySQLite=new SQLiteManager(dataBaseName);
    }
    private String analyzeCell(Cell x)
    {
        if(x==null)return "NULL";
        CellType tempCellType=x.getCellType();
        String temp;
        if(tempCellType==CellType.STRING)temp = x.getStringCellValue();
        else if(tempCellType==CellType.NUMERIC)
        {
            double y=x.getNumericCellValue();
            if(Math.floor(y)==y)temp=""+(int)Math.floor(y);
            else temp = ""+y;
        }
        else if(tempCellType==CellType.BOOLEAN)temp = ""+x.getBooleanCellValue();
        else if(tempCellType== CellType.FORMULA)temp = ""+x.getCellFormula();
        else temp="NULL";
        return temp;
    }
    private String getTableInfor()
    {
        sourceSheet=myExcel.getSheet(sheetName);

        if(sourceSheet==null||sourceSheet.getLastRowNum()+1==0)return null;
   
        StringBuffer tableInfo = new StringBuffer();
        tableInfo.append("rowNumber INT  PRIMARY KEY");

        ArrayList<String>attrib = new ArrayList<String>();
        Row items = sourceSheet.getRow(0);
        int cnt=items.getPhysicalNumberOfCells();
        //System.out.println(cnt);
        for(int k=0;k<cnt;k++)
        {
            attrib.add(items.getCell(k).getStringCellValue());
            //System.out.println(items.getCell(k).getStringCellValue());
        }
       
        type=new String[cnt];
        int len[] = new int[cnt];
        String temp;

        for(int i=0;i<cnt;i++)type[i]="INT";

        int rowNum=sourceSheet.getLastRowNum()+1;
        Cell tempCell;
        CellType tempCellType;
        for (int i = 1; i <rowNum; i++) 
        {   
             items = sourceSheet.getRow(i);   
            // System.out.println(items.getPhysicalNumberOfCells());
            for(int k=0;k<cnt;k++) 
            { 
                if(items==null)temp="NULL";
                else temp=analyzeCell(items.getCell(k));
                //System.out.print(temp+"   ");
                if(type[k]!="CHAR")
                for(int j=0;j<temp.length();j++)
                {
                    if(temp.charAt(j)<'0'||temp.charAt(j)>'9')
                    {
                        if(temp.charAt(j)=='.')
                        {
                            if(type[k]=="INT")type[k]="REAL";
                        }
                        else type[k]="CHAR";
                    }
                }
                if(type[k]=="CHAR")len[k]=Math.max(len[k], temp.length());
            }
         //   System.out.println("");
        }

      for(int i=0;i<cnt;i++)
        {
            tableInfo.append(", "+attrib.get(i)+" ");
            tableInfo.append(type[i]);
            if(type[i]=="CHAR")
            {
                tableInfo.append("("+len[i]+")"); 
            }
            tableInfo.append(" ");
        }

        return tableInfo.toString();
    }
    private int importTable()
    {
        StringBuffer buffer=new StringBuffer();
        int status;

        status = mySQLite.startInsert();
        if(status==0)return -1;

        Row items = sourceSheet.getRow(0);
        int cnt=items.getPhysicalNumberOfCells();
        int rowNum=sourceSheet.getLastRowNum()+1;
        String temp;
        for (int i = 1; i < rowNum; i++) 
        {   
            items = sourceSheet.getRow(i);   
            buffer.setLength(0);
            buffer.append("INSERT INTO "+tableName+" VALUES("+i);
            for(int k=0;k<cnt;k++) 
            {
                if(items==null)temp="NULL";
                else temp=analyzeCell(items.getCell(k));
                if(type[k]=="CHAR")
                {
                    buffer.append(",'"+temp+"'");
                }
                else
                {
                    buffer.append(","+temp);
                }
            }
            buffer.append(")");
            status=mySQLite.insertItem(buffer.toString());
            if(status==0)return -1;
        }
        status = mySQLite.endInsert();
        if(status==0)return -1;
        System.out.println("insertion finished! Total time cost of insertion is : "+SQLiteManager.totTime+" ms.\t\n\t\n");
        return rowNum-1;
    }
    public int startImport()
    {
        if(myExcel.successfullyRead()==false)return 0;
        String sql;
        sql=getTableInfor();
        if(sql==null)
        {
            System.out.println("The new table won't be created for the sheet doesn't exist or is empty.");
            return 0;
        }
    

        if(tableName==null)
        {
            if(sheetName!=null)tableName=sheetName;
            else 
            {
                int k=excelFileName.indexOf(".xls");
                tableName=excelFileName.substring(0, k);
            }
        }

        System.out.println("Table Structure : ");
        sql="CREATE TABLE "+tableName+" ("+sql+")";
        System.out.println(sql+"\t\n\t\n");
        int status;
        status = mySQLite.createTable(sql,tableName);
        if(status==0)return 0;
        status =  importTable();
        if(status==-1)return 0;
        System.out.println("Number of rows : "+status+"\t\n\t\n");
        return 1;
    }

    public static excelReaderForSQLite createExcelToSQLiteImporter(String[] args)
    {
        if(args.length<2)
        {
            System.out.println("Fatal Error : ");
            System.out.println("At least two parameters is required :  dataBaseName,xlsxFileName");
            return null;
        }
        else if(args.length>4)
        {
            System.out.println("Fatal Error : ");
            System.out.println("Too much parameters!");
            return null;
        }
        else
        {
            return new excelReaderForSQLite(args);
        }
    }

    public static void main(String[] args) 
    {
        excelReaderForSQLite Importer = createExcelToSQLiteImporter(args);
        if(Importer==null)
        {
            System.exit(0);
        }
        int status =Importer.startImport();
        if(status==1)
        {
            System.out.println("The excel file is successfully imported!");
        }
        else
        {
            System.out.println("Fail to import the excel file");
        }
        return;
    }
}

class SQLiteManager
{
    private static String sqliteJdbc="org.sqlite.JDBC";
    private String dataBaseName;
    private Connection connector;
    private Statement stmt;
    private PreparedStatement pstmt;
    public static long totTime=0;
    public SQLiteManager(String dataBaseName)
    {
        this.dataBaseName="jdbc:sqlite:./SQLite dataBase/"+dataBaseName;
        if(dataBaseName.indexOf(".db")==-1)this.dataBaseName+=".db";
        try
        {
            Class.forName(sqliteJdbc);
        }catch(Exception e)
        {
            System.out.println("There was something wrong when jdbs was started ");
            System.exit(0);
        }
    }
    public int startInsert()
    {
        try
        {
            //Class.forName(sqliteJdbc);
            totTime=System.currentTimeMillis();
            connector=DriverManager.getConnection(dataBaseName);
            stmt=connector.createStatement();
            connector.setAutoCommit(false);
            return 1;
        }catch(Exception e)
        {
            System.out.println("Error happened when the inserting channel was built");
            e.printStackTrace();
            return 0;
        }
    }
    public int insertItem(String sql)
    {
        try
        {
            stmt.executeUpdate(sql);
            return 1;
        }catch(Exception e)
        {
            System.out.println("Error happened when inserting item into SQLite table .");
            System.out.println(sql);
            e.printStackTrace();
            return 0;
        }
    }
    public int endInsert()
    {
        try
        {
            stmt.close();
            connector.commit();
            totTime=System.currentTimeMillis()-totTime;
            connector.close();
            return 1;
        }catch(Exception e)
        {
            System.out.println("Error happened when ending the inserting Channel .");
            e.printStackTrace();
            return 0;
        }
    }
    public int createTable(String sql,String tableName)
    {
        try
        {
            //Class.forName(sqliteJdbc);
            connector=DriverManager.getConnection(dataBaseName);
            stmt=connector.createStatement();

            ResultSet rs = connector.getMetaData().getTables(null, null, tableName, null);
            if (rs.next())
            {
                rs.close();
                System.out.println("The table : "+tableName +" has already existed. Overwrite it ?  y/n ");
                Scanner in = new Scanner(System.in);
                String confirm=in.nextLine().toLowerCase();
                if(confirm.equals("y")||confirm.equals("yes"))
                {
                    stmt.executeUpdate("DROP TABLE "+tableName);
                    stmt.executeUpdate(sql);
                    System.out.println("\t\n A new table is created to replace the origional one.");
                }
                else 
                {
                    System.out.println("\t\n Request for creating table is dropped.");
                    connector.close();
                    return 0;
                }
            }
            else 
            {
                rs.close();
                stmt.executeUpdate(sql);
            }
            connector.close();
            return 1;
        }catch(Exception e)
        {
            System.out.println("Error happened when create SQLite table.");
            System.out.println(sql);
            e.printStackTrace();
            return 0;
        }
    }
}


class excelReader
{
    private Workbook excelFile;
    excelReader(String fileName)
    {
        try
        {
            InputStream is=new FileInputStream(fileName);
            if(fileName.endsWith(".xls"))excelFile=new HSSFWorkbook(is);
            else if(fileName.endsWith(".xlsx"))excelFile=new XSSFWorkbook(is);
            else
            {
                System.out.println("Please make sure that your excel file name ends with '.xls' or '.xlsx'.");
                excelFile=null;
            }
        }catch(Exception e)
        {
            System.out.println("Error happened when reading excel file.");
            excelFile=null;
        }
    }
    public boolean successfullyRead()
    {
        if(excelFile==null)return false;
        else return true;
    }
    public Sheet getSheet(String sheetName)
    {
        if(successfullyRead()==false)return null;
        int cnt=excelFile.getNumberOfSheets();
        if(sheetName==null)
        {
            if(cnt==0)return null;
            else return excelFile.getSheetAt(0);
        }
        Sheet tempSheet;
        for(int i=0;i<cnt;i++)
        {
            tempSheet=excelFile.getSheetAt(i);
            if(sheetName.equals(tempSheet.getSheetName()))return tempSheet;
        }
        return null;
    }
}