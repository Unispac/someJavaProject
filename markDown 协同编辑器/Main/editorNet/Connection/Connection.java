package Main.editorNet.Connection;
public class Connection
{
    protected Thread myConnection;
    public void disConnect()
    {
        if(myConnection!=null)
        {
            myConnection.interrupt();
        }
    }
}