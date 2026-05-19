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
 * Abstract superclass for all employees.
 * Any employee can send/receive messages.
 */
public abstract class Employee extends User {
    private static final long serialVersionUID = 1L;

    public Employee() {}

    public Employee(String name, String surname, String birthDate,
                    String phoneNumber, String email, String password) {
        super(name, surname, birthDate, phoneNumber, email, password);
    }

    public void sendMessage(String messageFrom, String messageTo,
                            String title, String text) {
        Message m = new Message(messageFrom, messageTo, title, text);
        Database.messages.add(m);
    }

    public String getMessages() {
        String ans = "";
        int cnt = 0;
        for (Message message : Database.messages) {
            if (message.getMessageTo().equals(this.getLogin())) {
                cnt++;
                ans += cnt + ") From: " + message.getMessageFrom()
                        + "\n    Title: " + message.getTitle()
                        + "\n    Text: " + message.getText() + "\n\n";
            }
        }
        return ans.isEmpty() ? "No messages." : ans;
    }
}
