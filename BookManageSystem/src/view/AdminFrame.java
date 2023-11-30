package view;

import manager.BookManager;
import manager.UserManager;
import user.Admin;
import user.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class AdminFrame extends JFrame
{
    private class AddLibrarianPanel extends JFrame
    {
        private JLabel name, password;
        private JTextField nameField, passwordField;
        private JButton comfirmButton,returnButton;

        private AddLibrarianPanel()
        {
            name = new JLabel("用户名");
            password = new JLabel("密码");
            nameField = new JTextField(20);
            passwordField = new JTextField(20);
            comfirmButton = new JButton("添加");
            returnButton = new JButton("退出");

            comfirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = nameField.getText();
                    String password = passwordField.getText();

                    //检查是否输入了完整信息
                    if (name.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "请输入完整信息");
                        return;
                    }

                    if(!user.AddLibarian(name,password))
                        JOptionPane.showMessageDialog(null, "用户名已存在，添加失败");

                    updateUserList(UserManager.getInstance().GetAllUsers());
                    dispose();
                }
            });

            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            setLayout(new GridLayout(3,2));

            add(name);
            add(nameField);
            add(password);
            add(passwordField);
            add(comfirmButton);
            add(returnButton);

            setSize(300,150);
            setVisible(true);
        }
    }
    private Admin user;
    private JButton addLibarianButton;
    private JButton clearDataButton;
    private JButton exitButton;
    private JLabel chooseInfo;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private ArrayList<User> userList; // 用户列表
    private int currentChooseRow; // 当前选定的行

    public AdminFrame(User user) {
        // 设置窗口标题
        setTitle("用户列表");

        this.user = (Admin)user;

        userList = new ArrayList<>();

        // 设置布局管理器
        setLayout(new BorderLayout());

        String[] columnNames = {"用户ID", "用户名","密码", "用户类型"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        userTable.setEnabled(true);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setRowSelectionAllowed(true);
        userTable.setDefaultEditor(Object.class, null);
        userTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (userTable.getSelectedRow() < 0)
                    return;
                currentChooseRow = userTable.getSelectedRow();
                User user = userList.get(currentChooseRow);
                String userInfo = "<html>用户ID: " + user.getID()
                        + "<br>用户名: " + user.getName()
                        + "<br>用户类型: " + user.getType().toString()
                        + "<br>账户密码:" + user.getPassword()
                        + "</html>";
                chooseInfo.setText(userInfo);
            }
        });

        // 添加组件到窗口
        add(new JScrollPane(userTable), BorderLayout.NORTH);

        chooseInfo= new JLabel("");
        JPanel infoPanel = new JPanel();
        infoPanel.add(chooseInfo);

        add(infoPanel,BorderLayout.CENTER);

        addLibarianButton = new JButton("注册新图书管理员");
        addLibarianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddLibrarianPanel();
            }
        });

        clearDataButton = new JButton("清除所有数据并退出");
        clearDataButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserManager.getInstance().ClearUserData();
                BookManager.getInstance().ClearBookData();
                dispose();
            }
        });

        exitButton = new JButton("退出账号");
        exitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new LoginFrame();
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();

        System.out.println("a");

        buttonPanel.add(addLibarianButton);
        buttonPanel.add(clearDataButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        updateUserList(UserManager.getInstance().GetAllUsers());

        // 设置窗口大小、关闭操作和可见性
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public void updateUserList(ArrayList<User> userList) {
        tableModel.setRowCount(0);

        this.userList = userList;

        for (User user : userList) {
            String[] rowData = {
                    String.valueOf(user.getID()),
                    user.getName(),
                    user.getPassword(),
                    user.getType().toString()
            };
            tableModel.addRow(rowData);
        }
    }
}
