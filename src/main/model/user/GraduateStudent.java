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
 * Graduate student model for master and PhD students.
 *
 * <p>Graduate students are always researchers and may have diploma papers.
 * Supervisor assignment enforces the project rule that the supervisor must be
 * a researcher with h-index of at least 3.</p>
 */
public class GraduateStudent extends Student {
    private static final long serialVersionUID = 1L;

    private Degree graduateDegree; // MASTER or PHD
    private Teacher supervisor;
    private List<ResearchPaper> diplomaPapers;
    private ResearcherMixin researchMixin;

    /**
     * Creates a graduate student with researcher capabilities enabled.
     *
     * @param name first name
     * @param surname family name
     * @param birthDate date of birth
     * @param phoneNumber contact phone
     * @param email email address
     * @param password initial password
     * @param id student identifier
     * @param yearOfStudy current year of study
     * @param faculty faculty affiliation
     * @param graduateDegree graduate degree, usually MASTER or PHD
     */
    public GraduateStudent(String name, String surname, String birthDate,
                            String phoneNumber, String email, String password,
                            String id, Integer yearOfStudy, Faculty faculty,
                            Degree graduateDegree) {
        super(name, surname, birthDate, phoneNumber, email, password,
                id, yearOfStudy, faculty, graduateDegree);
        this.graduateDegree = graduateDegree;
        this.diplomaPapers = new ArrayList<>();
        this.researchMixin = new ResearcherMixin(); // always a researcher
    }

    /**
     * Assigns a research supervisor after validating researcher status and h-index.
     *
     * @param supervisor teacher selected as supervisor
     * @throws LowHIndexException if the teacher is not a researcher or has h-index below 3
     */
    public void setSupervisor(Teacher supervisor) throws LowHIndexException {
        if (!supervisor.isResearcher()) {
            throw new LowHIndexException(
                    supervisor.getName() + " " + supervisor.getSurname(), 0);
        }
        int h = supervisor.getResearchMixin().calculateHIndex();
        if (h < 3) {
            throw new LowHIndexException(
                    supervisor.getName() + " " + supervisor.getSurname(), h);
        }
        this.supervisor = supervisor;
        System.out.println(getName() + " assigned supervisor: "
                + supervisor.getName() + " (h=" + h + ")");
    }

    /**
     * Adds a diploma paper and includes it in the student's research profile.
     *
     * @param paper diploma paper to register
     */
    public void addDiplomaPaper(ResearchPaper paper) {
        diplomaPapers.add(paper);
        researchMixin.addPaper(paper);
    }

    public int calculateHIndex() { return researchMixin.calculateHIndex(); }
    public void printPapers(java.util.Comparator<ResearchPaper> c) {
        researchMixin.printPapers(c);
    }

    @Override
    public String getAllInfo() {
        return getRole() + "{ " + getName() + " " + getSurname()
                + " | ID: " + getId()
                + " | Degree: " + graduateDegree
                + " | Year: " + getYearOfStudy()
                + " | Faculty: " + getFaculty()
                + " | Supervisor: " + (supervisor != null ? supervisor.getName() + " " + supervisor.getSurname() : "none")
                + " | h-index: " + calculateHIndex()
                + " | Diploma papers: " + diplomaPapers.size()
                + " | GPA: " + totalGpa() + " }";
    }

    @Override
    public String getRole() { return "GraduateStudent(" + graduateDegree + ")"; }

    public Degree getGraduateDegree() { return graduateDegree; }
    public Teacher getSupervisor() { return supervisor; }
    public List<ResearchPaper> getDiplomaPapers() { return diplomaPapers; }
    public ResearcherMixin getResearchMixin() { return researchMixin; }

    @Override
    public String toString() {
        return getRole() + "[" + getId() + "] " + getName() + " " + getSurname()
                + " | Supervisor: "
                + (supervisor != null ? supervisor.getName() : "none");
    }
}
