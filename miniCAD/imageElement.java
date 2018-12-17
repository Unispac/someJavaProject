import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class imageElement
{
    private String imagesource;
    private double height=200,width=200;
    private double x=0,y=0;
    private Image image;
    private boolean valid=true;

    imageElement(String source)
    {
        imagesource=source;
        if(source==null){valid=false;return;}

        ImageIcon imageIcon = new ImageIcon(source);
        image = imageIcon.getImage();
        width=image.getWidth(miniCAD.canva);
        height=image.getHeight(miniCAD.canva);
        //System.out.println(width+" "+height);
        if(width==-1||height==-1)valid=false;
        if(width>500||height>500)
        {
            double scalefactor = Math.max(width/500.0,height/500.0);
            width/=scalefactor;
            height/=scalefactor;
        }
    }
    imageElement(String source,double x,double y,double height,double width)
    {
        imagesource=source;
        if(source==null){valid=false;return;}

        ImageIcon imageIcon = new ImageIcon(source);
        image = imageIcon.getImage();
        this.x=x;this.y=y;
        this.height=height;this.width=width;
        if(width==-1||height==-1)valid=false;
        if(width>500||height>500)
        {
            double scalefactor = Math.max(width/500.0,height/500.0);
            width/=scalefactor;
            height/=scalefactor;
        }
    }
    public void draw(Graphics h)
    {
        h.drawImage(image, (int)x, (int)y, (int)width, (int)height, miniCAD.canva);
    }
    public boolean  checkChosen(int x,int y)
    {
        if(x<this.x||x>this.x+width)return false;
        if(y<this.y||y>this.y+height)return false;
        return true;
    }
    public void move(double x,double y)
    {
        this.x+=x;
        this.y+=y;
        return;
    }
    public void scale(int scalecode)
    {
        double scalefactor=0.95;
        if(scalecode==0)
        {
            width*=scalefactor;
            height*=scalefactor;
        }
        else
        {
            scalefactor=1/scalefactor;
            width*=scalefactor;
            height*=scalefactor;
        }
    }
    public boolean isValid()
    {
        return valid;
    }
    public String toString()
    {
        StringBuffer buffer= new StringBuffer();
        buffer.append(imagesource+"\r");
        buffer.append(x+" "+y+" ");
        buffer.append(height+" "+width);        
        return buffer.toString();
    }
}