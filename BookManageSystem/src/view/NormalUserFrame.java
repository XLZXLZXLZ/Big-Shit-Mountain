package view;

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
import java.util.HashMap;
import java.util.Map;
import book.Book;
import book.BookInfo;
import manager.BookManager;
import manager.UserManager;
import user.NormalUser;
import user.User;

public class NormalUserFrame extends JFrame
{
    private NormalUser user;
    private JButton borrowBookButton,returnBookButton,exitButton;
    private JTable bookTable;
    private JLabel chooseInfo;
    private DefaultTableModel tableModel;
    private ArrayList<Book> bookList; //书籍列表
    private int currentChooseRow; //当前选定的行

    public NormalUserFrame(User _user) {
        // 设置窗口标题
        setTitle("用户窗口");

        user = (NormalUser) _user;

        // 设置布局管理器
        setLayout(new BorderLayout());

        String[] columnNames = {"编号","书名", "作者", "借阅状态"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(tableModel);
        bookTable.setEnabled(true);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.setRowSelectionAllowed(true);
        bookTable.setDefaultEditor(Object.class, null);
        bookTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                if(bookTable.getSelectedRow() < 0)
                    return;
                currentChooseRow  = bookTable.getSelectedRow();
                Book book = bookList.get(currentChooseRow);
                String borrowInfo;
                if(book.getBorrowerID() == user.getID())
                    borrowInfo = "你在借阅";
                else if(book.isBorrowed())
                    borrowInfo = "已被借阅";
                else
                    borrowInfo = "可借阅";

                chooseInfo.setText
                        (
                                "<html>书名: " + book.getInfo().getName()
                                        + "<br>作者: " + book.getInfo().getAuthor()
                                        + "<br>借阅状态: " + borrowInfo + "</html>"
                        );
            }
        });

        // 创建标签并设置文本
        chooseInfo= new JLabel("");
        JPanel infoPanel = new JPanel();
        infoPanel.add(chooseInfo);

        add(infoPanel,BorderLayout.CENTER);

        // 添加组件到窗口
        add(new JScrollPane(bookTable), BorderLayout.NORTH);

        borrowBookButton = new JButton("借用书籍");
        returnBookButton = new JButton("归还书籍");
        exitButton = new JButton("退出账号");

        //借阅按钮逻辑
        borrowBookButton.addActionListener(e -> {
            var result = UserManager.getInstance().BorrowBook(user,bookList.get(currentChooseRow));
            switch (result)
            {
                case BOOK_BORROWED -> JOptionPane.showMessageDialog(null, "该书籍已被借用!");
                case USER_LIMIT_REACHED -> JOptionPane.showMessageDialog(null, "您已达到借阅上限!");
                case SUCCESS -> JOptionPane.showMessageDialog(null, "借阅成功");
            }
            updateBookList(bookList);
            currentChooseRow = -1;
        });

        //归还按钮逻辑
        returnBookButton.addActionListener(e -> {
            if(currentChooseRow<0)
                return;
            var result = UserManager.getInstance().ReturnBook(user,bookList.get(currentChooseRow));
            if(result)
                JOptionPane.showMessageDialog(null, "归还成功");
            updateBookList(bookList);
        });

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

        buttonPanel.add(borrowBookButton);
        buttonPanel.add(returnBookButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);


        updateBookList(BookManager.getInstance().GetAllBook());

        // 设置窗口大小、关闭操作和可见性
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public void updateBookList(ArrayList<Book> bookList)
    {
        tableModel.setRowCount(0);

        this.bookList = bookList;

        int bookCount = 0;
        for (Book book : bookList)
        {
            String borrowInfo;
            if(book.getBorrowerID()  == user.getID())
                borrowInfo = "你在借阅";
            else if(book.isBorrowed())
                borrowInfo = "已被借阅";
            else
                borrowInfo = "可借阅";

            String[] rowData =
            {
                    Integer.toString(++bookCount),
                    book.getInfo().getName(),
                    book.getInfo().getAuthor(),
                    borrowInfo
            };
            tableModel.addRow(rowData);
        }
    }
}