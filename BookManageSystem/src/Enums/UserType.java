package Enums;

public enum UserType
{
    NormalUser,
    Librarian,
    Admin;

    public String toString()
    {
        switch (this)
        {
            case NormalUser: return  "用户";
            case Librarian: return "图书管理员";
            case Admin: return "总管理员";
        }
        return "不知道是个啥";
    }
}
