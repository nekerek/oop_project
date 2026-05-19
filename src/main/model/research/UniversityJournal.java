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
 * University research journal.
 *
 * PATTERN: Observer
 *   - Subject: UniversityJournal
 *   - Observer: Subscribable (any User can subscribe)
 *   When a new paper is published, ALL subscribers are notified.
 */
public class UniversityJournal implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Subscribable> subscribers;
    private List<ResearchPaper> papers;

    public UniversityJournal(String name) {
        this.name = name;
        this.subscribers = new ArrayList<>();
        this.papers = new ArrayList<>();
    }

    public void subscribe(Subscribable s) {
        if (!subscribers.contains(s)) {
            subscribers.add(s);
            System.out.println("Subscribed to journal: " + name);
        }
    }

    public void unsubscribe(Subscribable s) {
        subscribers.remove(s);
    }

    /** Publishes a paper and notifies all subscribers. */
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
