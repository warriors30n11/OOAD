import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.min;

abstract class Shape{
    public int x1,y1,x2,y2;
    public boolean isdrawed;

    public abstract void draw(Graphics g,boolean selected);
    public abstract void move(int Move_x,int Move_y);
    
    //set_isdrawed()用意在於，有些shape在current_shape那邊就會先被畫一次了，所以用一個boolean檢測是否已經畫過這個object
    public void set_isdrawed(boolean tmp){
        this.isdrawed = tmp;
    }
}

//         1
//        ____
//       |    |
//       |    |
//     2 |    | 3
//       |    |
//       |____|
//         4
abstract class Object extends Shape{
    public String name;
    Port[] ports = new Port[4];
    @Override
    public abstract void draw(Graphics g,boolean selected);
    @Override
    public void move(int Move_x,int Move_y){
        x1 += Move_x;
        x2 += Move_x;
        y1 += Move_y;
        y2 += Move_y;
        for(Port port:ports){
            port.x += Move_x;
            port.y += Move_y;
        }
    }
}

class ClassObject extends Object{
    @Override
    public void draw(Graphics g,boolean selected){
        int w = x2-x1;
        int h = y2-y1;
        int dis = h / 3 ;
        g.drawRect(x1, y1, w, h);
        g.drawLine( x1, y1 + dis,x1 +w, y1 + dis);
        g.drawLine( x1, y1 + dis*2,x1 + w, y1 + dis*2);

        int stringWidth = g.getFontMetrics(new Font(Font.DIALOG, Font.BOLD, 14)).stringWidth(name);
        double empty = (w - stringWidth ) / 2;
        g.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        g.drawString(name, x1 + (int)empty , y1 + 25);
        if(selected){
            for(Port port : ports)
                port.draw(g,selected);
            set_isdrawed(true);
        }
    }
}

class UsecaseObject extends Object{
    @Override
    public void draw(Graphics g,boolean selected){
        int w = x2-x1;
        int h = y2-y1;
        Font font =new Font(Font.DIALOG, Font.BOLD, 14);
        g.drawOval(x1, y1, w, h);

        int stringWidth = g.getFontMetrics(font).stringWidth(name);
        double empty = (w - stringWidth ) / 2;
        g.setFont(font);
        g.drawString(name, x1 + (int)empty , y1 + 50);
        if(selected){
            for(Port port : ports)
                port.draw(g,selected);
            set_isdrawed(true);
        }
    }
}


abstract class Line extends Shape {
    Port[] ports = new Port[2];//0 為起點，1為終點

    abstract public void draw(Graphics g,boolean selected);
    //line needn't to move
    public void move(int Move_x,int Move_y){}
    public double[] getRad(int x1, int y1, int x2, int y2){
        double[] vector = new double[4];
        int x = x1-x2;
        int y = y1-y2;
        double vec_x,vec_y,theta = 30; //theta 為箭頭兩翼角度
        theta = theta * Math.PI / 180;
        //normalization of vector
        vec_x = (x / Math.sqrt( Math.pow(x,2) + Math.pow(y,2) ));
        vec_y = (y / Math.sqrt( Math.pow(x,2) + Math.pow(y,2) ));
        //cw == clockwise
        vector[0] = (vec_x * Math.cos(theta)) - (vec_y * Math.sin(theta)); //x_cw
        vector[1]= (vec_x * Math.sin(theta)) + (vec_y * Math.cos(theta)); //y_cw
        vector[2]= (vec_x * Math.cos(theta)) + (vec_y * Math.sin(theta));//x_counter_cw
        vector[3]= -(vec_x * Math.sin(theta)) + (vec_y * Math.cos(theta));//y_counter_cw
        return vector;
    }

}

class AssociationLine extends Line{
    @Override
    public void draw(Graphics g,boolean selected) {
        double[] vector = getRad(ports[0].x,ports[0].y,ports[1].x,ports[1].y);
        g.drawLine(ports[0].x,ports[0].y,ports[1].x,ports[1].y);
        g.drawLine(ports[1].x,ports[1].y,ports[1].x+(int)(vector[0]*30),ports[1].y+(int)(vector[1]*30));
        g.drawLine(ports[1].x,ports[1].y,ports[1].x+(int)(vector[2]*30),ports[1].y+(int)(vector[3]*30));
        set_isdrawed(true);
    }
}
class DependencyLine extends Line{
    @Override
    public void draw(Graphics g,boolean selected) {
        BasicStroke dashed =new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0);
        double[] vector = getRad(ports[0].x,ports[0].y,ports[1].x,ports[1].y);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setStroke(dashed);
        g2.drawLine(ports[0].x,ports[0].y,ports[1].x,ports[1].y);
        g.drawLine(ports[1].x,ports[1].y,ports[1].x+(int)(vector[0]*30),ports[1].y+(int)(vector[1]*30));
        g.drawLine(ports[1].x,ports[1].y,ports[1].x+(int)(vector[2]*30),ports[1].y+(int)(vector[3]*30));
        set_isdrawed(true);
    }
}
class GeneralizationLine extends Line{
    @Override
    public void draw(Graphics g,boolean selected) {
        double[] vector = getRad(ports[0].x,ports[0].y,ports[1].x,ports[1].y);
        int[] xpoints = { ports[1].x, ports[1].x+(int)(vector[0]*30) , ports[1].x+(int)(vector[2]*30) };
        int[] ypoints = { ports[1].y, ports[1].y+(int)(vector[1]*30) , ports[1].y+(int)(vector[3]*30) };
        g.drawLine(ports[0].x,ports[0].y,ports[1].x,ports[1].y);
        g.fillPolygon(xpoints, ypoints, 3);
        set_isdrawed(true);
    }
}
class CompositionLine extends Line{
    @Override
    public void draw(Graphics g,boolean selected) {
        double[] vector = getRad(ports[0].x,ports[0].y,ports[1].x,ports[1].y);
        double vec_x = ((ports[0].x-ports[1].x) / Math.sqrt( Math.pow((ports[0].x-ports[1].x),2) + Math.pow((ports[0].y-ports[1].y),2) ));
        double vec_y = ((ports[0].y-ports[1].y) / Math.sqrt( Math.pow((ports[0].x-ports[1].x),2) + Math.pow((ports[0].y-ports[1].y),2) ));
        int[] xpoints = { ports[1].x, ports[1].x+(int)(vector[0]*30) , ports[1].x + (int)(vec_x *45), ports[1].x+(int)(vector[2]*30) };
        int[] ypoints = { ports[1].y, ports[1].y+(int)(vector[1]*30) , ports[1].y + (int)(vec_y *45), ports[1].y+(int)(vector[3]*30) };
        g.drawLine(ports[0].x,ports[0].y,ports[1].x,ports[1].y);
        g.fillPolygon(xpoints, ypoints, 4);
        set_isdrawed(true);
    }
}


class Port{
    public int x,y;
    public Object mother;
    private final ArrayList<Line> line_list = new ArrayList<Line>();
    boolean highlight=false;

    public Port(Object object,int x,int y){
        this.mother = object;
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics g,boolean selected){
        g.fillRect(x-5,y-5, 10,10);
        if(highlight){
            //draw all line in red again
            g.setColor(new Color(255, 0, 0));
            for(Line line : line_list)
                line.draw(g,selected);
            g.setColor(new Color(0, 0, 0));
            highlight = false;
        }
    }
    public ArrayList<Line> getLine_list(){
        return line_list;
    }
}


class Group extends Shape{
    private ArrayList<Shape> group_of_shape_list = new ArrayList<Shape>();
    public ArrayList<Shape> getGroup_of_ShapeList(){
        return  group_of_shape_list;
    }
    public void setGroup_of_ShapeList(ArrayList<Shape> group_list){
        this.group_of_shape_list = group_list;
    }

    @Override
    public void draw(Graphics g,boolean selected){
        g.setColor(new Color(35, 92, 232, 38));
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int w = abs(x1 - x2);
        int h = abs(y1 - y2);
        g.fillRect(x, y, w, h);
        g.setColor(Color.BLACK);
        set_isdrawed(true);
        for(Shape shape:group_of_shape_list)
            shape.draw(g, selected);
    }
    @Override
    public void move(int Move_x,int Move_y){
        x1 += Move_x;
        x2 += Move_x;
        y1 += Move_y;
        y2 += Move_y;
        for(Shape shape:group_of_shape_list)
            if(shape instanceof Object)
                shape.move(Move_x,Move_y);
            else if(shape instanceof Group){
                shape.x1 += Move_x;
                shape.x2 += Move_x;
                shape.y1 += Move_y;
                shape.y2 += Move_y;
            }
    }
}
//多重選取是Group的特異化，需不同於Group
class Multi_select extends Group{
    public void draw(Graphics g,boolean selected){
        BasicStroke dashed =new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(new Color(43, 117, 255));
        g2.setStroke(dashed);
        g2.draw(new RoundRectangle2D.Double(min(x1,x2), min(y1,y2),abs(x2-x1), abs(y2-y1), 10, 10));
        ArrayList<Shape> group_of_shape_list = getGroup_of_ShapeList();
        for(Shape shape:group_of_shape_list)
            shape.draw(g,selected);
    }
}