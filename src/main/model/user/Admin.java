package model.user;

import java.util.ArrayList;
import java.util.List;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.research.*;
import repository.*;
import service.*;

/**
 * System administrator. Manages users and sees action logs.
 */
public class Admin extends Employee {
    private static final long serialVersionUID = 1L;

    private List<String> actionLog;

    public Admin(String name, String surname, String birthDate,
                 String phoneNumber, String email, String password) {
        super(name, surname, birthDate, phoneNumber, email, password);
        this.actionLog = new ArrayList<>();
    }

    private void log(String action) {
        String entry = "[" + new java.util.Date() + "] " + action;
        actionLog.add(entry);
        Database.logFiles.put(String.valueOf(actionLog.size()), entry);
    }

    public void createStudent(String name, String surname, String birthDate,
                               String phoneNumber, String email, String password,
                               String id, Integer yearOfStudy) {
        Student st = new Student(name, surname, birthDate, phoneNumber, email,
                password, id, yearOfStudy, Faculty.FIT, Degree.BACHELOR);
        Database.users.add(st);
        log("Created student: " + name + " " + surname);
    }

    public void createTeacher(String name, String surname, String birthDate,
                               String phoneNumber, String email, String password,
                               String experience) {
        Teacher t = new Teacher(name, surname, birthDate, phoneNumber, email,
                password, Status.LECTOR, experience);
        Database.users.add(t);
        log("Created teacher: " + name + " " + surname);
    }

    public void createManager(String name, String surname, String birthDate,
                               String phoneNumber, String email, String password) {
        Manager m = new Manager(name, surname, birthDate, phoneNumber, email,
                password, Managers.DEPARTMENTS);
        Database.users.add(m);
        log("Created manager: " + name + " " + surname);
    }

    public void createLibrarian(String name, String surname, String birthDate,
                                 String phoneNumber, String email, String password) {
        Librarian l = new Librarian(name, surname, birthDate, phoneNumber,
                email, password);
        Database.users.add(l);
        log("Created librarian: " + name + " " + surname);
    }

    public boolean deleteUser(String login) {
        for (User u : Database.users) {
            if (u.getLogin().equals(login)) {
                Database.users.remove(u);
                log("Deleted user: " + login);
                return true;
            }
        }
        return false;
    }

    public void updateUserEmail(String login, String newEmail) {
        for (User u : Database.users) {
            if (u.getLogin().equals(login)) {
                u.setEmail(newEmail);
                log("Updated email for " + login + " -> " + newEmail);
                return;
            }
        }
        System.out.println("User not found.");
    }

    public String getUsers() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (User u : Database.users) {
            i++;
            sb.append(i).append(") ").append(u.toString()).append("\n");
        }
        return sb.toString();
    }

    public void seeLogFiles() {
        System.out.println("=== ACTION LOGS ===");
        if (Database.logFiles.isEmpty()) {
            System.out.println("No logs.");
            return;
        }
        Database.logFiles.values().forEach(System.out::println);
    }

    @Override
    public String getRole() { return "Admin"; }
    public List<String> getActionLog() { return actionLog; }
}
