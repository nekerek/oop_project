package model.common;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;
import controller.*;
import enumm.*;
import exception.*;
import model.course.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * University news item.
 * Research topic news is automatically PINNED (sorted first).
 * Implements Comparable so Research news appears before general news.
 */
public class News implements Serializable, Comparable<News> {
    private static final long serialVersionUID = 1L;

    private String id;
    private String topic;
    private String title;
    private String text;
    private Vector<String> comments;
    private Date postDate;
    private boolean pinned;

    public News(String id, String title, String text) {
        this(id, "General", title, text);
    }

    public News(String id, String topic, String title, String text) {
        this.id = id;
        this.topic = topic;
        this.title = title;
        this.text = text;
        this.comments = new Vector<>();
        this.postDate = new Date();
        this.pinned = "Research".equalsIgnoreCase(topic);
    }

    public void addComment(String comment) { comments.add(comment); }

    /** Pinned news (Research) sorts before regular news; then newest first. */
    @Override
    public int compareTo(News other) {
        if (this.pinned && !other.pinned) return -1;
        if (!this.pinned && other.pinned) return 1;
        return other.postDate.compareTo(this.postDate);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTopic() { return topic; }
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    public String getText() { return text; }
    public void setText(String t) { this.text = t; }
    public Vector<String> getComments() { return comments; }
    public Date getPostDate() { return postDate; }
    public boolean isPinned() { return pinned; }

    @Override
    public String toString() {
        return (pinned ? "[PINNED][" + topic + "] " : "[" + topic + "] ")
                + title + " | " + postDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof News other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
