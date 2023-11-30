package view;

import Enums.UserType;
import manager.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisiteFrame extends JFrame
{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField comfirmPasswordField;
    private JButton regisiteButton;
    private JButton returnButton;

    public RegisiteFrame() {
        // 设置窗口标题
        setTitle("注册");

        // 创建组件和布局
        JLabel usernameLabel = new JLabel("用户名:");
        JLabel passwordLabel = new JLabel("密码:");
        JLabel comfirmPasswordLabel = new JLabel("确认密码");

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        comfirmPasswordField = new JPasswordField(20);
        regisiteButton = new JButton("注册");
        returnButton = new JButton("返回");

        //为按钮注册事件
        regisiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String psw = new String(passwordField.getPassword());
                String _psw = new String(comfirmPasswordField.getPassword());
                if(!psw.equals(_psw))
                {
                    JOptionPane.showMessageDialog(null, "两次输入的密码不一致，请检查!");
                    return;
                }
                // 在按钮点击时执行的操作
                boolean result = UserManager.getInstance().AddUser(usernameField.getText(),psw, UserType.NormalUser);
                if(!result)
                {
                    JOptionPane.showMessageDialog(null, "用户名已被占用，来晚啦");
                    return;
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "注册成功");
                    return;
                }
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame();
                dispose();
            }
        });

        // 设置布局管理器
        setLayout(new GridLayout(4,2));

        // 添加组件到窗口
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(comfirmPasswordLabel);
        add(comfirmPasswordField);
        add(regisiteButton);
        add(returnButton);

        // 设置窗口大小、关闭操作和可见性
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
