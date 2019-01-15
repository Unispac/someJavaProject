package Main;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;

import javax.swing.Timer;

import Main.Controller.Controller;
import Main.editorNet.Connection.Connection;
import Main.editorNet.editorClient.editorClient;
import Main.editorNet.editorServer.editorServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application
{
    static public Stage currentStage;
    static public Controller currentController;
    static public TextArea logInfo;
    static private StringBuffer logInfoBuffer=new StringBuffer();
  //  static public boolean connected;
    static public int flashGap=500;
    static private ArrayList<Connection>myConnection=new ArrayList<Connection>();
    static public void disConnect()
    {
        
        for(Connection k:myConnection)k.disConnect();
        myConnection.clear();
        logInfoBuffer.append("Disconnected!\n");
        logInfo.setText(logInfoBuffer.toString());
    }
    static public void printLog(String s)
    {
        logInfoBuffer.append(s);
        logInfoBuffer.append("\n");
        logInfo.setText(logInfoBuffer.toString());
    }
    public void start(Stage primaryStage)
    {
        try
        {
            currentStage=primaryStage;
    
            EventHandler<WindowEvent> handler = new EventHandler<WindowEvent>() 
            {
                public void handle(WindowEvent event) 
                {
                    System.exit(0);
                }
            };
            primaryStage.setOnCloseRequest(handler);
           primaryStage.getIcons().add(new Image("/view/icon.jpg"));
           Parent root = FXMLLoader.load(getClass().getResource("/view/editor.fxml")); 
           primaryStage.setTitle("MarkUp");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            startTime();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void buildServer(int port)throws IOException
    {
      //  connected=true;
        logInfoBuffer.setLength(0);
        Main.printLog("");
        myConnection.add(new editorServer(port));
        Main.printLog("Server is running , host address is : \n "+Inet4Address.getLocalHost().toString()+":"+port+"\n");        
    }
    public static void connectServer(String serverIP,int remotePort)
    {
        //connected=true;
        logInfoBuffer.setLength(0);
        Main.printLog("");
        myConnection.add(new editorClient(serverIP, remotePort));
    }
    public static void  trigger(String[] args)
    {
        launch(args);
    }
    public void startTime() 
    {
        int delay = 1000;//1000毫秒
        
        Timer timer = new Timer
        (
            delay, new ActionListener() 
            {
                public void actionPerformed(java.awt.event.ActionEvent e) 
                {
                    Platform.runLater
                    (
                        new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                currentController.checkHeadLine();
                                currentController.renderHtml();
                            }
                        }
                    );
                }
            }
        );
        timer.start();
    }
}