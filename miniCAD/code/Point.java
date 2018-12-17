public class Point
{
    public double x,y;
    static double dot (Point x,Point y)
    {
        return x.x*y.x+x.y*y.y;
    }
    static double cross (Point x,Point y)
    {
        return x.x*y.y-x.y*y.x;
    }
    public double length()
    {
        return Math.sqrt(x*x+y*y);
    }
    static double angle(Point x,Point y)
    {
        return 180.0*Math.acos(dot(x, y)/(x.length()*y.length()))/Math.PI;
    }
    static double dirAngle(Point x,Point y)
    {
        if(cross(x,y)>0)return angle(x,y);
        else return -1.0*angle(x,y);
    }
    static Point differ(Point x,Point y)
    {
        return  new Point(y.x-x.x,y.y-x.y);
    }
    Point()
    {
        x=y=0;
    }
    Point (double x,double y)
    {
        this.x=x;
        this.y=y;
    }
}