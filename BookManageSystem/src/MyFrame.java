import javax.swing.*;

public class MyFrame extends JFrame {
    public MyFrame() {
        // 设置窗口标题
        setTitle("界面");

        // 设置窗口大小
        setSize(400, 300);

        // 设置窗口位置居中
        setLocationRelativeTo(null);

        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}