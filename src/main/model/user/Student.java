package model.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.research.*;
import repository.*;
import service.*;

/**
 * General bachelor student model with academic, registration, library, and
 * optional researcher behavior.
 */
public class Student extends User {
    private static final long serialVersionUID = 1L;

    private String id;
    private Integer yearOfStudy;
    private Integer totalCredits;
    private Double GPA;
    protected Vector<Course> courses;
    private Faculty faculty;
    private Degree degree;
    private Integer creditLimit;
    private Integer chosenCredits;
    // Tracks fail count per course ID
    private Map<String, Integer> failCounts;
    // Researcher capability (optional for bachelor)
    private ResearcherMixin researchMixin;
    // Organization membership
    private String organizationName;
    private boolean isOrgHead;
    private boolean isGraduated;  // true when student has completed studies

    public Student() {}

    public Student(String name, String surname, String birthDate,
                   String phoneNumber, String email, String password,
                   String id, Integer yearOfStudy, Faculty faculty, Degree degree) {
        super(name, surname, birthDate, phoneNumber, email, password);
        this.id = id;
        this.yearOfStudy = yearOfStudy;
        this.totalCredits = 0;
        this.GPA = 0.0;
        this.courses = new Vector<>();
        this.faculty = faculty;
        this.degree = degree;
        this.creditLimit = 21;
        this.chosenCredits = 0;
        this.failCounts = new HashMap<>();
    }

    // ── Course registration ───────────────────────────────────────────────
    /**
     * Creates a pending course registration request for this student.
     *
     * <p>The request is stored for manager approval. Registration is rejected
     * when the student would exceed 21 selected credits, the course is
     * unavailable, or the student has already failed the same course three
     * times.</p>
     *
     * @param courseID identifier of the requested course
     * @throws CreditOverFlow if registration exceeds the credit limit or the course is unavailable
     * @throws CourseFailLimitException if the course was failed three times already
     */
    public void registerToCourse(String courseID)
            throws CreditOverFlow, CourseFailLimitException {
        for (Course course : Database.courses) {
            if (course.getCourseId().equals(courseID)) {
                // Check fail limit (max 3)
                int fails = failCounts.getOrDefault(courseID, 0);
                if (fails >= 3) throw new CourseFailLimitException(course.getCourseName());
                // Check credit limit
                if (chosenCredits + course.getCredits() > creditLimit)
                    throw new CreditOverFlow(
                            "Credit limit exceeded! Max " + creditLimit + " credits.");
                if (!course.getIsAvailable())
                    throw new CreditOverFlow("Course is not available.");
                Database.studentRegistration.put(this.id, course);
                return;
            }
        }
        System.out.println("Course " + courseID + " not found.");
    }

    public void increaseCredits(int creditCount) { chosenCredits += creditCount; }

    public void recordFail(String courseId) {
        failCounts.put(courseId, failCounts.getOrDefault(courseId, 0) + 1);
    }

    public void dropCourse(String courseId) {
        courses.removeIf(c -> c.getCourseId().equals(courseId));
    }

    // ── GPA & transcript ──────────────────────────────────────────────────
    /**
     * Calculates cumulative GPA from all marks belonging to this student.
     *
     * @return GPA value formatted as a string
     */
    public String totalGpa() {
        int i = 0;
        double points = 0.0;
        for (Mark mark : Database.marks) {
            if (mark.getStudentId().equals(this.id)) {
                points += mark.getTotalGrade();
                i++;
            }
        }
        if (i == 0) return "0.0";
        return convertToGPA(points / i);
    }

    /**
     * Converts a numeric total score to the university GPA scale.
     *
     * @param t total percentage score
     * @return GPA value as text
     */
    public String convertToGPA(Double t) {
        if (t >= 95) return "4.0";
        if (t >= 90) return "3.67";
        if (t >= 85) return "3.33";
        if (t >= 80) return "3.0";
        if (t >= 75) return "2.67";
        if (t >= 70) return "2.33";
        if (t >= 65) return "2.0";
        if (t >= 60) return "1.67";
        if (t >= 55) return "1.33";
        if (t >= 50) return "1.0";
        return "0.0";
    }

    /**
     * Builds a transcript containing marks, letter grades, and cumulative GPA.
     *
     * @return formatted transcript for this student
     */
    public String viewTranscript() {
        int i = 0;
        String s = "=== TRANSCRIPT: " + getName() + " " + getSurname() + " ===\n";
        double points = 0.0;
        for (Mark mark : Database.marks) {
            if (mark.getStudentId().equals(this.id)) {
                points += mark.getTotalGrade();
                i++;
                s += i + ") " + mark.getCourseName()
                        + " | 1st: " + mark.getFirstAtt()
                        + " | 2nd: " + mark.getSecondAtt()
                        + " | Final: " + mark.getFinalGrade()
                        + " | Total: " + mark.getTotalGrade()
                        + " " + mark.convertToLetter()
                        + " (GPA " + mark.convertToGPA() + ")\n";
            }
        }
        s += "Cumulative GPA: " + (i == 0 ? "0.0" : convertToGPA(points / i)) + "\n";
        return s;
    }

    public String getTranscript() { return "Transcript downloaded for " + getName(); }

    // ── View ──────────────────────────────────────────────────────────────
    public String viewMarks() {
        String s = "";
        int i = 0;
        for (Mark mark : Database.marks) {
            if (mark.getStudentId().equals(this.id)) {
                i++;
                s += i + ") " + mark.getCourseName()
                        + " | Total: " + mark.getTotalGrade()
                        + " " + mark.convertToLetter() + "\n";
            }
        }
        return s.isEmpty() ? "No marks yet." : s;
    }

    /**
     * Returns attendance records for this student.
     *
     * @return formatted attendance list, or a message when no records exist
     */
    public String viewAttendance() {
        String s = "";
        int i = 0;
        for (Attendance a : Database.attendance) {
            if (a.getStudentId().equals(this.id)) {
                i++;
                s += i + ") " + a + "\n";
            }
        }
        return s.isEmpty() ? "No attendance records." : s;
    }

    public String viewCourses() {
        String s = "";
        int i = 0;
        for (Course course : Database.courses) {
            i++;
            s += i + ") " + course.getCourseName()
                    + " [" + course.getCourseId() + "]"
                    + " | " + course.getCourseType()
                    + " | " + course.getCredits() + " cr\n";
        }
        return s.isEmpty() ? "No courses available." : s;
    }

    public String viewAvailableCourses() {
        String s = "";
        int i = 0;
        for (Course course : Database.courses) {
            if (course.getIsAvailable()) {
                i++;
                s += i + ") " + course.getCourseName()
                        + " [" + course.getCourseId() + "]"
                        + " | " + course.getCourseType()
                        + " | " + course.getCredits() + " cr\n";
            }
        }
        return s.isEmpty() ? "No available courses." : s;
    }

    public String viewCourseFiles(String courseId) {
        String s = "";
        int i = 0;
        for (File file : Database.files) {
            if (file.getCourseId().equals(courseId)) {
                i++;
                s += i + ") " + file.getFileName()
                        + "  " + file.getDescription()
                        + " | Posted: " + file.getPostDate() + "\n";
            }
        }
        return s.isEmpty() ? "No files for this course." : s;
    }

    public String viewTeacherInfo(String name) {
        for (User user : Database.users) {
            if (user instanceof Teacher && user.getName().equalsIgnoreCase(name))
                return ((Teacher) user).getAllInfo();
        }
        return "Teacher not found.";
    }

    /**
     * Adds a rating to a teacher found by name.
     *
     * @param teacherName teacher name used for lookup
     * @param rating rating value, expected to be in the UI range of 1 to 5
     */
    public void rateTeacher(String teacherName, int rating) {
        for (User user : Database.users) {
            if (user instanceof Teacher
                    && user.getName().equalsIgnoreCase(teacherName)) {
                ((Teacher) user).increaseRating(rating);
                System.out.println("Rated " + teacherName + ": " + rating + "/5");
                return;
            }
        }
        System.out.println("Teacher not found.");
    }

    public String viewBooks() {
        String s = "";
        int i = 0;
        for (Book b : Database.books) {
            i++;
            s += i + ") " + b + "\n";
        }
        return s.isEmpty() ? "No books in library." : s;
    }

    /**
     * Creates a library book order for this student.
     *
     * @param bookId identifier of the requested book
     */
    public void orderBook(String bookId) {
        for (Book book : Database.books) {
            if (book.getId().equals(bookId)) {
                Database.orders.put(this.getId(), book);
                System.out.println("Book ordered: " + book.getTitle());
                return;
            }
        }
        System.out.println("Book not found.");
    }

    // ── Researcher (optional for bachelor) ───────────────────────────────
    public boolean isResearcher() { return researchMixin != null; }
    public void becomeResearcher() {
        if (researchMixin == null) researchMixin = new ResearcherMixin();
    }
    public ResearcherMixin getResearchMixin() { return researchMixin; }

    // ── Organization ──────────────────────────────────────────────────────
    public void joinOrganization(String orgName, boolean asHead) {
        this.organizationName = orgName;
        this.isOrgHead = asHead;
        System.out.println(getName() + (asHead ? " is head of " : " joined ") + orgName);
    }

    // ── Info ──────────────────────────────────────────────────────────────
    /** Marks this student as graduated. */
    public void graduate() {
        this.isGraduated = true;
        System.out.println(getName() + " " + getSurname() + " is now GRADUATED.");
    }

    public boolean isGraduated() { return isGraduated; }

    public String getAllInfo() {
        String yearDisplay = isGraduated ? "GRADUATED" : "Year " + yearOfStudy;
        return "Student{ " + getName() + " " + getSurname()
                + " | ID: " + id
                + " | " + yearDisplay
                + " | Faculty: " + faculty
                + " | Degree: " + degree
                + " | Credits: " + chosenCredits + "/" + creditLimit
                + " | GPA: " + totalGpa()
                + " | Researcher: " + isResearcher() + " }";
    }

    @Override
    public String getRole() { return "Student"; }

    @Override
    public String toString() {
        String yearDisplay = isGraduated ? "GRADUATED" : "Year " + yearOfStudy;
        return "Student[" + getId() + "] " + getName() + " " + getSurname()
                + " | " + yearDisplay + " | GPA: " + totalGpa();
    }

    // ── Getters ───────────────────────────────────────────────────────────
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer y) { this.yearOfStudy = y; }
    public Integer getTotalCredits() { return totalCredits; }
    public Double getGPA() { return GPA; }
    public void setGPA(Double gpa) { this.GPA = gpa; }
    public Vector<Course> getCourses() { return courses; }
    public Faculty getFaculty() { return faculty; }
    public Degree getDegree() { return degree; }
    public Integer getCreditLimit() { return creditLimit; }
    public String getOrganizationName() { return organizationName; }
    public boolean isOrgHead() { return isOrgHead; }
}
