import java.awt.*;
import javax.swing.*;
public class myCanva extends JPanel
{
    private static double gap=goBang.gap;
    private static String aiWin="AI WINS!";
    private static String humanWin="HUMAN WINS!";
    private static Font textFont=new Font("等线", 1, 30);

    private Image chessBoard;
    private Image blackChess;
    private Image whiteChess;
    private int rows=15,cols=15;
    private int width,height;
    private int rChess;
    private int [][] chessBoardState=new int[rows][cols];
    private Color clearColor = new Color(255, 255, 255);
    private Color tempColor = new Color(0, 255, 0, 150);
    private Color redColor = new Color(255,0,0);
    
    private int aiChess=2,hmChess=1;
    private int turn=1;
    private int winner=0;
    private boolean finished=false;

    private int highLightX=-1,highLightY=-1;
    


    
    myCanva(String chessBoardSource,String blackChessSource,String whiteChessSource,int width,int height)
    {
        ImageIcon imageIcon =new ImageIcon(chessBoardSource);
        chessBoard = imageIcon.getImage();
        imageIcon = new ImageIcon(blackChessSource);
        blackChess=imageIcon.getImage();
        imageIcon=new ImageIcon(whiteChessSource);
        whiteChess=imageIcon.getImage();
        this.width=width;
        this.height=height;
        rChess=blackChess.getWidth(this)/2;
    }
    protected void paintComponent(Graphics h)
    {
        h.setColor(clearColor);
        h.drawRect(0, 0, width, height);
        h.setColor(tempColor);
        h.drawImage(chessBoard, 0,0,width,height,this);
        
        double x=25,y=25;

        if(highLightX!=-1)
        {
            h.setColor(redColor);
            h.drawRect((int)(highLightX*gap+25)-rChess-2, (int)(highLightY*gap+25)-rChess-2, rChess*2+4, rChess*2+4);
            h.setColor(tempColor);
        }

        for(int i=0;i<15;i++)
        {
            for(int j=0;j<15;j++)
            {
                if(chessBoardState[i][j]==1)
                    h.drawImage(blackChess, (int)x-rChess,(int)y-rChess,rChess*2,rChess*2,this);
                else if(chessBoardState[i][j]==2)
                    h.drawImage(whiteChess, (int)x-rChess,(int)y-rChess,rChess*2,rChess*2,this);
                else if(chessBoardState[i][j]==3)
                    h.fillOval((int)x-rChess, (int)y-rChess, rChess*2, rChess*2);  
                
                y+=gap;
            }
            y=25;
            x+=gap;
        }

        if(finished==false&&winner!=0)
        {
            finished=true;
            repaint();
        }
        else if(finished)
        {
            h.setColor(clearColor);
            h.setFont(textFont);
            if(winner==aiChess)h.drawString(aiWin, 200, 100);
            else h.drawString(humanWin, 200, 100);
        }
    }
    public void setChess(int x,int y,int type)
    {
        if(winner!=0)return;

        if(x<0||x>14)return;
        else if(y<0||y>14)return;
        else if(type<0||type>3)return;
        else chessBoardState[x][y]=type;
                
        
        if(spare(x, y)==false)
        {
            winner=checkWinner(x, y,type);
            highLightX=x;
            highLightY=y;
        }
        repaint();
        
        if(turn==hmChess&&spare(x, y)==false)
        {
            turn=aiChess;
            if(type==hmChess)aiGO();
            turn=hmChess;
        }
        return;
    }

    public int getChessState(int x,int y)
    {
        if(x<0||x>14)return -1;
        else if(y<0||y>14)return -1;
        else return chessBoardState[x][y];
    }
    public int getAiChess()
    {
        return aiChess;
    }
    public int getHmChess()
    {
        return hmChess;
    }
    public void setHmFirst()
    {
        aiChess=2;
        hmChess=1;
        restart();
    }
    public void setAiFirst()
    {
        aiChess=1;
        hmChess=2;
        restart();
    }
    public boolean getTurn()
    {
        if(turn==hmChess)return true;
        else return false;
    }

    public void restart()
    {
        for(int i=0;i<15;i++)
        for(int j=0;j<15;j++)
        chessBoardState[i][j]=0;
        winner=0;

        highLightX=highLightY=-1;

        if(hmChess==1)turn=1;
        else
        {
            chessBoardState[7][7]=aiChess;
            highLightX=highLightY=7;
            turn=2;
        }
        finished=false;
        
        repaint();
    }

    private void aiGO()
    {
        int maxAi=-1,maxHmForAi=-1;
        int maxHm=-1,maxAiForHm=-1;

        int tempAi,tempHm;
        int aiTargetX=0,aiTargetY=0;
        int hmTargetX=0,hmTargetY=0;
        
        for(int i=0;i<15;i++)
        {
            for(int j=0;j<15;j++)
            {
                if(spare(i, j)==false)continue;
                tempAi=evaluate(i,j,aiChess);
                tempHm=evaluate(i,j,hmChess);

               

                if(tempAi>maxAi||(tempAi==maxAi&&tempHm>maxHmForAi))
                {
                    maxAi=tempAi;
                    maxHmForAi=tempHm;
                    aiTargetX=i;
                    aiTargetY=j;
                }

                if(tempHm>maxHm||(tempHm==maxHm&&tempAi>maxAiForHm))
                {
                    maxHm=tempHm;
                    maxAiForHm=tempAi;
                    hmTargetX=i;
                    hmTargetY=j;
                }
            }
        }

        //System.out.println(maxAi+" "+maxHm+" "+maxAiForHm+" "+maxHmForAi);

        if(maxAi>=maxHm)
        {
            setChess(aiTargetX, aiTargetY, aiChess);
            repaint();
        }
        else
        {
            setChess(hmTargetX, hmTargetY, aiChess);
            repaint();
        }
    }

    private int checkWinner(int x,int y,int currentState)
    {
        int i,j,k,len;
        

        i=Math.max(0,x-4);j=Math.min(14, x+4);len=0;
        for(k=i;k<=j;k++)
        {
            if(currentState ==chessBoardState[k][y])len++;
            else  len=0;
            if(len==5)return currentState;
        }

        i=Math.max(0,y-4);j=Math.min(14, y+4);len=0;
        for(k=i;k<=j;k++)
        {
            if(currentState==chessBoardState[x][k])len++;
            else len=0;
            if(len==5)return currentState;
        }

        i=-Math.min(x, y);
        j=Math.min(14-x,14-y);
        for(k=i;k<=j;k++)
        {
            if(currentState==chessBoardState[x+k][y+k])len++;
            else len=0;
            if(len==5)return currentState;
        }

        i=-Math.min(x,14-y);
        j=Math.min(14-x,y);
        for(k=i;k<=j;k++)
        {
            if(currentState==chessBoardState[x+k][y-k])len++;
            else len=0;
            if(len==5)return currentState;
        }

       return 0;
    }



    private static int score;
    private static int[] subscore = new int[4];
    private static int[] substate = new int[4];
    private static int dx[] = {1,0,1,1};
    private static int dy[]={0,1,1,-1};
    private boolean valid(int x,int y)
    {
        if(x<0||x>14)return false;
        if(y<0||y>14)return false;
        return true;
    }
    private boolean spare(int x,int y)
    {
        if(chessBoardState[x][y]==0||chessBoardState[x][y]==3)return true;
        else return false;
    }
    private int getState(int sx,int sy,int journey,int type,int k)
    {
        int len=0;
        int tx=sx,ty=sy;
        int i,orState;
        for(i=0;i<journey;i++)
        {
            if(chessBoardState[tx][ty]==type)len++;
            tx+=dx[k];
            ty+=dy[k];
        }

        switch(len)
        {
            
            case 5:
                return 0;
            case 4:
                int cnt=0;
                    if(journey==5)
                    {
                        tx=sx;ty=sy;
                        for( i=0;i<journey;i++)
                        {
                            if(chessBoardState[tx][ty]!=type)break;
                            tx+=dx[k];
                            ty+=dy[k];
                        }
                        if(spare(tx, ty))cnt++;
                           
                            if(i==0)
                            {
                                tx+=5*dx[k];
                                ty+=5*dy[k];
                                if(valid(tx,ty)&&spare(tx, ty))cnt++;
                            }
                            else if(i==4)
                            {
                                tx-=5*dx[k];
                                ty-=5*dy[k];
                                if(valid(tx,ty)&&spare(tx, ty))cnt++;
                            }
                            
                    }
                    else
                    {
                        tx=sx-dx[k];ty=sy-dy[k];
                        if(valid(tx,ty)&&spare(tx, ty))cnt++;
                    }
                    if(cnt==2)return 1;
                    else if(cnt==1)return 2;
                    else return 11;

            case 3:
                tx=sx;ty=sy;
                boolean dead3=false;
                int tempstate;
                for(i=0;i<journey;i++)
                {
                    if(spare(tx, ty))
                    {
                        orState=chessBoardState[tx][ty];
                       chessBoardState[tx][ty]=type;
                       tempstate=getState(sx,sy,journey,type,k);
                       chessBoardState[tx][ty]=orState;
                        
                       if(tempstate==1)return 3;
                       if(tempstate==2)dead3=true;
                    }
                    tx+=dx[k];
                    ty+=dy[k];
                }
                if(dead3==true)return 4;
                else return 11;

            case 2:
                tx=sx;ty=sy;
                boolean dead2=false;
                for(i=0;i<journey;i++)
                {
                    if(spare(tx, ty))
                    {
                        orState=chessBoardState[tx][ty];
                        chessBoardState[tx][ty]=type;
                        tempstate=getState(sx,sy,journey,type,k);
                        chessBoardState[tx][ty]=orState;
                        
                        if(tempstate==3)return 5;
                        if(tempstate==4)dead2=true;
                    }
                    tx+=dx[k];
                    ty+=dy[k];
                }
                if(dead2==true)return 6;
                else return 11;  
            default:
                return 11;
        }
    }

    private int getScore(int state)
    {
        if(state==0)return level1();
        else if(state==1)return level2();
        else if(state==2)return level3();
        else if(state==3)return level4();
        else if(state==4)return level5();
        else if(state==5)return level6();
        else if (state==6)return level7();
        else if (state==7)return level8();
        else if (state==8)return level9();
        else if(state==9)return level10();
        else if(state==10)return level11();
        else if(state==11)return level12();
        else return 0;
    }
    private int evaluate(int x,int y,int type)
    {
        int tempstate=chessBoardState[x][y];
        chessBoardState[x][y]=type;

        int i,j,k;

        score=0;
        for(i=0;i<4;i++)subscore[i]=0;

        i=Math.max(0, x-4);
        for(;i<=x;i++)
        {
            j=Math.min(i+4, 14);
            k=getState(i, y,j-i+1, type, 0);
            if(subscore[0]<getScore(k))
            {
                substate[0]=k;
                subscore[0]=getScore(k);
            }
        }

        i=Math.max(0, y-4);
        for(;i<=y;i++)
        {
            j=Math.min(i+4, 14);
            k=getState(x, i, j-i+1 , type, 1);
            if(subscore[1]<getScore(k))
            {
                substate[1]=k;
                subscore[1]=getScore(k);
            }
        }
     
        i=Math.min(x, y);
        i=Math.min(i, 4);
        for(;i>=0;i--)
        {
            j=Math.min(14-x,14-y);
            j=Math.min(j,4-i);
            k=getState(x-i, y-i, i+j+1, type, 2);
            if(subscore[2]<getScore(k))
            {
                substate[2]=k;
                subscore[2]=getScore(k);
            }
        }

        i=Math.min(x,14-y);
        i=Math.min(i, 4);
        for(;i>=0;i--)
        {
            j=Math.min(14-x, y);
            j=Math.min(j, 4-i);
            k=getState(x-i, y+i,i+j+1, type, 3);
            if(subscore[3]<getScore(k))
            {
                substate[3]=k;
                subscore[3]=getScore(k);
            }
        }

        for(k=0;k<4;k++)score+=subscore[k];

        chessBoardState[x][y]=tempstate;
        return score;
    }
   

    private static int level1()
    {
        return  9999999;
    }
    private static int level2()
    {
        return 99999;
    }    
    private static int level3()
    {
        return 9999;
    }
    private static int level4()
    {
        return 2000;
    }
    private static int level5()
    {
        return 499;
    }
    private static int level6()
    {
        return  122;
    }
    private static int level7()
    {
        return 30+(int)(Math.random()*10);
    }
    private static int level8()
    {
        return 8;
    }
    private static int level9()
    {
        return 2+(int)(Math.random()*5);    
    }
    private static int level10()
    {
        return 1;
    }
    private static int level11()
    {
        return 1;
    }
    private static int level12()
    {
        return 1;
    }
}