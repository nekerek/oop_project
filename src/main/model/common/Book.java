package model.common;

import java.io.Serializable;
import java.util.Objects;
import controller.*;
import enumm.*;
import exception.*;
import model.course.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String id;
    private String author;

    public Book() {
        
    }

    public Book(String title, String id, String author) {
        this.title = title;
        this.id = id;
        this.author = author;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Book)) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(id, book.id) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, id, author);
    }


    @Override
    public String toString() {
        return "{" +
            " title='" + getTitle() + "'" +
            ", id='" + getId() + "'" +
            ", author='" + getAuthor() + "'" +
            "}";
    }
    
        


}
