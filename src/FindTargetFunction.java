import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import static java.lang.Math.*;
import static java.lang.Math.abs;

public class FindTargetFunction {

    //給一滑鼠點擊座標，判斷該座標是否位於某個shape中
    public static Shape find_selected_shape(Point startPoint,ArrayList<Shape> all_shape){
        int bound_x1,bound_y1,bound_x2,bound_y2;
        int max=-1; // max is for recording the max area
        Shape target_shape = null;
        if(!all_shape.isEmpty()) {
            for (Shape shape : all_shape) {
                bound_x1 = min(shape.x1,shape.x2);
                bound_x2 = max(shape.x1,shape.x2);
                bound_y1 = min(shape.y1,shape.y2);
                bound_y2 = max(shape.y1,shape.y2);
                boolean in_out = true; //true means in group area
                if( bound_x1 > startPoint.x  || startPoint.x  > bound_x2 ){
                    in_out = false;
                }
                if( bound_y1 > startPoint.y  || startPoint.y  > bound_y2 ){
                    in_out = false;
                }
                if(in_out){
                    int area = (bound_x2 - bound_x1) * (bound_y2-bound_y1);
                    if(area>max){
                        max = area;
                        target_shape = shape;
                    }
                }
            }
        }
        return target_shape;
    }

    //給一滑鼠點擊座標，判斷該座標是否位於 給定的shape 的四個port中的其中一個。
    public static Port find_selected_port(Point startPoint, Shape selected_shape) {
        //如果有找到object，則直接將 線 連接到mouseReleased()時，最接近的port上
        if(selected_shape != null){
            int min = 1000;
            int dis = 0;
            Port tmp = new Port(null,0,0);
            Port selected_port = null;
                                /*       1
                                        ____
                                       |    |
                                     2 |    | 3
                                       |____|
                                         4
                                 */
            // point 1
            tmp.x = selected_shape.x1 + ((selected_shape.x2 - selected_shape.x1) / 2);
            tmp.y = selected_shape.y1;
            dis = (int) sqrt(pow(abs(startPoint.x - tmp.x), 2) + pow(abs(startPoint.y - tmp.y), 2));
            if (dis < min) {
                min = dis;
                selected_port = ((Object)selected_shape).ports[0];
            }
            // point 2
            tmp.x = selected_shape.x1;
            tmp.y = selected_shape.y1 + ((selected_shape.y2 - selected_shape.y1) / 2);
            dis = (int) sqrt(pow(abs(startPoint.x - tmp.x), 2) + pow(abs(startPoint.y - tmp.y), 2));
            if (dis < min) {
                min = dis;
                selected_port = ((Object)selected_shape).ports[1];
            }
            // point 3
            tmp.x = selected_shape.x2;
            tmp.y = selected_shape.y1 + ((selected_shape.y2 - selected_shape.y1) / 2);
            dis = (int) sqrt(pow(abs(startPoint.x - tmp.x), 2) + pow(abs(startPoint.y - tmp.y), 2));
            if (dis < min) {
                min = dis;
                selected_port = ((Object)selected_shape).ports[2];
            }
            //point 4
            tmp.x = selected_shape.x1 + ((selected_shape.x2 - selected_shape.x1) / 2);
            tmp.y = selected_shape.y2;
            dis = (int) sqrt(pow(abs(startPoint.x - tmp.x), 2) + pow(abs(startPoint.y - tmp.y), 2));
            if (dis < min) {
                min = dis;
                selected_port = ((Object)selected_shape).ports[3];
            }

            return selected_port;
        }
        return null;
    }

    //給一座標，判斷是否位於一個矩形區域內，矩形左上角左標(x1,y1)；矩形右上角左標(x2,y2)
    public static boolean find_in_or_out(Point point,int x1, int y1, int x2, int y2){
        //此function為判斷一個point是否在一個範圍內
        int bound_x1,bound_y1,bound_x2,bound_y2;
        bound_x1 = min(x1,x2);
        bound_x2 = max(x1,x2);
        bound_y1 = min(y1,y2);
        bound_y2 = max(y1,y2);
        return (bound_x1 <= point.x && point.x <= bound_x2) && (bound_y1 <= point.y && point.y <= bound_y2);
    }

}
