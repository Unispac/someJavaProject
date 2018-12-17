import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class easyDraw
{
    public static void main(String[] args) 
    {
        JFrame frame = new JFrame();
        frame.setTitle("easyDraw");
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setBackground(new Color(220,220,220));

        myCanva canva = new myCanva();
        frame.add(canva);

        myMouse listener = new myMouse();
        listener.setCanva(canva);

        canva.addMouseListener(listener);
        canva.addMouseMotionListener(listener);
        canva.addMouseWheelListener(listener);
        
        System.out.println("Press Left Button to draw black line , right button to draw white line.");
        System.out.println("The slower the cursor move , the thicker the line is.");
        System.out.println("Press the middle button to clear the canva.");
    
        
        return;
    }
}
class myMouse implements MouseListener,MouseMotionListener,MouseWheelListener
{
    private int BUTTON=0;
    private myCanva canva;
    private int gap=0;
    private long temp=0 , init_time=0;

    public void setCanva(myCanva x)
    {
        canva=x;
    }
    public void mouseClicked(MouseEvent e)
    {
        //System.out.println("Clicked");
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e)
    {
        BUTTON=e.getButton();
        if(canva==null)return;

        if(BUTTON==1||BUTTON==3)
        {
            canva.setType(BUTTON);
        }
        if(BUTTON==2)
        {
            System.out.println("clear!!");
            canva.clear();
            canva.repaint();
            gap=0;
        }
    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e)
    {
        //System.out.println("Released");
            //DOWN=0;
        if(canva!=null)
        {
            canva.endLine();
            BUTTON=0;
        }
    }

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e)
    {
        if(canva!=null)
        {
            canva.endLine();
            BUTTON=0;
        }
    }

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e)
    {
        //System.out.println("Exited");
        //DOWN=0;
        if(canva!=null)
        {
            canva.endLine();
            BUTTON=0;
        }
    }
    

    public void mouseDragged(MouseEvent e)
    {
        //System.out.println(e.getButton()==e.BUTTON1);
        //System.out.println("Draged");
        //System.out.println(BUTTON);
        gap++;
        if(canva!=null&&gap==5)
        {
            gap=0;
            if(BUTTON==1||BUTTON==3)
            {
                temp=System.currentTimeMillis();
                canva.addPoint( e.getX(), e.getY(),temp-init_time);
                init_time=temp;
                canva.repaint();
            }
        }
    }

        /**
         * Invoked when the mouse cursor has been moved onto a component
         * but no buttons have been pushed.
         */
    public void mouseMoved(MouseEvent e)
    {
        //System.out.println("Moved");
    }
    
           /**
     * Invoked when the mouse wheel is rotated.
     * see MouseWheelEvent
     */
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        //System.out.println("Wheeled");
    }
}
class myPoint
{
    public int x,y;
    public long time;
    myPoint(int a,int b,long d)
    {
        x=a;y=b;
        time=d;
    }
}
class myCanva extends JPanel
{
    private ArrayList<myPoint> content = new ArrayList<myPoint>(); 

    private Color BLACK=new Color(0,0,0);
    private Color WHITE =new Color(255,255,255);
    private Color CLEAR = new Color(220,220,220);
    private Stroke size[] = new BasicStroke[100];
    private int type=1;
    private int clearCanva=0;

    myCanva()
    {
        
        for(int i=1;i<=60;i++)size[i]=new BasicStroke((float)(0.5+(4.0*Math.sin((float)(i*1.5)*Math.PI/180.0))));
        size[0]=size[1];
    }
    protected void paintComponent(Graphics h)
    {
        if(clearCanva==1)
        {
            h.setColor(CLEAR);
            h.fillRect(0, 0, 800, 600);
            clearCanva=0;
            return;
        }
        Graphics2D g= (Graphics2D)h;
        if(content.isEmpty())return;
        
        myPoint now,next;
        Iterator<myPoint>x;

        x=content.iterator();
        now=x.next();

        if(now!=null)
        {
           if(type==1)g.setColor(BLACK);
           else g.setColor(WHITE);
        }
        
        int speed;

        while(x.hasNext())
        {
            next=x.next();
            speed=(int)(next.time);
            if(speed>60)speed=45;
            g.setStroke(size[speed]);
            g.drawLine(now.x,now.y, next.x, next.y);
           now=next;
        }        
    }
    public void addPoint(int x,int y,long time)
    {
            content.add(new myPoint(x,y,time));
    }
    public void endLine()
    {
         content.clear();
    }
    public void clear()
    {
        content.clear();
        clearCanva=1;
    }
    public void setType(int x)
    {
        type=x;
    }
}