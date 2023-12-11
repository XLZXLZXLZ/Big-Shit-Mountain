package manager;

import Enums.BorrowResult;
import book.Book;
import user.Admin;
import user.Librarian;
import user.NormalUser;
import user.User;
import Enums.UserType;

import java.io.*;
import java.util.*;

public class UserManager
{
    private final static String dataName = "User.dat";
    private ArrayList<User> users;
    //用户名字与用户的键值对
    private HashMap<String, User> userMap;

    //region 单例
        // 单例内部静态类
        private static class SingletonHolder
        {
            private static final UserManager INSTANCE = new UserManager();
        }

        // 公共静态方法，获取唯一实例
        public static UserManager getInstance()
        {
            return SingletonHolder.INSTANCE;
        }

    //endregion
    public UserManager()
    {
        users = new ArrayList<>();
        userMap = new HashMap<>();
        LoadUserData();

        //假设列表里无人，默认添加一个管理员
        if(users.isEmpty())
            AddUser("Admin","123",UserType.Admin);
    }

    public User Authenticate(String name, String password)
    {
        User user = userMap.get(name);

        if(user == null)
        {
            System.out.println("用户名不存在");
            return null;
        }

        if(password.equals(user.getPassword()))
            return user;

        System.out.println("密码错误");
        return null;
    }

    public boolean AddUser(String name, String password, UserType type)
    {
        User user = null;
        switch (type)
        {
            case Admin : user = new Admin(userMap.size()+1,name,password,type); break;
            case NormalUser: user = new NormalUser(userMap.size()+1,name,password,type); break;
            case Librarian: user = new Librarian(userMap.size()+1,name,password,type); break;
        }

        for (User existingUser : userMap.values())
        {
            if (existingUser.getName().equals(name))
            {
                System.out.println("已重名");
                return false; // 存在重名用户，返回 false
            }
        }

        if(user == null)
        {
            System.out.println("未知错误，注册用户失败");
            return false;
        }

        users.add(user);
        userMap.put(name,user);

        SaveUserData();

        return true; // 无重名用户，返回 true
    }

    //尝试借阅书籍
    public BorrowResult BorrowBook(NormalUser user, Book book)
    {
        //我好像解耦合做的像坨shi
        if(book.isBorrowed())
            return BorrowResult.BOOK_BORROWED;
        boolean success = user.Borrow(book);
        if(!success)
            return BorrowResult.USER_LIMIT_REACHED;
        BookManager.getInstance().BorrowBook(book,user.getID());
        SaveUserData();
        return BorrowResult.SUCCESS;
    }

    public boolean ReturnBook(NormalUser user,Book book)
    {
        if(!user.Return(book))
            return false;
        BookManager.getInstance().ReturnBook(book);
        SaveUserData();
        return true;
    }

    public ArrayList<User> GetAllUsers()
    {
        return users;
    }

    public void SaveUserData()
    {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(dataName)))
        {
            outputStream.writeObject(users);
        }
        catch (IOException e)
        {
            System.out.println("保存用户数据失败：" + e.getMessage());
        }
    }

    public void LoadUserData()
    {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dataName)))
        {
            users = (ArrayList<User>) inputStream.readObject();
            rebuildUserMap();
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println("加载用户数据失败：" + e.getMessage());
        }
    }

    public void ClearUserData()
    {
        users.clear();
        SaveUserData();
        LoadUserData();
    }

    private void rebuildUserMap()
    {
        userMap.clear();
        for (User user : users)
        {
            userMap.put(user.getName(), user);
        }
    }
}

