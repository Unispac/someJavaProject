package Main.editorNet.editorClient;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Main.Main;
import Main.Controller.Controller;
import Main.editorNet.Connection.Connection;

public class editorClient extends Connection
{
    public editorClient(String serverName,int port)
    {
        
        myConnection=new editorClientThread(serverName,port);
        myConnection.start();
    }
}
class editorClientThread extends Thread
{
    private Socket client;
    private String serverName;
    private int port;
    public editorClientThread(String serverName,int port)
    {
        this.serverName=serverName;
        this.port=port;
    }
    public void run()
    {
     try
      {
          Main.printLog("Try to connect :" + serverName + " , port :" + port);
         Socket client = new Socket(serverName, port);
         client.setSoTimeout(2000);
         DataOutputStream out = new DataOutputStream(client.getOutputStream());
         DataInputStream in = new DataInputStream(client.getInputStream());
         Main.printLog("Successfully connect to the target host : \n"+serverName+":"+port);
         out.writeUTF("ready");

         String temp;
         temp=in.readUTF();
         while(temp==null)
         {
             Thread.sleep(100);
             temp=in.readUTF();
         }
         temp=temp.substring(0,temp.length()-7);
         
        Main.currentController.updateText(temp);
         out.writeUTF("initSuccess");
        int punish=0;
        int dominant=0;
        while(!isInterrupted())
        {
            if(dominant==0)
            {
                temp=in.readUTF();
                if(!temp.endsWith("@client"))
                {
                    if("wait".equals(temp))
                    {
                        punish=4;
                        Main.currentController.punnish();
                    }
                    else break;
                    continue;
                }
                temp=temp.substring(0,temp.length()-7);
                if(Main.currentController.updateText(temp)==false)
                {
                    dominant=4;
                    out.writeUTF("wait");
                    continue;
                }
                Thread.sleep(Main.flashGap);
            }
            else dominant--;
            
            if(punish==0)
            {
                out.writeUTF(Main.currentController.getText()+"@host");
                Thread.sleep(Main.flashGap);
            }
            else  if(--punish==0)
            {
                Main.currentController.releasePunnish();
                out.writeUTF(Main.currentController.getText()+"@host");
                Thread.sleep(Main.flashGap);
            }
        }
        
      }catch(InterruptedException e)
      {
          Main.printLog("Connection will be stopped.");
      }
      catch(Exception e)
      {
        e.printStackTrace();
        Main.printLog("Connection Abort.");
      }
    }
}