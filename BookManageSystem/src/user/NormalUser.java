package user;

import Enums.UserType;
import book.Book;

import java.util.ArrayList;
import java.util.List;

public class NormalUser extends User
{
    private static final int maxHoldCount = 3;//默认最大持有书籍量
    protected List<Integer> holdID;
    public NormalUser(int ID, String name, String password, UserType type) {
        super(ID, name, password,type);
        holdID = new ArrayList<>();
    }

    public boolean Borrow(Book book)
    {
        if(holdID.size() >= 3)
            return false;
        holdID.add(book.getBookID());
        return true;
    }

    public boolean Return(Book book)
    {
        if(holdID.removeIf(b-> b == book.getBookID()))
            return true;
        return false;
    }
}
