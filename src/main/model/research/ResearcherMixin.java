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
 * Composition component that provides reusable researcher capabilities.
 *
 * <p>It stores papers and projects and is embedded into roles that can act as
 * researchers, avoiding duplicated h-index and paper-printing logic.</p>
 */
public class ResearcherMixin implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;

    public ResearcherMixin() {
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    /**
     * Adds a paper to the researcher's publication list.
     *
     * @param paper paper to add
     */
    public void addPaper(ResearchPaper paper) { papers.add(paper); }
    public void addProject(ResearchProject project) { projects.add(project); }

    /**
     * Calculates the h-index from paper citation counts.
     *
     * @return largest h such that h papers each have at least h citations
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
     * Prints papers sorted by the given comparator strategy.
     *
     * @param c comparator defining paper order
     */
    public void printPapers(Comparator<ResearchPaper> c) {
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort(c);
        sorted.forEach(System.out::println);
    }

    public List<ResearchPaper> getPapers() { return papers; }
    public List<ResearchProject> getProjects() { return projects; }
}
