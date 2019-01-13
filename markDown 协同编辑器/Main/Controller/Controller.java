package Main.Controller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import top.touchface.md2x.*;
//import top.touchface.md2x.*;

import javax.lang.model.util.ElementScanner6;

import Main.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
class headLine
{
    public String content;
    int level;
    public FlowPane myPane;
    headLine(String content,int level)
    {
        this.content=content;
        this.level=level;
    }
}
public class Controller implements Initializable
{
    @FXML
    private TextArea inputArea;
    @FXML
    private TextArea logField;
    @FXML
    private TextField localPortField;
    @FXML
    private TextField remoteIPField;
    @FXML
    private TextField remotePortField;
    @FXML
    private FlowPane headLineField;

    

    private Alert HelpInfo=new Alert(Alert.AlertType.INFORMATION);
    private FileChooser fileChooser,fileSaver,exportHtml;
    private String content;
    private String currentFile;
    private int fontSize=12;
    private boolean ctrlDown=false;
    private ArrayList<headLine>myHeadLine=new ArrayList<headLine>();
    long lastTime=-9999999;
    private boolean underPunished=false;
    //private Timer updateHeadLineTimer;
    private String lastText;
    private headLine levelStack[]=new headLine[12];

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        inputArea.setFont(new Font("Cambria",fontSize));

        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Markdown File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Markdown Files", "*.md"),
            new FileChooser.ExtensionFilter("Html Filse", "*.html")
        );

        fileSaver=new FileChooser();
        fileSaver.setTitle("Save As");
        fileSaver.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Markdown Files", "*.md"),
            new FileChooser.ExtensionFilter("Html Filse", "*.html")
        );

        exportHtml=new FileChooser();
        exportHtml.setTitle("Export Html File");
        exportHtml.getExtensionFilters().addAll
        (
            new FileChooser.ExtensionFilter("Html Filse", "*.html"),
            new FileChooser.ExtensionFilter("Markdown Files", "*.md")
        );
        
        Main.currentController=this;
        Main.logInfo=logField;

        for(int i=0;i<12;i++)levelStack[i]=new headLine("", 0);
        /*updateHeadLineTimer=new Timer();
        updateHeadLineTimer.scheduleAtFixedRate
        (
            new TimerTask()
            {
                @Override
                public void run() 
                {
                    checkHeadLine();
                }
            }
            , 0, 1000
        );*/
    }

    public void toOpenFile(ActionEvent event)
    {
        File myFile=fileChooser.showOpenDialog(Main.currentStage);
        if(myFile!=null)
        {
            try
            {
                currentFile=myFile.getAbsolutePath();
            
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(currentFile),"UTF-8"));
                StringBuffer x=new StringBuffer();
                String temp=in.readLine();
                while(temp!=null)
                {
                    x.append(temp);
                    x.append("\n");
                    temp=in.readLine();
                }
                in.close();
                content=x.toString();
                inputArea.setText(content);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public void toSaveFile(ActionEvent event)
    {
        if(currentFile!=null)
        {
             try
            {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentFile),"UTF-8"));
                out.write(inputArea.getText());
                out.close();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            toSaveAsFile(null);
        }       
    }
    public void toSaveAsFile(ActionEvent event)
    {
        File myFile=fileSaver.showSaveDialog(Main.currentStage);
        if(myFile!=null)
        {
            try
            {
                currentFile=myFile.getAbsolutePath();
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentFile),"UTF-8"));
                out.write(inputArea.getText());
                 out.close();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public void toExit(ActionEvent event)
    {
       // System.out.println("click Exit");
        System.exit(0);
    }
    public void input(KeyEvent event)
    {
        if(ctrlDown&&event.getCode()==KeyCode.CONTROL)ctrlDown=false;
        lastTime=System.currentTimeMillis();
    }
    public void keyDown(KeyEvent event)
    {   
        KeyCode myCode=event.getCode();
        if(myCode==KeyCode.EQUALS)
        {
            if(fontSize<60)
            {
                fontSize++;
                inputArea.setFont(new Font("Cambria",fontSize));
            }         
        }
        else if(myCode==KeyCode.MINUS)
        {
            if(fontSize>10)
            {
                fontSize--;
                inputArea.setFont(new Font("Cambria",fontSize));
            }         
        }
        else if(myCode==KeyCode.CONTROL) 
        {
            ctrlDown=true;
        }
        else if(ctrlDown==true&&myCode==KeyCode.S)
        {
            if(currentFile!=null)toSaveFile(null);
            else toSaveAsFile(null);
        }
    }
    public void toClose(ActionEvent event)
    {
        inputArea.clear();
        currentFile=null;
    }
    public void toHtml(ActionEvent event)
    {
        File myFile=exportHtml.showSaveDialog(Main.currentStage);
        
        if(myFile!=null)
        try
        {
            Md2x md2x=new Md2x();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(myFile.getAbsolutePath()),"UTF-8"));
            out.write(md2x.parse(inputArea.getText()));
            out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        } 
    }
    public void toBuildServer()throws IOException
    {
        TextInputDialog PORT=new TextInputDialog();
        PORT.setTitle("Input the local port to be listenned");
        PORT.setHeaderText("Local Port");
        String port;
        try
        {
           port=PORT.showAndWait().get();
            Main.buildServer(Integer.parseInt(port) );
        }catch(Exception e)
        {
            System.out.println("server building abort.");
        }
        PORT.close();
    }
    public void toConnectServer()
    {
        TextInputDialog IP=new TextInputDialog();
        TextInputDialog PORT=new TextInputDialog();
        String IPaddress;
        String remotePort;
        try
        {
            IP.setTitle("Input the remote IP address");
            IP.setHeaderText("Remote IP");
            IPaddress=IP.showAndWait().get();
            IP.close();
            
            PORT.setTitle("Input the remote Port");
            PORT.setHeaderText("Remote Port");
            remotePort=PORT.showAndWait().get();
            PORT.close();

            Main.connectServer(IPaddress, Integer.parseInt(remotePort));
        }catch(Exception e)
        {
            System.out.println("server connecting abort");
        }
    }
    public boolean updateText(String newText)
    {
        long temp=lastTime;
        lastTime=System.currentTimeMillis();
        if(underPunished==false&&lastTime-temp<500)return false;
        
        if(inputArea.getText().equals(newText))return true;
        else inputArea.setText(newText);
        return true;
    }
    public String getText()
    {
        return inputArea.getText();
    }
    public void punnish()
    {
        underPunished=true;
    }
    public void releasePunnish()
    {
        underPunished=false;
    }
    public void toDisconnected()
    {
        Main.disConnect();
    }
    public void toConnectByTextField()
    {
        try
        {
            Main.connectServer(remoteIPField.getText(), Integer.parseInt(remotePortField.getText()));
        }catch(Exception e)
        {

        }
    }
    public void toBuildByTextField()
    {
        try
        {
            Main.buildServer(Integer.parseInt( localPortField.getText()));
        }catch(Exception e)
        {

        }
    }
    public void showHelpInfo()
    {
        HelpInfo.setTitle("markUp");
        HelpInfo.setHeaderText("collaborative markdown editor");
        HelpInfo.setContentText("Developed by Unispac @ ZJU");
        HelpInfo.show();
    }
    public void checkHeadLine()
    {
        myHeadLine.clear();
        String content=inputArea.getText();

        if(lastText!=null&&lastText.equals(content))return;
        else lastText=content;

        int index=content.indexOf('#');
        int ed;
        int level;
        while(index!=-1)
        {
            level=0;
            while(index<content.length()&&content.charAt(index)=='#')
            {
                level++;
                index++;
            }
            if(index<content.length()&&content.charAt(index)==' ')
            {
                ed=content.indexOf('\n',index);
                if(ed<0)ed=content.length()-1;
                if(ed>index)myHeadLine.add(new headLine(content.substring(index,ed),level));
                index=ed;
            }
         
           if(index>=0&&index<content.length())index=content.indexOf('#',index);
           else index=-1;
        }
        ObservableList<Node> temp=headLineField.getChildren();
        
        temp.clear();
        TitledPane x;
        int lastlevel=100;
        index=0;
        ed=0;
        headLine tempHead=null;
        FlowPane lastPane=null,tempPane;
        if(myHeadLine.size()>0)tempHead=myHeadLine.get(0);
        for(int i=0;i<myHeadLine.size();i++)
        {
            tempPane=new FlowPane();
            x=new TitledPane(""+index, tempPane);
            x.setText(tempHead.content);
            x.setPrefWidth(200-20*tempHead.level);

            if(index==0)temp.add(x);
            else levelStack[index-1].myPane.getChildren().add(x);
            
            lastlevel=tempHead.level;
            lastPane=tempPane;
            if(i+1<myHeadLine.size())
            {
                tempHead=myHeadLine.get(i+1);
                if(tempHead.level>lastlevel)
                {
                    levelStack[index].level=lastlevel;
                    levelStack[index].myPane=lastPane;
                    index++;
                }
                else
                {
                    while(index>0&&levelStack[index-1].level>=tempHead.level)index--;
                }
            }
        }
    }
}