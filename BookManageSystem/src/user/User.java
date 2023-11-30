package user;

import Enums.UserType;
import book.Book;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class User implements Serializable
{
    private int ID; //用户ID
    private String name; //用户名
    private String password; //密码
    private UserType type; //用户类型


    public User(int ID, String name, String password,UserType type)
    {
        this.ID = ID;
        this.name = name;
        this.password = password;
        this.type = type;
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

}

