import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MyMenuBar extends JMenuBar {
    private final MyCanvas canvas;

    public MyMenuBar(){
        canvas = MyCanvas.getInstance();
        JMenu main_menu;
        JMenuItem item;

        /* --- File menu --- */
        main_menu = new JMenu("File");
        add(main_menu);

        /* --- Edit menu --- */
        main_menu = new JMenu("Edit");
        add(main_menu);

        item = new JMenuItem("Change object name");
        main_menu.add(item);
        item.addActionListener(new ChangeNameListener(canvas));

        item = new JMenuItem("Group");
        main_menu.add(item);
        item.addActionListener(new GroupActionListener(canvas));

        item = new JMenuItem("Ungroup");
        main_menu.add(item);
        item.addActionListener(new UnGroupActionListener(canvas));
    }


}
