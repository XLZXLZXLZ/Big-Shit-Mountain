package book;

import java.io.Serializable;

//书籍类，该类存储的书籍的内容及借出信息
public final class Book implements Serializable
{
    //当前最后一本书籍的ID
    private static int globalBookID = 0;

    //借出书籍的内容
    private BookInfo info;

    //书籍ID
    private int bookID;

    //借出者的ID，当没有借出者时，该值为-1
    private int borrowerID;

    public Book(BookInfo info)
    {
        this.info = info;
        borrowerID = -1;
        bookID = ++globalBookID;
    }
    public Book(String name,String author)
    {
        var info = new BookInfo(name,author);
        this.info = info;
        borrowerID = -1;
        bookID = ++globalBookID;
    }

    public int getBookID() {
        return bookID;
    }

    public int getBorrowerID()
    {
        return borrowerID;
    }

    public BookInfo getInfo()
    {
        return info;
    }
    public void setInfo(BookInfo info)
    {
        this.info = info;
    }


    //查询书籍是否已被借出
    public boolean isBorrowed()
    {
        return borrowerID > 0;
    }

    //借阅书籍函数
    public boolean BorrowBook(int borrowID)
    {
        if(isBorrowed())
        {
            System.out.println("此书已被借出");
            return false;
        }
        this.borrowerID = borrowID;
        return true;
    }

    public void ReturnBook()
    {
        this.borrowerID = -1;
    }
}

