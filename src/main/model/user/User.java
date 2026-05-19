package model.user;

import java.io.Serializable;
import java.util.Objects;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.research.*;
import repository.*;
import service.*;

/**
 * Abstract base class for all users in the system.
 * Every subclass must implement getRole().
 */
public abstract class User implements Comparable<User>, Serializable, Subscribable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String surname;
    private String birthDate;
    private String phoneNumber;
    private String email;
    private String login;
    private String password;
    private Language language;

    public User() {}

    public User(String name, String surname, String birthDate,
                String phoneNumber, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.login = name.substring(0, 1).toLowerCase()
                + "_" + surname.toLowerCase() + "@kbtu.kz";
        this.password = password;
        this.language = Language.EN;
    }

    /** Every subclass declares its role. */
    public abstract String getRole();

    // ── News tab ──────────────────────────────────────────────────────────
    public String viewNewsTab() {
        String ans = "";
        int i = 0;
        for (News news : Database.news) {
            i++;
            ans += i + ") " + (news.isPinned() ? "[PINNED] " : "")
                    + "Title: " + news.getTitle()
                    + "\n    " + news.getText()
                    + "\n    " + news.getPostDate() + "\n\n";
        }
        return ans.isEmpty() ? "No news." : ans;
    }

    // ── Auth ──────────────────────────────────────────────────────────────
    public boolean signIn(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (oldPassword.equals(this.password)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

    // ── Observer callback (any User can subscribe to journals) ────────────
    @Override
    public void onNewPaperPublished(String journalName, String paperTitle, String authorName) {
        System.out.println("[JOURNAL] " + getName() + ": new paper in '" + journalName
                + "': \"" + paperTitle + "\" by " + authorName);
    }

    // ── Comparable (by surname) ───────────────────────────────────────────
    @Override
    public int compareTo(User other) {
        return this.surname.compareToIgnoreCase(other.surname);
    }

    // ── Getters & setters ────────────────────────────────────────────────
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Language getLanguage() { return language; }
    public void setLanguage(Language language) { this.language = language; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() { return Objects.hash(login); }

    @Override
    public String toString() {
        return getRole() + "{ name='" + name + "', surname='" + surname
                + "', login='" + login + "' }";
    }
}
