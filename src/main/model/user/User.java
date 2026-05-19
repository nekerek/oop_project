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
 * Base class for all authenticated users in the university system.
 *
 * <p>The class stores shared identity, contact, login, password, and language
 * information. It also provides common behavior such as news viewing,
 * password changes, equality by login, and journal subscription notifications.</p>
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

    /**
     * Returns a display name for the concrete system role.
     *
     * @return role name shown in menus and user listings
     */
    public abstract String getRole();

    // ── News tab ──────────────────────────────────────────────────────────
    /**
     * Builds a formatted list of university news visible to the user.
     *
     * @return news list, or a message if no news is available
     */
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
    /**
     * Checks whether the provided credentials match this user.
     *
     * @param login login entered by the user
     * @param password password entered by the user
     * @return {@code true} if both login and password match
     */
    public boolean signIn(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

    /**
     * Changes the user's password after validating the current password.
     *
     * @param oldPassword current password
     * @param newPassword replacement password
     * @return {@code true} when the password was changed
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (oldPassword.equals(this.password)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

    // ── Observer callback (any User can subscribe to journals) ────────────
    /**
     * Receives a journal publication notification.
     *
     * @param journalName journal that published the paper
     * @param paperTitle title of the new paper
     * @param authorName author shown in the notification
     */
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
