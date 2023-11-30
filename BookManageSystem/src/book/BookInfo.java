package book;

import java.io.Serializable;

//书籍内容
public final class BookInfo implements Serializable
{
    public BookInfo(String name,String author)
    {
        this.name = name;
        this.author = author;
    }

    private String name;
    private String author;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

}
