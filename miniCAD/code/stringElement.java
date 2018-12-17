import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class stringElement
{
    private static FontRenderContext frc = new FontRenderContext(new AffineTransform(),true,true);

    private String content;
    private int x,y;
    private boolean chosen=false;
    private boolean underModify=false;
    private Color textColor;
    private Font textFont;
    private Rectangle rec;

    stringElement(String s,int x,int y)
    {
        content=s;
        this.x=x;
        this.y=y;
        textColor=miniCAD.currentColor;
        textFont = new Font(miniCAD.fontType, miniCAD.fontStyle, miniCAD.fontSize);
        rec= textFont.getStringBounds(content, frc).getBounds();
    }

    stringElement(String s,int x,int y,Color inColor,String type,int style,int size)
    {
        content=s;
        this.x=x;this.y=y;
        textColor=inColor;
        textFont = new Font(type, style, size);
        rec= textFont.getStringBounds(content, frc).getBounds();
    }
    public void draw(Graphics h)
    {
       if(underModify==true)return;
        Graphics2D g= (Graphics2D) h;
        g.setFont(textFont);
        g.setColor(textColor);
        if(chosen==true)
        {
            g.setStroke(miniCAD.chosenBrushSize[1]);
            g.drawRect(x-5, y+5,rec.width+10, rec.height+5);
        }
        g.drawString(content, x, y+rec.height);
        g.setColor(miniCAD.currentColor);
    }
    public boolean checkChosen(int x,int y)
    {
        if(x<this.x||x>this.x+rec.width)return false;
        if(y<this.y||y>this.y+rec.height)return false;
        return true;
    }
    public void choose()
    {
        chosen=true;
    }
    public void unchoose()
    {
        chosen=false;
    }
    public void modify()
    {
        underModify=true;
    }
    public void unmodify()
    {
        underModify=false;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public void modifyText(String newText)
    {
        content=newText;
        rec= textFont.getStringBounds(content, frc).getBounds();
    }
    public void move(int x,int y)
    {
        this.x+=x;
        this.y+=y;
        rec.x+=x;
        rec.y+=y;
    }
    public void updateFontAttrib()
    {
        textFont = new Font(miniCAD.fontType, miniCAD.fontStyle, miniCAD.fontSize);
        rec= textFont.getStringBounds(content, frc).getBounds();
    }
    public void setColor(Color x)
    {
        textColor=x;
    }
    public String toString()
    {
        StringBuffer buffer= new StringBuffer();
        buffer.append(content+"\r");
        buffer.append(x+" "+y+" ");
        buffer.append(textColor.getRed()+" "+textColor.getGreen()+" "+textColor.getBlue()+" ");
        buffer.append(textFont.getFamily()+" "+textFont.getStyle()+" "+textFont.getSize());
        return buffer.toString();
    }
    public String getContentString()
    {
        return content;
    }
}