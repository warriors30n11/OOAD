import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


class ChangeNameListener implements ActionListener{
    private final MyCanvas canvas;

    ChangeNameListener(MyCanvas canvas){
        this.canvas = canvas;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ChangeName();
    }

    public void ChangeName(){
        Shape current_shape = canvas.getCurrentShape();
        if(current_shape != null) {
            JFrame inputTextFrame = new JFrame("Change Object Name");
            inputTextFrame.setSize(400, 100);
            inputTextFrame.getContentPane().setLayout(new GridLayout(0, 1));

            JPanel panel = null;
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

            JTextField Text = new JTextField(((Object)current_shape).name);
            panel.add(Text);
            inputTextFrame.getContentPane().add(panel);

            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

            JButton confirm = new JButton("OK");
            panel.add(confirm);

            JButton cancel = new JButton("Cancel");
            panel.add(cancel);

            inputTextFrame.getContentPane().add(panel);

            inputTextFrame.setLocationRelativeTo(null);
            inputTextFrame.setVisible(true);

            confirm.addActionListener(e -> {
                ((Object) current_shape).name = Text.getText();
                canvas.repaint();
                inputTextFrame.dispose();
            });

            cancel.addActionListener(e -> inputTextFrame.dispose());
        }
    }
}

class GroupActionListener implements ActionListener{

    private final MyCanvas canvas;

    GroupActionListener(MyCanvas canvas){
        this.canvas = canvas;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<Shape> all_shape = canvas.getShapeList();
        Shape multi_selecting_group = canvas.getMulti_selecting_group();
        if(multi_selecting_group != null) {
            Shape group = new Group();
            group.x1 = multi_selecting_group.x1;
            group.x2 = multi_selecting_group.x2;
            group.y1 = multi_selecting_group.y1;
            group.y2 = multi_selecting_group.y2;
            ArrayList<Shape> group_of_shape = (ArrayList<Shape>) ((Group) multi_selecting_group).getGroup_of_ShapeList().clone();
            ((Group)group).setGroup_of_ShapeList(group_of_shape);
            all_shape.add(group);
            canvas.setMulti_selecting_group(null);
            canvas.setCurrentShape(null);
            canvas.repaint();
        }

    }
}

class UnGroupActionListener implements ActionListener{

    private final MyCanvas canvas;

    UnGroupActionListener(MyCanvas canvas){
        this.canvas = canvas;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Shape selected_shape = canvas.getCurrentShape();
        ArrayList<Shape> all_shape = canvas.getShapeList();
        Shape multi = canvas.getMulti_selecting_group();
        if(multi == null && selected_shape!=null){
            int idx = all_shape.indexOf(selected_shape);
            all_shape.remove(idx);
            canvas.setCurrentShape(null);
            canvas.repaint();
        }
    }
}


public abstract class MenuBarListener{

}
