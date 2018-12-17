import javax.lang.model.util.ElementScanner6;
import javax.swing.*;

import javafx.stage.FileChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList; 

public class miniCAD
{
    static private JFrame frame;
    static  JPanel buttonPanel;
    static myCanva canva;
    static Mouse listener;
    static JTextField textBlock = new JTextField(8);

    static boolean filled=false;
    static String state="choose";
    static int fontStyle=0;
    static int fontSize=25;
    static String fontType="楷体";

    static Color currentColor=new Color(0, 0, 0);
    static Color  menuColor = new Color(205,92,92);
    static int currentStroke = 1;
    static Stroke chosenBrushSize[] = new Stroke[11];
    static Stroke brushSize[] = new Stroke[11];

    public static void main(String[] args) 
    {
        frame = new JFrame();
        frame.setTitle("miniCAD");
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CrateMenuBar();
        LayoutPanel();
        frame.setVisible(true);
        textBlock.setVisible(false);
    }
    static void enumFonts(JMenuBar target)
    {
        GraphicsEnvironment e = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        String [] fontnames = e.getAvailableFontFamilyNames();
        JComboBox theType = new JComboBox<String>(fontnames);
        theType.setPreferredSize(new Dimension(300,25));
        theType.setBackground(menuColor);
        theType.addItemListener(new fontTypeAction(theType));
        theType.setSelectedItem("楷体");
        target.add(theType);
    }

    static void CrateMenuBar()
    {
        JMenuBar menubar = new JMenuBar();
        menubar.setBackground(menuColor);
        frame.setJMenuBar(menubar);
        

        JMenu fileMenu = new JMenu("File");
        menubar.add(fileMenu);
        

        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new loadFile());
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new saveFile());
        fileMenu.add(saveItem);

        JMenuItem loadImage = new JMenuItem("Load Image");
        loadImage.addActionListener(new loadImage());
        fileMenu.add(loadImage);

        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new quitProgram());
        fileMenu.add(quitItem);

        JMenu colorMenu = new JMenu("Color");
        menubar.add(colorMenu);
        colorMenu.setLayout(null);
        ColorBoard black = new ColorBoard(colorMenu, new Color(0,0,0));
        ColorBoard white = new ColorBoard(colorMenu,new Color(255,255,255));
        ColorBoard grey =  new ColorBoard(colorMenu, new Color(180,180,180));
        ColorBoard red = new ColorBoard(colorMenu,new Color(255,0,0));
        ColorBoard orange = new ColorBoard(colorMenu, new Color(255,156,0));
        ColorBoard yellow = new ColorBoard(colorMenu, new Color(255,255,0));
        ColorBoard green = new ColorBoard(colorMenu, new Color(0, 255, 0));
        ColorBoard cyan = new ColorBoard(colorMenu, new Color(0,255,255));
        ColorBoard blue = new ColorBoard(colorMenu,new Color(0,0,255));
        ColorBoard purple = new ColorBoard(colorMenu, new Color(255,0,255)); 

        JMenu lineMenu = new JMenu("Size");
        menubar.add(lineMenu);
        for(int i=1;i<=10;i++)
        {
            lineMenu.add(new FontBoard(i));
        }

        JMenu fontSizeMenu = new JMenu("fontSize");
        menubar.add(fontSizeMenu);
        for(int i=1;i<=20;i++)
        {
            JMenuItem temp = new JMenuItem("size : "+i);
            temp.addActionListener(new fontSizeAction(4*i));
            fontSizeMenu.add(temp);
        }


        JMenu fontStyleMenu = new JMenu("fontStyle");
        menubar.add(fontStyleMenu);
        JMenuItem PLAIN = new JMenuItem("Plain");
        PLAIN.addActionListener(new fontStyleAction(0));
        JMenuItem BOLD = new JMenuItem("Bold");
        BOLD.addActionListener(new fontStyleAction(1));
        JMenuItem ITALIC = new JMenuItem("Italic");
        ITALIC.addActionListener(new fontStyleAction(2));

        fontStyleMenu.add(PLAIN);
        fontStyleMenu.add(BOLD);
        fontStyleMenu.add(ITALIC);

        enumFonts(menubar);
    }

    static void InsertButton(JButton x,JPanel y)
    {

        Color frontColor = new Color(255,255,255);
        Color backColor =new Color(99,184,255);
        x.setForeground(frontColor);
        x.setBackground(backColor);
        y.add(x);
        y.repaint();
    }

    static void LayoutPanel()
    {

        frame.setLayout(null);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0,139,139));
        buttonPanel.setSize(150,550);
        buttonPanel.setLocation(0, 0);
        
        canva=new myCanva();
        canva.setBackground(new Color(220,220,220));
        canva.setSize(650, 550);
        canva.setLocation(150, 0);


        listener = new Mouse(canva);
        canva.addMouseListener(listener);
        canva.addMouseMotionListener(listener);
        canva.addMouseWheelListener(listener);
        
        textBlock.addKeyListener(new textConfirm());
        canva.add(textBlock);
    

        buttonPanel.setLayout(null);

        JButton buttonLine = new JButton("线段");
        buttonLine.addActionListener(new buttonEvent("line"));
        buttonLine.setBounds(20, 25, 110, 30);
        InsertButton(buttonLine, buttonPanel);
        
        JButton buttonRec = new JButton("矩形");
        buttonRec.addActionListener(new buttonEvent("rec"));
        buttonRec.setBounds(20,80, 110, 30);
        InsertButton(buttonRec, buttonPanel);
       
        JButton buttonEllipse = new JButton("椭圆");
        buttonEllipse.addActionListener(new buttonEvent("ellipse"));
        buttonEllipse.setBounds(20,135, 110, 30);
        InsertButton(buttonEllipse, buttonPanel);


        JButton buttonLines = new JButton("折线");
        buttonLines.addActionListener(new buttonEvent("lines"));
        buttonLines.setBounds(20,190, 110, 30);
        InsertButton(buttonLines, buttonPanel);

        JButton buttonPoly = new JButton("多边形");
        buttonPoly.addActionListener(new buttonEvent("polygon"));   
        buttonPoly.setBounds(20,245, 110, 30);
        InsertButton(buttonPoly, buttonPanel);
        
        JButton buttonText = new JButton("文本");
        buttonText.addActionListener(new buttonEvent("text"));
        buttonText.setBounds(20,300, 110, 30);
        InsertButton(buttonText,buttonPanel);

        JButton buttonChoose = new JButton("选择");
        buttonChoose.addActionListener(new buttonEvent("choose"));
        buttonChoose.setBounds(20,355, 110, 30);
        InsertButton(buttonChoose, buttonPanel);

        JButton buttonClear = new JButton("清空");
        buttonClear.addActionListener(new clearSignal());
        buttonClear.setBounds(20,410, 110, 30);
        InsertButton(buttonClear, buttonPanel);

        

        JCheckBox buttonFilled = new JCheckBox("填充");
        buttonFilled.addActionListener(new checkBoxEvent());
        buttonFilled.setBounds(40,465, 70, 40);
        buttonPanel.add(buttonFilled);

        frame.add(buttonPanel);
        frame.add(canva);
    }
}



class checkBoxEvent implements ActionListener
{
    public void actionPerformed(ActionEvent e)
    {
        miniCAD.filled=(!miniCAD.filled);
    }
}
class clearSignal implements ActionListener
{
    public void actionPerformed(ActionEvent e)
    {
        miniCAD.canva.clear();
        miniCAD.canva.repaint();
    }
}
class buttonEvent implements ActionListener
{
    private String statename;
    public buttonEvent(String x)
    {
        statename = x;
    }
    public void actionPerformed(ActionEvent event)
    {
        miniCAD.state=statename;
        miniCAD.listener.onChangeState();
    }
}
class FontBoard extends JMenuItem implements ActionListener
{
    private static float[] arr = {4.0f,2.0f};
    int id;
    FontBoard(int x)
    {
        super("size : "+x);
        miniCAD.brushSize[x]=new BasicStroke(x);
        miniCAD.chosenBrushSize[x]= new BasicStroke(x,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1.0f,arr,0); 
        this.addActionListener(this);
        id=x;
    }
    public void actionPerformed(ActionEvent e)
    {
        miniCAD.currentStroke=id;
        miniCAD.listener.changeSize(id);
        miniCAD.canva.repaint();
    }
}
class ColorBoard extends JMenuItem implements ActionListener
{
    private Color color;
    ColorBoard(JMenu x,Color color)
    {   
        this.setPreferredSize(new Dimension(100, 20));
        this.color=color;
        this.addActionListener(this);
        x.add(this);
    }
    protected void paintComponent(Graphics h)
    {
        h.setColor(color);
        h.fillRect(0, 0,100, 20);
    }
    public void actionPerformed(ActionEvent e)
    {
        miniCAD.currentColor=color;
        miniCAD.listener.changeColor(color);
    }
}
class loadFile implements ActionListener
{
    private File x;
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser FileSelector= new JFileChooser(". \\");
        FileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileSelector.showOpenDialog(new JLabel("导入"));

        x=FileSelector.getSelectedFile();
        if(x!=null)miniCAD.canva.importFile(x.getAbsolutePath());
    }
}
class saveFile implements ActionListener
{
    private File x;

    public void actionPerformed(ActionEvent e)
    {
        JFileChooser FileSelector = new JFileChooser(". \\");
        FileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);    
        FileSelector.showSaveDialog(new JLabel("导出"));
        
        x=FileSelector.getSelectedFile();
        if(x!=null)miniCAD.canva.outportFile(x.getAbsolutePath());
    }
}
class loadImage implements ActionListener
{
    private File x;
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser FileSelector = new JFileChooser(". \\");
        FileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);    
        FileSelector.showOpenDialog(new JLabel("读取"));
        x=FileSelector.getSelectedFile();
        if(x!=null)miniCAD.canva.pushImage(new imageElement(x.getAbsolutePath()));
        miniCAD.canva.repaint();
    }
}
class quitProgram implements ActionListener
{
    public void actionPerformed(ActionEvent e)
    {
        System.exit(0);
    }
}
class textConfirm implements KeyListener
{
    public void keyTyped(KeyEvent e) 
    {
        // TODO Auto-generated method stub
    }


    public void keyPressed(KeyEvent e) 
    {
        if(e.getKeyCode()==10)miniCAD.listener.onChangeState();
    }


    public void keyReleased(KeyEvent e) {

    }
}
class fontStyleAction implements ActionListener 
{
    private int style;
    fontStyleAction(int style)
    {
        this.style=style;
    }
    public void actionPerformed(ActionEvent e)
    {
        miniCAD.fontStyle=style;
        if(miniCAD.listener!=null)miniCAD.listener.onStringStateChange();
    }
}
class fontSizeAction implements ActionListener
{
    private int size;
    fontSizeAction(int size)
    {
        this.size=size;
    }
    public void actionPerformed(ActionEvent e)
    {
        miniCAD.fontSize=size;
        if(miniCAD.listener!=null)miniCAD.listener.onStringStateChange();
    }
}
class fontTypeAction implements ItemListener
{
    private JComboBox box=null;
    fontTypeAction(JComboBox box)
    {
        this.box=box;
    }
    public void itemStateChanged(ItemEvent e)
    {
            if(e.getStateChange() == ItemEvent.SELECTED)
            {
                    miniCAD.fontType=(String)box.getSelectedItem();
                    if(miniCAD.listener!=null)miniCAD.listener.onStringStateChange();
             }
    }  
}