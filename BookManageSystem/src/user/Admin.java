package user;

import Enums.UserType;
import manager.UserManager;

public class Admin extends User
{
    public Admin(int ID, String name, String password, UserType type) {
        super(ID, name, password,type);
    }

    public boolean AddLibarian(String name,String password)
    {
        return UserManager.getInstance().AddUser(name,password,UserType.Librarian);
    }
}
