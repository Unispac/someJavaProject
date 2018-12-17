import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

public class myCanva extends JPanel
{
    private ArrayList<Primative> element = new ArrayList<Primative>();
    private ArrayList<stringElement> text = new ArrayList<stringElement>();
    private ArrayList<imageElement> image = new ArrayList<imageElement>();
    private Primative currentPrimative=null;
    private Color BLACK=new Color(0,0,0);
    private Color WHITE =new Color(255,255,255);
    private Color CLEAR = new Color(220,220,220);
    private boolean isOpen=false;
    private stringElement targetText=null;
    
  

    protected void paintComponent(Graphics h)
    {
        h.setColor(CLEAR);
        h.fillRect(0, 0, 800, 600);
        h.setColor(BLACK);
        for(imageElement x : image)x.draw(h);
        for(Primative x : element)x.draw(h);
        for(stringElement x : text)x.draw(h);
        
    }
    void pushPrimative(Primative x)
    {
        element.add(x);
        this.confirm();
        currentPrimative=x;
    }
    void pushImage(imageElement x)
    {
        if(x.isValid()==true)image.add(x);
    }
    void confirm()
    {
        if(currentPrimative!=null)
        {
            if(currentPrimative.valid()==true)currentPrimative.confirm();
            else element.remove(element.size()-1);
            currentPrimative=null;
        }
    }
    Primative getCurrentPrimative()
    {
        return currentPrimative;
    }
    public void delete(Primative x)
    {
        element.remove(x);
        repaint();
    }
    public void delete(stringElement x)
    {
        text.remove(x);
        repaint();
    }
    public void clear()
    {
        element.clear();
        text.clear();
        image.clear();
    }
    public Primative checkChosenPrimative(Point x)
    {
        for(Primative k : element)
        {
            if(k.checkChosen(x)==true)return k;
        }
        return null;
    }
    public void importFile(String filePath)
    {
            Primative tempPrimative;
            stringElement tempText;
            imageElement tempImage;
            int size;
            try
            {
                Scanner in =  new Scanner(new BufferedReader(new FileReader(filePath)));   
                int cnt;
                cnt=in.nextInt();
                for(int i=0;i<cnt;i++)
                {
                    tempPrimative=new Primative(in.next(), in.nextBoolean());
                    tempPrimative.setSize(in.nextInt());
                    tempPrimative.setColor(new Color(in.nextInt(),in.nextInt(),in.nextInt()));
                    size=in.nextInt();
                    for(int j=0;j<size;j++)
                    {
                        tempPrimative.push(new Point(in.nextDouble(),in.nextDouble()));
                    }
                    element.add(tempPrimative);  
                } 

                cnt=in.nextInt();
                for(int i=0;i<cnt;i++)
                {
                    in.nextLine();
                    tempText=new stringElement
                        (   in.nextLine(), in.nextInt(), in.nextInt(), 
                            new Color(in.nextInt(),in.nextInt(),in.nextInt()), in.next(), in.nextInt(), in.nextInt()
                        );
                    text.add(tempText);
                }

                cnt=in.nextInt();
                for(int i=0;i<cnt;i++)
                {
                    in.nextLine();
                    tempImage=new imageElement(in.nextLine(), in.nextDouble(), in.nextDouble(), in.nextDouble(), in.nextDouble());
                    pushImage(tempImage);
                }
                in.close();
            }catch(IOException e)
            {
                System.out.println("Failed to import the graphics file!");
                e.printStackTrace();
            }finally
            {
                repaint();
            }
    }
    
    public void outportFile(String filePath)
    {
        try
        {
            PrintWriter out = new PrintWriter(filePath);
            
            out.println(element.size());
            for(Primative k : element)
            {
                out.println(k);
            
            }
            out.println(text.size());
            for(stringElement k : text)
            {
                out.println(k);
            }
            out.println(image.size());
            for(imageElement k : image)
            {
                out.println(k);
            }

            out.close();
        }catch(IOException e)
        {
            System.out.println("Failed to outport the graphics file!");
            e.printStackTrace();
        }
    }
    public void newText(int x,int y)
    {
        miniCAD.textBlock.setLocation(x, y);
        miniCAD.textBlock.setText("");
        miniCAD.textBlock.setVisible(true);
    }
    public void confirmText()
    {
        miniCAD.textBlock.setVisible(false);
        if(miniCAD.textBlock.getText().isEmpty())return;
        text.add(new stringElement(miniCAD.textBlock.getText(),miniCAD.textBlock.getX(),miniCAD.textBlock.getY()));
        repaint();
    }
    public void openText()
    {
        if(targetText==null)return;
        targetText.modify();
        miniCAD.textBlock.setLocation(targetText.getX(), targetText.getY());
        miniCAD.textBlock.setText(targetText.getContentString());
        miniCAD.textBlock.setVisible(true);
        isOpen=true;
        repaint();
    }
    public void closeText()
    {
        if(targetText==null)return;
        String newString = miniCAD.textBlock.getText();
        if(newString.isEmpty())text.remove(targetText);
        else targetText.modifyText(newString);
        targetText.unmodify();
        targetText=null;
        miniCAD.textBlock.setVisible(false);
        isOpen=false;
        repaint();
    }
    public void chooseText(stringElement x)
    {   
        if(x==null)return;
        targetText=x;
        x.choose();
        repaint();
    }
    public void unchooseText()
    {
        if(targetText==null)return;
        targetText.unchoose();
        if(isOpen)closeText();
        targetText=null;
        repaint();
    }
    public void moveText(int x,int y)
    {
        if(targetText!=null)
        {
            miniCAD.textBlock.setLocation(miniCAD.textBlock.getX()+x,miniCAD.textBlock.getY()+y);
            targetText.move(x,y);
        }
    }
    public void remove(imageElement x)
    {
        image.remove(x);
        repaint();
    }
    public stringElement checkChosenText(int x,int y)
    {
        for(stringElement k : text)
        {
            if(k.checkChosen(x, y))return k;
        }
        return null;
    }
    public imageElement checkChosenImage(int x,int y)
    {
        imageElement k;
        for(int i = image.size()-1;i>=0;i--)
        {
            k=image.get(i);
            if(k.checkChosen(x, y))return k;
        }
        return null;
    }
}