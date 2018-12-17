import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.*;
public class goBang
{
    static  JFrame window;
    static  myCanva chessBoard;
    static  JMenuBar menuBar;
    static myMouseListener mouseListener;
    static double gap=39.3;
    
    static void setMenu()
    {
        menuBar=new JMenuBar();
        menuBar.setBounds(0, 0, 615,30 );
        menuBar.setBackground(new Color(220,220,220));

        JMenu optionMenu = new JMenu("option");

        JMenuItem restartItem = new JMenuItem("restart");
        restartItem.addActionListener(new restartAction());

        JMenuItem humanFirstItem = new JMenuItem("Human First");
        humanFirstItem.addActionListener(new setFirstAction(1));

        JMenuItem aiFirstItem = new JMenuItem("AI First");
        aiFirstItem.addActionListener(new setFirstAction(2));

        JMenuItem quitItem =new JMenuItem("quit");
        quitItem.addActionListener(new quitAction());

        window.add(menuBar);
        menuBar.add(optionMenu);
        optionMenu.add(restartItem);
        optionMenu.add(aiFirstItem);
        optionMenu.add(humanFirstItem);
        optionMenu.add(quitItem);
    }
    static void setChessBoard()
    {
        chessBoard=new myCanva(".\\resource\\chessBoard.jpg",".\\resource\\blackChess.png",".\\resource\\whiteChess.png",600,600);
        chessBoard.setBounds(0, 30, 615, 638);
        mouseListener = new myMouseListener(chessBoard);
        chessBoard.addMouseListener(mouseListener);
        chessBoard.addMouseMotionListener(mouseListener);
        chessBoard.addMouseWheelListener(mouseListener);

        window.add(chessBoard);
    }
    public static void main(String[] args) 
    {
        window = new JFrame();
        window.setTitle("goBang");
        window.setSize(615,668);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);

        setChessBoard();
        setMenu();
        
        chessBoard.repaint();
        window.setVisible(true);
    }
}

class restartAction implements ActionListener
{
    public void actionPerformed(ActionEvent e)
    {
        goBang.mouseListener.restart();
        goBang.chessBoard.restart();
    }
}
class setFirstAction implements ActionListener
{
    private int type;
    setFirstAction(int type)
    {
        this.type=type;
    }
    public void actionPerformed(ActionEvent e)
    {
            goBang.mouseListener.setTurn(type);
            if(type==1)goBang.chessBoard.setHmFirst();
            else goBang.chessBoard.setAiFirst();   
    }
}

class quitAction implements ActionListener
{
    public void actionPerformed(ActionEvent e)
    {
            System.exit(0);
    }
}