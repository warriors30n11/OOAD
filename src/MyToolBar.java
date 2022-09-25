import javax.swing.*;
import java.awt.*;

public class MyToolBar extends JToolBar {
    private final int Tool_num = 7;
    private final MyCanvas canvas;
    private final Color bg = new Color(253, 21, 21);

    private final ImageIcon select_icon,association_icon,dependency_icon,generalization_icon,composition_icon,class_icon,usecase_icon;
    private final ToolBtn select_btn,association_btn,dependency_btn,generalization_btn,composition_btn,class_btn,usecase_btn;
    private JButton holdBtn = null;

    public MyToolBar() {
        canvas = MyCanvas.getInstance();
        setLayout(new GridLayout(Tool_num, 1, 2, 2));

        this.select_icon = new ImageIcon(new ImageIcon("img/select.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        this.select_btn = new ToolBtn("select",this.select_icon, new Select());
        add(this.select_btn);

        this.association_icon = new ImageIcon(new ImageIcon("img/association.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        this.association_btn = new ToolBtn("Association",this.association_icon, new Line_Listener((int)1));
        add(this.association_btn);

        this.dependency_icon = new ImageIcon(new ImageIcon("img/dependency.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        this.dependency_btn = new ToolBtn("Dependency",this.dependency_icon, new Line_Listener((int)2));
        add(this.dependency_btn);

        this.generalization_icon = new ImageIcon(new ImageIcon("img/generalization.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        this.generalization_btn = new ToolBtn("Generation",this.generalization_icon, new Line_Listener((int)3));
        add(this.generalization_btn);

        this.composition_icon = new ImageIcon(new ImageIcon("img/composition.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        this.composition_btn = new ToolBtn("Composition",this.composition_icon, new Line_Listener((int)4));
        add(this.composition_btn);

        this.class_icon = new ImageIcon(new ImageIcon("img/class.png").getImage().getScaledInstance(80, 100, Image.SCALE_DEFAULT));
        this.class_btn = new ToolBtn("class",this.class_icon,new DrawClass());
        add(this.class_btn);

        this.usecase_icon = new ImageIcon(new ImageIcon("img/use case.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        this.usecase_btn = new ToolBtn("usecase",this.usecase_icon, new DrawUsecase());
        add(this.usecase_btn);

    }

    private class ToolBtn extends JButton {
        public ToolBtn(String ToolName, ImageIcon icon, ToolListener listener) {
            setToolTipText(ToolName);
            setIcon(icon);
            setFocusable(false);
            setBackground(new Color(0, 0, 0));
            setBorderPainted(false);
            setRolloverEnabled(true);
            addActionListener(e -> {
                if(holdBtn != null) {
                    holdBtn.setBackground(new Color(0, 0, 0));
                }
                holdBtn = (JButton) e.getSource();
                holdBtn.setBackground(bg);
                canvas.setListener(listener);
                //initial selecting
                canvas.setCurrentShape(null);
                canvas.repaint();

            });

        }



    }

}
