import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


class Select extends ToolListener{
        private Point startPoint = null;
        private Point prePoint = null;
        private Shape selected_shape = null;

        public void mousePressed(MouseEvent e) {
                /*
                Select有三種狀況:
                1.要進行多重選取，其優先權最大
                2.點選到Group，其優先權次中
                3.點選到Object，其優先權最低
                如果mousePressed的位置，同時存在多重選取、Group、Object，則按照優先權優先選取。
                 */
                startPoint = e.getPoint();
                prePoint = startPoint;
                ArrayList<Shape> all_shape = canvas.getShapeList();
                Shape multi_selecting_group = canvas.getMulti_selecting_group();

                selected_shape = FindTargetFunction.find_selected_shape(startPoint,all_shape);

                Shape pre_shape = canvas.getCurrentShape();
                //點擊到的位置位在object外面時，selected object 會被設為null
                //但如果點擊位置在四個port上，則不將selected object 設為null
                if(pre_shape != null){
                        if(pre_shape instanceof Object) {
                                //判斷是不是點在四個port的範圍內，如果是，則highlight該port
                                if (setPort(startPoint,pre_shape))
                                        selected_shape = pre_shape;
                        }
                }

                //多重選取物件
                if(multi_selecting_group == null) {
                        //selected_object 和 selected_group 皆為null代表要畫多重選取
                        if (selected_shape == null) {
                                Shape multi_select = new Multi_select();
                                multi_select.x1 = multi_select.x2 = e.getX();
                                multi_select.y1 = multi_select.y2 = e.getY();
                                //multi_select.id = -1; //多重選取是虛的group
                                canvas.setMulti_selecting_group(multi_select);
                                canvas.setCurrentShape(null);
                        }
                        else{
                                canvas.setMulti_selecting_group(null);
                                if(selected_shape != null && selected_shape != pre_shape)
                                        canvas.setCurrentShape(selected_shape);
                        }
                }
                else{
                        //判斷點擊的位置是在multi selecting的框外還框內
                        boolean in_out_2 = FindTargetFunction.find_in_or_out(startPoint,multi_selecting_group.x1,multi_selecting_group.y1,
                                multi_selecting_group.x2,multi_selecting_group.y2);
                        if(in_out_2)
                                selected_shape = multi_selecting_group;
                        else {
                                canvas.setMulti_selecting_group(null);
                                canvas.setCurrentShape(null);
                        }
                }
                canvas.repaint();
        }
        public void mouseDragged(MouseEvent e) {
                int Move_x = e.getX() - prePoint.x;
                int Move_y = e.getY() - prePoint.y;
                prePoint = e.getPoint();

                Shape multi_selecting_group = canvas.getMulti_selecting_group();
                //正在拉多重選取的框框
                if(multi_selecting_group != null && multi_selecting_group != selected_shape){
                        multi_selecting_group.x2 = e.getX();
                        multi_selecting_group.y2 = e.getY();
                        canvas.setCurrentShape(multi_selecting_group);
                }
                else if(selected_shape != null)
                        selected_shape.move(Move_x,Move_y);

                canvas.repaint();
        }
        public void mouseReleased(MouseEvent e) {
                //only multi-selecting will use mouseReleased()
                Shape multi_selecting_group = canvas.getMulti_selecting_group();
                if(multi_selecting_group != null && multi_selecting_group != selected_shape) {
                        ArrayList<Shape> all_shape = canvas.getShapeList();
                        ArrayList<Shape> group_of_shape_list = ((Group) multi_selecting_group).getGroup_of_ShapeList();
                        for (Shape shape : all_shape) {
                                boolean in_out = FindTargetFunction.find_in_or_out(new Point((shape.x1+shape.x2)/2,(shape.y1+shape.y2)/2),
                                        multi_selecting_group.x1,multi_selecting_group.y1,multi_selecting_group.x2,multi_selecting_group.y2);
                                if (in_out)
                                        group_of_shape_list.add(shape);
                        }
                        if (group_of_shape_list.size() == 0) { // no object in group area
                                canvas.setMulti_selecting_group(null);
                                canvas.setCurrentShape(null);
                        }
                        else
                                canvas.setCurrentShape(multi_selecting_group);
                        canvas.repaint();
                }
        }

        @Override
        public void init_Object(int x,int y){}
        public boolean setPort(Point startPoint,Shape pre_shape){
                boolean in_out;
                boolean tmp;
                tmp = FindTargetFunction.find_in_or_out(startPoint, ((Object) pre_shape).ports[0].x-5,((Object) pre_shape).ports[0].y-5,
                        ((Object) pre_shape).ports[0].x+5,((Object) pre_shape).ports[0].y+5);
                in_out = tmp;
                if(tmp)
                        ((Object) pre_shape).ports[0].highlight = true;

                tmp = (FindTargetFunction.find_in_or_out(startPoint, ((Object) pre_shape).ports[1].x-5,((Object) pre_shape).ports[1].y-5,
                        ((Object) pre_shape).ports[1].x+5,((Object) pre_shape).ports[1].y+5));
                in_out = in_out || tmp;
                if(tmp)
                        ((Object) pre_shape).ports[1].highlight = true;

                tmp = (FindTargetFunction.find_in_or_out(startPoint,((Object) pre_shape).ports[2].x-5,((Object) pre_shape).ports[2].y-5,
                        ((Object) pre_shape).ports[2].x+5,((Object) pre_shape).ports[2].y+5));
                in_out = in_out || tmp;
                if(tmp)
                        ((Object) pre_shape).ports[2].highlight = true;

                tmp = (FindTargetFunction.find_in_or_out(startPoint,((Object) pre_shape).ports[3].x-5,((Object) pre_shape).ports[3].y-5,
                        ((Object) pre_shape).ports[3].x+5,((Object) pre_shape).ports[3].y+5));
                in_out = in_out || tmp;
                if(tmp)
                        ((Object) pre_shape).ports[3].highlight = true;

                return in_out;
        }
}

class Line_Listener extends ToolListener{

        private Shape start_shape = null;
        private Port start_port = null;
        private Shape target_line = null;
        private Port p2 = new Port(null,0,0);
        private final int type;


        public Line_Listener(int type){
                this.type = type;
        }
//
        public void mousePressed(MouseEvent e) {
                Point startPoint = e.getPoint();
                ArrayList<Shape> all_shape = canvas.getShapeList();

                start_shape = FindTargetFunction.find_selected_shape(startPoint,all_shape);
                start_port = FindTargetFunction.find_selected_port(startPoint,start_shape);
                if(start_shape != null && start_port != null){
                        switch (this.type){
                                case 1-> target_line = new AssociationLine();
                                case 2-> target_line = new DependencyLine();
                                case 3-> target_line = new GeneralizationLine();
                                case 4-> target_line = new CompositionLine();
                        }

                        ((Line) target_line).ports[0] = start_port;
                        ArrayList<Line> port_line_list = start_port.getLine_list();
                        all_shape.add(target_line);
                        port_line_list.add(((Line) target_line));
                }
        }
        public void mouseDragged(MouseEvent e) {
                if(start_shape != null) {
                        p2.x = e.getX();
                        p2.y = e.getY();
                        ((Line) target_line).ports[1] = p2;
                        canvas.repaint();
                }
        }
        public void mouseReleased(MouseEvent e) {
                Point endPoint = e.getPoint();
                ArrayList<Shape> all_shape = canvas.getShapeList();

                Shape end_shape = FindTargetFunction.find_selected_shape(endPoint,all_shape);
                if( start_shape != null && (end_shape == null  ||  end_shape ==start_shape) ){
                        Shape line = all_shape.get(all_shape.size()-1);
                        all_shape.remove(line);
                        ArrayList<Line> port_line_list = start_port.getLine_list();
                        Shape line2 = port_line_list.get(port_line_list.size()-1);
                        port_line_list.remove(line2);
                }
                else if(start_shape != null && end_shape instanceof Object) {
                        Port end_port = FindTargetFunction.find_selected_port(endPoint,end_shape);
                        ((Line) target_line).ports[1] = end_port;
                        ArrayList<Line> port_line_list = end_port.getLine_list();
                        port_line_list.add(((Line) target_line));
                }
                canvas.repaint();
        }

        @Override
        public void init_Object(int x1, int y) {}
}

class DrawClass extends ToolListener{
        public void mousePressed(java.awt.event.MouseEvent e) {
                init_Object(e.getX(), e.getY());
                canvas.repaint();
        }
        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        public void init_Object(int x, int y) {
                Object object = new ClassObject();
                ArrayList<Shape> all_shape = canvas.getShapeList();
                object.x1 = x-45;
                object.y1 = y-60;
                object.x2 = x+45;
                object.y2 = y+60;
                object.name = "Object name";
                object.ports[0] = new Port(object,x,y-60);
                object.ports[1] = new Port(object,x-45,y);
                object.ports[2] = new Port(object,x+45,y);
                object.ports[3] = new Port(object,x,y+60);

                all_shape.add(object);
        }
}

class DrawUsecase extends ToolListener{
        public void mousePressed(java.awt.event.MouseEvent e) {
                init_Object(e.getX(), e.getY());
                canvas.repaint();
        }
        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}


        public void init_Object(int x, int y) {
                Object object = new UsecaseObject();
                ArrayList<Shape> all_shape = canvas.getShapeList();
                object.x1 = x - 60;
                object.y1 = y - 45;
                object.x2 = x + 60;
                object.y2 = y + 45;
                object.name = "Object name";
                object.ports[0] = new Port(object,x,y-45);
                object.ports[1] = new Port(object,x-60,y);
                object.ports[2] = new Port(object,x+60,y);
                object.ports[3] = new Port(object,x,y+45);
                all_shape.add(object);
        }
}

public abstract class ToolListener extends MouseAdapter {
        protected final MyCanvas canvas = MyCanvas.getInstance();
        public abstract void init_Object(int x,int y);
        public abstract void mousePressed(java.awt.event.MouseEvent e);
        public abstract void mouseDragged(MouseEvent e);
        public abstract void mouseReleased(MouseEvent e);
}
