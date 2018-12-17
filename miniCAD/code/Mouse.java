import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Mouse implements MouseListener,MouseMotionListener,MouseWheelListener
{
    private myCanva canva;
    private boolean newPoint=false;
    private int pointcnt=0;
    private Primative chosen=null;
    private imageElement imageTarget=null;
    private stringElement textchosen=null;
    private Point currentPos = new Point();
    private Color chosenColor = new Color(255,0,0);
    private boolean textblock=false;

    public Mouse(myCanva x)
    {
        canva=x;
    }
    public void setCanva(myCanva x)
    {
        canva=x;
    }
    public void changeColor(Color x)
    {
        if(chosen!=null)chosen.setColor(x);
        if(textchosen!=null)textchosen.setColor(x);
        canva.repaint();
    }
    public void changeSize(int x)
    {
        if(chosen!=null)chosen.setSize(x);
    }
    public void mouseClicked(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
        if(chosen!=null)
        {
            //System.out.println("UNCHOSEN!!");
            if(e.getButton()==3)canva.delete(chosen);
            else chosen.unChoose();

            chosen=null;
        }
        if(textchosen!=null)
        {
            if(e.getButton()==3)
            {
                canva.unchooseText();
                canva.delete(textchosen);
                textchosen=null;
            }
        }
        if(imageTarget!=null)
        {
            if(e.getButton()==3)canva.remove(imageTarget);
            imageTarget=null;
        }
        

        if(e.getButton()==1)
        {
            if(miniCAD.state=="choose")
            {
                currentPos.x=e.getX();
                currentPos.y=e.getY();

                stringElement temp=canva.checkChosenText(e.getX(), e.getY());
                if(temp==textchosen)
                {
                    if(temp!=null)canva.openText();
                }
                else
                {
                    canva.unchooseText();
                    textchosen=temp;
                    if(textchosen!=null)canva.chooseText(textchosen);
                }

                if(textchosen==null)
                {
                    chosen = canva.checkChosenPrimative(currentPos);
                    if(chosen!=null)
                    {
                        chosen.choose();
                    }
                    else
                    {
                        imageTarget = canva.checkChosenImage(e.getX(), e.getY());
                    }
                }
            }
            else if(miniCAD.state=="text")
            {
                if(textblock==false)
                {
                    textblock=true;
                    canva.newText(e.getX(),e.getY());
                }
                else
                {
                    textblock=false;
                    canva.confirmText();
                }
            }
            else
            {
                if(canva.getCurrentPrimative()==null)
                {
                    canva.pushPrimative(new Primative(miniCAD.state,miniCAD.filled));
                    canva.getCurrentPrimative().push(new Point(e.getX(), e.getY()));
                    pointcnt++;
                }
                else
                {
                    newPoint=false;
                    pointcnt++;
                    switch(canva.getCurrentPrimative().getType())
                    {
                        case "line" :
                            if(pointcnt==2)canva.confirm();
                            pointcnt=0;
                            break;
                        case "ellipse" :
                            if(pointcnt==2)canva.confirm();
                            pointcnt=0;
                            break;
                        case "rec" :
                            if(pointcnt==2)canva.confirm();
                            pointcnt=0;
                            break;
                    }
                }
            }
        }
        else if(e.getButton()==3)
        {
            if(newPoint==true)
            {
                canva.getCurrentPrimative().pop();
                newPoint=false;
            }
            canva.confirm();
            pointcnt=0;
        }
        canva.repaint();
        
    }
    public void mouseReleased(MouseEvent e)
    {
        if(e.getButton()==1&&canva.getCurrentPrimative()!=null)
        {
            canva.getCurrentPrimative().push(new Point(e.getX(), e.getY()));
            newPoint=true;
        }
    }
    public void mouseEntered(MouseEvent e)
    {
    }
    public void mouseExited(MouseEvent e)
    {
    }
    public void mouseDragged(MouseEvent e)
    {
        if(chosen!=null)
        {
            chosen.move(e.getX()-currentPos.x,e.getY()-currentPos.y);
            canva.repaint();
        }
        if(textchosen!=null)
        {
            canva.moveText((int)(e.getX()-currentPos.x),(int)(e.getY()-currentPos.y));
            canva.repaint();
        }
        if(imageTarget!=null)
        {
            imageTarget.move((double)(e.getX()-currentPos.x),(double)(e.getY()-currentPos.y));
            canva.repaint();
        }
        currentPos.x=e.getX();
        currentPos.y=e.getY();
    }
    public void mouseMoved(MouseEvent e)
    {
        if(newPoint==true)
        {
            canva.getCurrentPrimative().modify(e.getX(), e.getY());
            canva.repaint();
        }
    }
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if(e.getWheelRotation()==1)
        {
            if(chosen!=null){chosen.scale(0,e.getX(),e.getY());canva.repaint();}
            if(imageTarget!=null){imageTarget.scale(0);}
        }
        else
        {
            if(chosen!=null){chosen.scale(1,e.getX(),e.getY());canva.repaint();}
            if(imageTarget!=null){imageTarget.scale(1);}
        }
        canva.repaint();
    }
    public void onChangeState()
    {
        if(textblock==true)
        {
            textblock=false;
            canva.confirmText();
        }
        if(textchosen!=null)
        {
            canva.unchooseText();
            canva.closeText();
            textchosen=null;
        }
        if(imageTarget!=null)
        {
            imageTarget=null;
        }
    }
    public void onStringStateChange()
    {
        if(textchosen!=null)
        {
            textchosen.updateFontAttrib();
            canva.repaint();
        }
    }
}