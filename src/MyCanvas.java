import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.Font;

public class MyCanvas extends JPanel {
    private static MyCanvas instance = null;
    private final ArrayList<Shape> all_shape = new ArrayList<Shape>();
    private Shape current_shape = null;
    private Shape multi_selecting_shape = null;
    private ToolListener current_listener = null;

    //constructor
    private MyCanvas(){
    }
    public static MyCanvas getInstance(){
        if (instance == null){
            instance = new MyCanvas();
        }
        return instance;
    }

    public ArrayList<Shape> getShapeList(){
        return all_shape;
    }
    public Shape getCurrentShape(){
        return current_shape;
    }
    public Shape getMulti_selecting_group(){
        return multi_selecting_shape;
    }

    public void setCurrentShape(Shape shape){
        this.current_shape = shape;

    }
    public void setMulti_selecting_group(Shape shape){
        multi_selecting_shape = shape;
    }
    public void setListener(ToolListener listener){
        if(current_listener == null){
            current_listener = listener;
        }
        else{
            removeMouseListener(current_listener);
            removeMouseMotionListener(current_listener);
            current_listener = listener;
        }
        addMouseListener(current_listener);
        addMouseMotionListener(current_listener);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        if(current_shape!=null)
            current_shape.draw(g,true);
        for(Shape shape:all_shape) {
            if(!shape.isdrawed)
                shape.draw(g, false);
            shape.set_isdrawed(false);
        }
    }
}
