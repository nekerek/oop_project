package model.research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * University research journal acting as the subject in the Observer pattern.
 *
 * <p>Any {@link Subscribable} user can subscribe. Publishing a paper stores it,
 * creates a pinned research-news announcement, and notifies all subscribers.</p>
 */
public class UniversityJournal implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Subscribable> subscribers;
    private List<ResearchPaper> papers;

    /**
     * Creates an empty university journal.
     *
     * @param name journal name
     */
    public UniversityJournal(String name) {
        this.name = name;
        this.subscribers = new ArrayList<>();
        this.papers = new ArrayList<>();
    }

    /**
     * Subscribes a user to paper publication notifications.
     *
     * @param s subscriber to add
     */
    public void subscribe(Subscribable s) {
        if (!subscribers.contains(s)) {
            subscribers.add(s);
            System.out.println("Subscribed to journal: " + name);
        }
    }

    public void unsubscribe(Subscribable s) {
        subscribers.remove(s);
    }

    /**
     * Publishes a paper, creates a research news announcement, and notifies subscribers.
     *
     * @param paper paper being published
     * @param authorName author name used in news and notifications
     */
    public void publishPaper(ResearchPaper paper, String authorName) {
        papers.add(paper);
        // Auto-announce as Research news
        News announcement = new News(
                "J_" + System.currentTimeMillis(),
                "Research",
                "New paper in " + name + ": " + paper.getTitle(),
                "Published by " + authorName + ". DOI: " + paper.getDoi()
        );
        Database.news.add(announcement);
        java.util.Collections.sort(Database.news); // keep Research pinned on top
        // Notify observers
        for (Subscribable sub : subscribers) {
            sub.onNewPaperPublished(name, paper.getTitle(), authorName);
        }
    }

    public String getName() { return name; }
    public List<ResearchPaper> getPapers() { return papers; }
    public int getSubscriberCount() { return subscribers.size(); }

    @Override
    public String toString() {
        return "Journal: " + name + " | " + papers.size() + " papers | "
                + subscribers.size() + " subscribers";
    }
}
