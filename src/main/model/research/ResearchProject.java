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
 * A research project has a topic, published papers, and participants (Researchers only).
 */
public class ResearchProject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String topic;
    private List<ResearchPaper> publishedPapers;
    private List<User> participants;

    public ResearchProject(String topic) {
        this.topic = topic;
        this.publishedPapers = new ArrayList<>();
        this.participants = new ArrayList<>();
    }

    /**
     * Only Researchers can join. Throws NotAResearcherException otherwise.
     */
    public void addParticipant(User user) throws NotAResearcherException {
        boolean isResearcher =
                (user instanceof Teacher && ((Teacher) user).isResearcher())
                || (user instanceof GraduateStudent)
                || (user instanceof Researcher);
        if (!isResearcher) {
            throw new NotAResearcherException(user.getName() + " " + user.getSurname());
        }
        participants.add(user);
        System.out.println(user.getName() + " joined project: " + topic);
    }

    public void addPaper(ResearchPaper paper) { publishedPapers.add(paper); }

    public String getTopic() { return topic; }
    public List<ResearchPaper> getPublishedPapers() { return publishedPapers; }
    public List<User> getParticipants() { return participants; }

    @Override
    public String toString() {
        return "ResearchProject{topic='" + topic + "', participants=" + participants.size() + "}";
    }
}
