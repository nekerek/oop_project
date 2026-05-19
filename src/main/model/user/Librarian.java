package model.user;

import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.research.*;
import repository.*;
import service.*;

/**
 * Library staff. Manages books and book orders.
 */
public class Librarian extends Employee {
    private static final long serialVersionUID = 1L;

    public Librarian(String name, String surname, String birthDate,
                     String phoneNumber, String email, String password) {
        super(name, surname, birthDate, phoneNumber, email, password);
    }

    public void addBook(String title, String id, String author) {
        Book b = new Book(title, id, author);
        if (!Database.books.contains(b)) {
            Database.books.add(b);
            System.out.println("Book added: " + title);
        }
    }

    public void removeBook(String id) {
        Database.books.removeIf(b -> b.getId().equals(id));
    }

    public String updateOrderBook(String studentId, String bookId, String request) {
        Book target = null;
        for (Book book : Database.books) {
            if (book.getId().equals(bookId)) { target = book; break; }
        }
        if (target == null) return "Book not found.";
        if (!Database.orders.containsKey(studentId)) return "Order not found.";
        if (request.equalsIgnoreCase("ACCEPT")) {
            Database.orders.remove(studentId);
            return "Book order ACCEPTED for " + studentId;
        } else {
            Database.orders.remove(studentId);
            return "Book order REJECTED for " + studentId;
        }
    }

    public String viewBooks() {
        String s = "";
        int i = 0;
        for (Book b : Database.books) {
            i++;
            s += i + ") " + b + "\n";
        }
        return s.isEmpty() ? "No books." : s;
    }

    public String viewOrders() {
        return Database.orders.isEmpty() ? "No orders." : Database.orders.toString();
    }

    @Override
    public String getRole() { return "Librarian"; }
}
