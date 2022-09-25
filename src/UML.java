import javax.swing.*;
import java.awt.*;
public class UML extends JFrame{

    public UML() {
        MyCanvas canvas = MyCanvas.getInstance();
        MyToolBar toolbar = new MyToolBar();
        MyMenuBar menubar = new MyMenuBar();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menubar, BorderLayout.NORTH);
        getContentPane().add(toolbar, BorderLayout.WEST);
        getContentPane().add(canvas, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        UML mainWindow = new UML();
        mainWindow.setTitle("UML editor");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(1200, 900);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }
}
