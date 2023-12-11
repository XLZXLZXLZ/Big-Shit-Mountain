package user;

import Enums.UserType;
import book.Book;

import java.util.ArrayList;
import java.util.List;

public class NormalUser extends User
{
    private static final int maxHoldCount = 3;//默认最大持有书籍量
    protected int holdCount = 0;
    public NormalUser(int ID, String name, String password, UserType type) {
        super(ID, name, password,type);
    }

    public boolean Borrow(Book book)
    {
        if(holdCount >= maxHoldCount)
            return false;
        holdCount++;
        return true;
    }

    public boolean Return(Book book)
    {
        if(book.getBorrowerID() == getID())
        {
            holdCount --;
            return true;
        }
        return false;
    }
}
