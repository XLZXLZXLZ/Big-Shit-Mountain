package view;

import manager.UserManager;
import user.User;
import Enums.UserType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame
{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton regisiteButton;

    public LoginFrame() {
        // 设置窗口标题
        setTitle("登录");

        // 创建组件和布局
        JLabel usernameLabel = new JLabel("用户名:");
        JLabel passwordLabel = new JLabel("密码:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("登录");
        regisiteButton = new JButton("去注册");


        //为按钮注册事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在按钮点击时执行的操作
                TryLogin();
            }
        });

        regisiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在按钮点击时执行的操作
                new RegisiteFrame();
                dispose();
            }
        });

        // 设置布局管理器
        setLayout(new GridLayout(3,2));

        // 添加组件到窗口
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(regisiteButton); // 占位
        add(loginButton);

        // 设置窗口大小、关闭操作和可见性
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void addLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    //登录函数
    private void TryLogin()
    {
        //尝试登陆
        User user =  UserManager.getInstance().Authenticate(getUsername(),getPassword());
        if(user == null) //尝试失败
        {
            JOptionPane.showMessageDialog(null, "用户名或密码错误!");
        }
        else if(user.getType() == UserType.Admin) //尝试成功，管理员
        {
            new AdminFrame(user);
            dispose();
        }
        else if(user.getType() == UserType.NormalUser) //尝试成功，普通用户
        {
            new NormalUserFrame(user);
            dispose();
        }
        else if(user.getType() == UserType.Librarian) //尝试成功，普通用户
        {
            new LibrarianFrame(user);
            dispose();
        }
    }
}