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
 * Standalone Researcher  an employee who is neither Teacher nor Student,
 * but is a researcher (e.g. lab scientist).
 *
 * Teacher and GraduateStudent also get researcher capabilities via
 * the ResearcherMixin composition object.
 *
 * Design decision: Researcher functionality is provided by ResearcherMixin
 * (composition) so that Teacher, GraduateStudent, AND Researcher all share
 * the same logic without diamond inheritance.
 */
public class Researcher extends Employee implements Subscribable, Serializable {
    private static final long serialVersionUID = 1L;

    private ResearcherMixin researchMixin;

    public Researcher(String name, String surname, String birthDate,
                      String phoneNumber, String email, String password) {
        super(name, surname, birthDate, phoneNumber, email, password);
        this.researchMixin = new ResearcherMixin();
    }

    public ResearcherMixin getResearchMixin() { return researchMixin; }

    public int calculateHIndex() { return researchMixin.calculateHIndex(); }

    public void addPaper(ResearchPaper p) { researchMixin.addPaper(p); }
    public void addProject(ResearchProject p) { researchMixin.addProject(p); }

    public void printPapers(Comparator<ResearchPaper> c) { researchMixin.printPapers(c); }

    @Override
    public void onNewPaperPublished(String journalName, String paperTitle, String authorName) {
        System.out.println("[JOURNAL] " + getName() + ": new paper in '" + journalName
                + "': \"" + paperTitle + "\" by " + authorName);
    }

    @Override
    public String getRole() { return "Researcher"; }
}
