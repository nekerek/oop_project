package model.research;

import java.io.Serializable;
import java.util.*;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * Composition object that provides researcher capabilities.
 * Used by Teacher, GraduateStudent, and Researcher to avoid code duplication.
 *
 * Pattern: this is the "mixin via composition" approach.
 */
public class ResearcherMixin implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;

    public ResearcherMixin() {
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void addPaper(ResearchPaper paper) { papers.add(paper); }
    public void addProject(ResearchProject project) { projects.add(project); }

    /**
     * Calculates the h-index:
     * Largest h such that h papers each have >= h citations.
     */
    public int calculateHIndex() {
        List<Integer> cits = new ArrayList<>();
        for (ResearchPaper p : papers) cits.add(p.getCitations());
        cits.sort(Collections.reverseOrder());
        int h = 0;
        for (int i = 0; i < cits.size(); i++) {
            if (cits.get(i) >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }

    /**
     * Prints papers sorted by the given Comparator.
     */
    public void printPapers(Comparator<ResearchPaper> c) {
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort(c);
        sorted.forEach(System.out::println);
    }

    public List<ResearchPaper> getPapers() { return papers; }
    public List<ResearchProject> getProjects() { return projects; }
}
