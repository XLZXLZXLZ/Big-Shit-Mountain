package view;

import book.Book;
import manager.BookManager;
import manager.UserManager;
import user.Librarian;
import user.NormalUser;
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

public class LibrarianFrame extends JFrame
{
    //添加书籍窗口，内部类
    private class AddBookFrame extends JFrame
    {
        private JLabel name, author;
        private JTextField nameField, authorField;
        private JButton comfirmButton,returnButton;

        private AddBookFrame()
        {
            name = new JLabel("书名");
            author = new JLabel("作者");
            nameField = new JTextField(20);
            authorField = new JTextField(20);
            comfirmButton = new JButton("添加");
            returnButton = new JButton("退出");

            comfirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = nameField.getText();
                    String author = authorField.getText();

                    //检查是否输入了完整信息
                    if(name.isEmpty() || author.isEmpty())
                    {
                        JOptionPane.showMessageDialog(null, "请输入完整信息");
                        return;
                    }

                    BookManager.getInstance().AddBook(name,author);
                    AddBookCallBack();
                    dispose();
                }
            });

            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddBookCallBack();
                    dispose();
                }
            });

            setLayout(new GridLayout(3,2));
            add(name);
            add(nameField);
            add(author);
            add(authorField);
            add(comfirmButton);
            add(returnButton);

            setSize(300, 150);
            setVisible(true);
        }
    }

    private Librarian user;
    private JButton addBookButton;
    private JButton delBookButton;
    private JButton exitButton;
    private JTable bookTable;
    private JLabel chooseInfo;
    private DefaultTableModel tableModel;
    private ArrayList<Book> bookList; //书籍列表
    private int currentChooseRow; //当前选定的行

    public LibrarianFrame(User _user) {
        // 设置窗口标题
        setTitle("图书管理员窗口");

        user = (Librarian) _user;

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
                if(bookTable.getSelectedRow() <= 0)
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

        addBookButton = new JButton("添加书籍");
        delBookButton = new JButton("删除书籍");
        exitButton = new JButton("退出账号");

        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddBookFrame();
            }
        });

        delBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentChooseRow >= bookList.size() || currentChooseRow < 0)
                    return;
                Book book = bookList.get(currentChooseRow);
                if(book.isBorrowed())
                {
                    JOptionPane.showMessageDialog(null, "此书籍已被借阅，不可删除!(若有异常，使用\"强制归还\"将该书籍归还!)");
                    return;
                }
                BookManager.getInstance().DelBook(book);
                currentChooseRow = -1;
                updateBookList(BookManager.getInstance().GetAllBook());
            }
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

        buttonPanel.add(addBookButton);
        buttonPanel.add(delBookButton);
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

    private void AddBookCallBack()
    {
        updateBookList(BookManager.getInstance().GetAllBook());
    }
}