import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class myMouseListener implements MouseListener,MouseMotionListener,MouseWheelListener
{
    private int currentX=-1,currentY=-1;
    private static int tempX,tempY;
    private myCanva chessBoard;
    private static double gap=goBang.gap;
    private boolean lastTemp=false;
    private int turn=1;

    myMouseListener(myCanva chessBoard)
    {
        this.chessBoard=chessBoard;
    }
    public void mouseClicked(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
        if(chessBoard.getTurn()==false)return;
        
        if(lastTemp==false)return;
        else
        {
            lastTemp=false;
            if(chessBoard.getChessState(currentX, currentY)!=3)return;
            else  chessBoard.setChess(currentX, currentY, turn);
        }
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }
    
    public void mouseDragged(MouseEvent e)
    {
    }

    public void mouseMoved(MouseEvent e)
    {
        if(chessBoard.getTurn()==false)return;

        double x,y;
        x=(e.getX()-25)*1.0/gap;
        y=(e.getY()-25)*1.0/gap;

       
        
       tempX=(int)Math.ceil(x*1.0);
       tempY=(int)Math.ceil(y*1.0);

        if(tempX-x<=0.35&&tempY-y<=0.35);
        else if(tempX-x<=0.35&&tempY-y>=0.65)tempY-=1;
        else if(tempX-x>=0.65&&tempY-y<=0.35)tempX-=1;
        else if(tempX-x>=0.35&&tempY-y>=0.65){tempX-=1;tempY-=1;}
        else
        {
            if(lastTemp==true)
            {
                chessBoard.setChess(currentX, currentY, 0);
                lastTemp=false;
            }
            return;
        }

        if(lastTemp==true)
        {
            if(tempX==currentX&&tempY==currentY)return;
            else    
            {
                chessBoard.setChess(currentX, currentY, 0);
            }
        }
        
        currentX=tempX;currentY=tempY;
        
        if(chessBoard.getChessState(currentX, currentY)==0)
        {
            chessBoard.setChess(currentX, currentY, 3);
            lastTemp=true;
        }
        else lastTemp=false;

    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
    }

    public void restart()
    {
        currentX=currentY=-1;
        lastTemp=false;
    }
    public void setTurn(int x)
    {
        turn=x;restart();
    }
    
}