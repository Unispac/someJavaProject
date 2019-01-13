package Main.editorNet.editorServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import Main.Main;
import Main.Controller.Controller;
import Main.editorNet.Connection.Connection;

public class editorServer extends Connection
{
    public editorServer(int port) throws IOException
    {
        myConnection= new editorServerThread(port);
        myConnection.start();
    }
}
class editorServerThread extends Thread
{
    
    private ServerSocket myServer;
    public editorServerThread(int port)throws IOException
    {
        myServer=new ServerSocket(port);
        myServer.setSoTimeout(1000);
    }
    public void run()
    {
        Main.printLog("Wating for remote connection , \nlistenning port: "+myServer.getLocalPort()+"...");
        while(!isInterrupted())
        {
            try
            {
                Socket server=myServer.accept();
                server.setSoTimeout(2000);
                Main.printLog("Have connected, the remote IP is: \n"+server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
               
                    while("ready".equals(in.readUTF())==false)
                    {
                        Thread.sleep(100);
                    }
                    out.writeUTF(Main.currentController.getText()+"@client");
                    Thread.sleep(100);
                    while("initSuccess".equals(in.readUTF())==false)
                    {
                        out.writeUTF(Main.currentController.getText()+"@client");
                        Thread.sleep(100);
                    }


                    String temp;
                    out.writeUTF(Main.currentController.getText()+"@client");
                   // System.out.println("write");
                    Thread.sleep(Main.flashGap);
                    

                   int punish=0;
                   int dominant=0;
                    while(true)
                    {
                        if(dominant==0)
                        {
                            temp=in.readUTF();
                           // System.out.println("read");
                            if(!temp.endsWith("@host"))
                            {
                                if("wait".equals(temp))
                                {
                                    punish=4;//Thread.sleep(3000);
                                    Main.currentController.punnish();
                                }
                                else break;

                                continue;
                            }
                            temp=temp.substring(0, temp.length()-5);
                            if(Main.currentController.updateText(temp)==false)
                            {
                                dominant=4;
                                out.writeUTF("wait");
                              //  System.out.println("write");
                                continue;
                            }
                            Thread.sleep(Main.flashGap);
                        }
                        else dominant--;
                        
                        if(punish==0)
                        {
                            out.writeUTF(Main.currentController.getText()+"@client");
                            //System.out.println("write");
                            Thread.sleep(Main.flashGap);
                        }
                        else if(--punish==0)
                        {
                            Main.currentController.releasePunnish();
                            //System.out.println("write");
                            out.writeUTF(Main.currentController.getText()+"@client");
                            Thread.sleep(Main.flashGap);
                        }
                        
                       // System.out.println("dominant : "+dominant +"   "+"punnish : "+punish);
                    }
                server.close();
            }catch(SocketTimeoutException s)
            {
                if(isInterrupted())Main.printLog("Server will be stopped.");
            }catch(InterruptedException e)
            {
                Main.printLog("Server will be stopped.");
                break;
            }catch(Exception e)
            {
                Main.printLog("The remote client has disconnected,server will be closed.");
                break;
            }
        }
    }
}