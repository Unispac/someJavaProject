import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
public class Primative
{

    private static Point a=new Point();
    private static Point b=new Point();

    private String type;
    private ArrayList<Point>content;
    private Polygon polygon;
    private boolean filled=true;
    private Color primativeColor;
    private int primativeFont;
    private boolean chosen=false;
    private boolean confirmed=false;


    Primative(String type)
    {
        this.type=type;
        if(type=="polygon")polygon=new Polygon();
        content=new ArrayList<Point>();
        primativeColor=miniCAD.currentColor;
        primativeFont=miniCAD.currentStroke;
    }
    Primative(String type,boolean filled)
    {
        this.filled=filled;
        this.type=type;
        if(type=="polygon")polygon=new Polygon();
        content=new ArrayList<Point>();
        primativeColor=miniCAD.currentColor;
        primativeFont=miniCAD.currentStroke;
    }
    void choose()
    {
        chosen=true;
    }
    void unChoose()
    {
        chosen=false;
    }
    String getType()
    {
        return type;
    }
    public void setColor(Color x)
    {
        primativeColor=x;
        return;
    }
    public void setSize(int x)
    {
        primativeFont=x;
        return;
    }
    boolean getFilled()
    {
        return filled;
    }
    boolean valid()
    {
        if(content.size()<2)return false;
        else return true;
    }
    void push(Point x)
    {
        content.add(x);
    }
    void pop()
    {
        content.remove(content.size()-1);
    }
    public void move(double x,double y)
    {
        for(Point k : content){k.x+=x;k.y+=y;}
        if(type=="polygon")
        {
            int size = content.size();
            for(int i=0;i<size;i++)
            {
                polygon.xpoints[i]+=x;
                polygon.ypoints[i]+=y;
            }
        }
    }
    void modify(int x,int y)
    {
        if(confirmed==false)
        {
            Point temp=content.get(content.size()-1);
            temp.x=x;
            temp.y=y;
        }
    }
    void confirm()
    {
        if(type=="polygon")
        for(Point x : content)
        {
            polygon.addPoint((int)x.x,(int)x.y);
        }
        confirmed=true;
    }
    void draw(Graphics h)
    {
        Graphics2D x= (Graphics2D) h;

        if(chosen==false)x.setStroke(miniCAD.brushSize[primativeFont]);
        else x.setStroke(miniCAD.chosenBrushSize[primativeFont]);
        x.setColor(primativeColor);

        switch(type)
        {
            case "line":
                if(content.size()<2)break;
                else
                {
                    x.drawLine((int)content.get(0).x,(int)content.get(0).y, (int)content.get(1).x, (int)content.get(1).y);
                }
                break;
            case "rec" :
                if(content.size()<2)break;
                else
                {
                    int x1=(int)Math.min(content.get(0).x, content.get(1).x);
                    int x2=(int)Math.max(content.get(0).x,content.get(1).x);
                    int y1=(int)Math.min(content.get(0).y, content.get(1).y);
                    int y2=(int)Math.max(content.get(0).y,content.get(1).y);
                    x2-=x1;y2-=y1;

                    if(chosen==false&&filled==true)x.fillRect(x1,y1,x2,y2);
                    else x.drawRect(x1,y1,x2,y2);
                }
                break;
            case "lines":
                if(content.size()<2)break;
                else
                {
                    int size=content.size();
                    Point now=content.get(0),next;
                    for(int i=1;i<size;i++)
                    {
                        next=content.get(i);
                        x.drawLine((int)now.x,(int)now.y,(int)next.x,(int)next.y);
                        now=next;
                    }
                }
                break;
            case "ellipse":
                if(content.size()<2)break;
                else
                {
                    int x1=(int)Math.min(content.get(0).x, content.get(1).x);
                    int x2=(int)Math.max(content.get(0).x,content.get(1).x);
                    int y1=(int)Math.min(content.get(0).y, content.get(1).y);
                    int y2=(int)Math.max(content.get(0).y,content.get(1).y);
                    x2-=x1;y2-=y1;

                    if(chosen==false&&filled==true)x.fillOval(x1,y1,x2,y2);
                    else x.drawOval(x1,y1,x2,y2);
                }
                break;
            case "polygon":
                if(confirmed==false)
                {
                    if(content.size()<2)break;
                    else
                    {
                        int size=content.size();
                        Point now=content.get(0),next;
                        for(int i=1;i<size;i++)
                        {
                            next=content.get(i);
                            x.drawLine((int)now.x,(int)now.y,(int)next.x,(int)next.y);
                            now=next;
                        }
                        x.drawLine((int)content.get(size-1).x, (int)content.get(size-1).y, (int)content.get(0).x, (int)content.get(0).y);
                    }
                }
                else
                {
                    if(content.size()<2)break;
                    else
                    {
                        if(chosen==false&&filled==true)x.fillPolygon(polygon);
                        else x.drawPolygon(polygon);
                    }
                }
        }
        x.setColor(miniCAD.currentColor);
        x.setStroke(miniCAD.brushSize[primativeFont]);
    }
    boolean checkChosen(Point x)
    {
        switch(type)
        {
            //System.out.println(type);
            case "line":
                a.x=content.get(0).x-x.x;
                a.y=content.get(0).y-x.y;
                b.x=content.get(1).x-x.x;
                b.y=content.get(1).y-x.y;
                if(Math.abs(Math.abs(Point.angle(a, b))-180.0)<20.0)return true;
                else return false;
            case "lines":
                Point now = content.get(0);
                Point next;
                int size = content.size();
                for(int i=1;i<size;i++)
                {
                    next=content.get(i);
                    a.x=next.x-x.x;a.y=next.y-x.y;
                    b.x=now.x-x.x;b.y=now.y-x.y;

                    //System.out.println(Point.angle(a,b));
                    if(180.0-Point.angle(a, b)<20.0)
                    {
                        return true;
                    }
                    now = next;
                }
                return false;
            case "ellipse":
                a.x=(int)Math.min(content.get(0).x, content.get(1).x);
                b.x=(int)Math.max(content.get(0).x, content.get(1).x);
                a.y=(int)Math.min(content.get(0).y, content.get(1).y);
                b.y=(int)Math.max(content.get(0).y, content.get(1).y);
                if(x.x>=a.x&&x.x<=b.x&&x.y>=a.y&&x.y<=b.y)return true;
                else return false;
            case "rec":
                a.x=(int)Math.min(content.get(0).x, content.get(1).x);
                b.x=(int)Math.max(content.get(0).x, content.get(1).x);
                a.y=(int)Math.min(content.get(0).y, content.get(1).y);
                b.y=(int)Math.max(content.get(0).y, content.get(1).y);
                if(x.x>=a.x&&x.x<=b.x&&x.y>=a.y&&x.y<=b.y)return true;
                else return false;
            case "polygon":
                Point front  = content.get(0);
                Point back;
                int cnt = content.size();
                double angle=0;
                for(int i=1;i<cnt;i++)
                {
                    back = content.get(i);
                    a.x=front.x-x.x;a.y=front.y-x.y;
                    b.x=back.x-x.x;b.y=back.y-x.y;
                    angle += Point.dirAngle(a, b);
                    front=back;
                }
                back = content.get(0);
                a.x=front.x-x.x;a.y=front.y-x.y;
                b.x=back.x-x.x;b.y=back.y-x.y;
                angle += Point.dirAngle(a, b);
                if(Math.abs(angle)>180.0)return true;
                else return false;
        }
        return false;
    }
    public void setFill(boolean fill)
    {
        filled=fill;
    }
    public void scale(int expand,int x,int y)
    {
        double scalenum=(expand==1)?(1.0/0.9):0.9;
        for(Point k : content)
        {
            k.x=(k.x-x)*scalenum+ x;
            k.y=(k.y-y)*scalenum+y;
        }
        if(type=="polygon")
        {
            int size = content.size();
            for(int i=0;i<size;i++)
            {
                polygon.xpoints[i]=(int)content.get(i).x;
                polygon.ypoints[i]=(int)content.get(i).y;
            }
        }
    }
    public String toString()
    {
        StringBuffer x= new StringBuffer();
          /*
    private ArrayList<Point>content;
    private Polygon polygon;
    */
        x.append(type+" ");
        x.append(filled+" ");
        x.append(primativeFont+" ");
        x.append(primativeColor.getRed()+" ");
        x.append(primativeColor.getGreen()+" ");
        x.append(primativeColor.getBlue()+" ");
        x.append(content.size()+" ");
        for(Point k : content)
        {
            x.append(k.x+" "+k.y+" ");
        }
        return x.toString();
    }
}