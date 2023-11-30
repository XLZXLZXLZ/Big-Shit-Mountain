package user;

import Enums.UserType;
import book.Book;
import manager.BookManager;

public class Librarian extends User
{
    public Librarian(int ID, String name, String password, UserType type) {
        super(ID, name, password, type);
    }

    public void AddBook(Book book)
    {
        BookManager.getInstance().AddBook(book);
    }

    public boolean DelBook(Book book)
    {
        return BookManager.getInstance().DelBook(book);
    }
}
