package manager;
import book.Book;
import book.BookInfo;

import java.io.*;
import java.util.*;


public class BookManager
{
    private static final String dataName = "Book.dat";
    //所有书籍信息的存储
    private ArrayList<Book> books;

    //所有书籍信息与其ID的映射表，由于可能删除书籍，因此书籍ID不一定与列表一一对应
    private HashMap<Integer,Book> bookMap;

    //region 单例
    // 单例内部静态类
    private static class SingletonHolder
    {
        private static final BookManager INSTANCE = new BookManager();
    }

    // 公共静态方法，获取唯一实例
    public static BookManager getInstance()
    {
        return BookManager.SingletonHolder.INSTANCE;
    }

    //endregion
    public BookManager()
    {
        books = new ArrayList<>();
        bookMap = new HashMap<>();
        LoadBookData();
    }

    public void AddBook(Book book)
    {
        books.add(book);
        bookMap.put(book.getBookID(),book);
        SaveBookData();
    }

    //重载
    public void AddBook(String name,String author)
    {
        var info = new BookInfo(name,author);
        var b = new Book(info);
        AddBook(b);
    }

    //删除函数
    public boolean DelBook(Book book)
    {
        boolean removed = books.removeIf(b -> b.equals(book));
        if (removed)
        {
            bookMap.remove(book.getBookID());
        }
        else
        {
            System.out.println("尝试删除的书籍不存在!");
        }
        SaveBookData();
        return removed;
    }

    //借阅函数
    public boolean BorrowBook(Book book,int borrowerID)
    {
        if(book.BorrowBook(borrowerID))
        {
            SaveBookData();
            return true;
        }
        return false;
    }

    public void ReturnBook(Book book)
    {
        book.ReturnBook();
        SaveBookData();
    }


    // 获取现有书籍
    public ArrayList<Book> GetAllBook()
    {
        var bookList = new ArrayList<Book>();
        for (int i = 0; i < books.size(); i++)
        {
            bookList.add(books.get(i));
        }
        return bookList;
    }

    // 获取所有同名书籍
    public ArrayList<Book> GetBookByName(String name)
    {
        var bookList = new ArrayList<Book>();
        for (int i = 0; i < books.size(); i++)
        {
            if (books.get(i).getInfo().getName().equals(name))
                bookList.add(books.get(i));
        }
        return bookList;
    }

    // 获取所有某作者书籍
    public ArrayList<Book> GetBookByAuthor(String author)
    {
        var bookList = new ArrayList<Book>();
        for (int i = 0; i < books.size(); i++)
        {
            if (books.get(i).getInfo().getAuthor().equals(author))
                bookList.add(books.get(i));
        }
        return bookList;
    }

    public void SaveBookData()
    {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(dataName)))
        {
            outputStream.writeObject(books);
        }
        catch (IOException e)
        {
            System.out.println("保存书籍数据失败：" + e.getMessage());
        }
    }


    public void LoadBookData()
    {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dataName)))
        {
            books = (ArrayList<Book>) inputStream.readObject();
            rebuildBookMap();
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println("加载书籍数据失败：" + e.getMessage());
        }
    }

    public void ClearBookData()
    {
        books.clear();
        SaveBookData();
        LoadBookData();
    }

    private void rebuildBookMap()
    {
        bookMap.clear();
        for (Book book : books)
        {
            bookMap.put(book.getBookID(), book);
        }
    }

}
