package model.user;

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
 * Teacher role responsible for course materials, marks, complaints, attendance,
 * and student information.
 *
 * <p>Professors are initialized as researchers automatically. Other teacher
 * statuses can gain researcher capabilities by calling {@link #becomeResearcher()}.</p>
 */
public class Teacher extends Employee {
    private static final long serialVersionUID = 1L;

    private Status teacherStatus;
    private Vector<Course> courses;
    private String experience;
    private double rating;
    private int ratingCnt;
    private ResearcherMixin researchMixin; // null if not a researcher

    public Teacher() {}

    /**
     * Creates a teacher with the specified academic status.
     *
     * @param name first name
     * @param surname family name
     * @param birthDate date of birth
     * @param phoneNumber contact phone
     * @param email email address
     * @param password initial password
     * @param teacherStatus academic position
     * @param experience teaching experience description
     */
    public Teacher(String name, String surname, String birthDate,
                   String phoneNumber, String email, String password,
                   Status teacherStatus, String experience) {
        super(name, surname, birthDate, phoneNumber, email, password);
        this.teacherStatus = teacherStatus;
        this.courses = new Vector<>();
        this.experience = experience;
        this.rating = 0.0;
        this.ratingCnt = 0;
        // Professors are ALWAYS researchers
        if (teacherStatus == Status.PROFESSOR) {
            this.researchMixin = new ResearcherMixin();
        }
    }

    // ── Researcher capability ─────────────────────────────────────────────
    public boolean isResearcher() { return researchMixin != null; }

    public void becomeResearcher() {
        if (researchMixin == null) researchMixin = new ResearcherMixin();
    }

    public ResearcherMixin getResearchMixin() { return researchMixin; }

    // ── Complaint to dean with urgency ────────────────────────────────────
    /**
     * Sends a complaint about a student to the dean (Manager).
     */
    /**
     * Sends a complaint about a student to the dean/manager as an official message.
     *
     * @param student student being reported
     * @param reason complaint reason
     * @param urgency complaint urgency level
     * @param dean manager receiving the complaint
     */
    public void sendComplaint(Student student, String reason,
                              ComplaintUrgency urgency, Manager dean) {
        String subject = "[" + urgency + "] Complaint about "
                + student.getName() + " " + student.getSurname();
        String body = "From teacher: " + getName() + " " + getSurname()
                + "\nStudent: " + student.getName() + " " + student.getSurname()
                + " (ID: " + student.getId() + ")"
                + "\nReason: " + reason
                + "\nUrgency: " + urgency;
        sendMessage(getLogin(), dean.getLogin(), subject, body);
        System.out.println("Complaint sent to dean about "
                + student.getName() + " | Urgency: " + urgency);
    }

    // ── Marks ─────────────────────────────────────────────────────────────
    /**
     * Adds a mark for a student in a course.
     *
     * @param courseName course title
     * @param studentId student identifier
     * @param firstAtt first attestation score
     * @param secondAtt second attestation score
     * @param finalGrade final exam score
     */
    public void putMark(String courseName, String studentId,
                        Double firstAtt, Double secondAtt, Double finalGrade) {
        Mark m = new Mark(courseName, studentId, firstAtt, secondAtt, finalGrade);
        Database.marks.add(m);
        if (!m.isPassed()) {
            System.out.println("WARNING: Student " + studentId
                    + " failed " + courseName);
        }
    }

    /**
     * Returns all marks recorded for a course.
     *
     * @param courseName course title used for filtering
     * @return formatted mark list
     */
    public String viewMarks(String courseName) {
        String s = "";
        int i = 0;
        for (Mark mark : Database.marks) {
            if (mark.getCourseName().equals(courseName)) {
                i++;
                s += i + ") Student: " + mark.getStudentId()
                        + "\n    1st: " + mark.getFirstAtt()
                        + " | 2nd: " + mark.getSecondAtt()
                        + " | Final: " + mark.getFinalGrade()
                        + "\n    Total: " + mark.getTotalGrade()
                        + " " + mark.convertToLetter()
                        + " (GPA " + mark.convertToGPA() + ")\n\n";
            }
        }
        return s.isEmpty() ? "No marks for this course." : s;
    }

    /**
     * Records attendance for a student in a course.
     *
     * @param courseId course identifier
     * @param studentId student identifier
     * @param present whether the student was present
     * @param note optional attendance note
     */
    public void markAttendance(String courseId, String studentId,
                               boolean present, String note) {
        Database.attendance.add(new Attendance(courseId, studentId, present, note));
    }

    /**
     * Returns attendance records for a course.
     *
     * @param courseId course identifier
     * @return formatted attendance records
     */
    public String viewAttendance(String courseId) {
        String ans = "";
        int i = 0;
        for (Attendance a : Database.attendance) {
            if (a.getCourseId().equals(courseId)) {
                i++;
                ans += i + ") " + a + "\n";
            }
        }
        return ans.isEmpty() ? "No attendance for this course." : ans;
    }

    // ── Students ──────────────────────────────────────────────────────────
    public String viewStudents() {
        String ans = "";
        int i = 0;
        for (User user : Database.users) {
            if (user instanceof GraduateStudent) {
                GraduateStudent gs = (GraduateStudent) user;
                i++;
                ans += i + ") [" + gs.getGraduateDegree() + "] "
                        + gs.getName() + " " + gs.getSurname()
                        + " | ID: " + gs.getId()
                        + " | Year: " + gs.getYearOfStudy()
                        + " | Faculty: " + gs.getFaculty() + "\n";
            } else if (user instanceof Student) {
                Student st = (Student) user;
                i++;
                ans += i + ") [BACHELOR] " + st.getName() + " " + st.getSurname()
                        + " | ID: " + st.getId()
                        + " | Year: " + st.getYearOfStudy()
                        + " | Faculty: " + st.getFaculty() + "\n";
            }
        }
        return ans.isEmpty() ? "No students." : ans;
    }

    public String viewStudentInfo(String name) {
        for (User user : Database.users) {
            if (user instanceof Student) {
                Student st = (Student) user;
                if (st.getName().equalsIgnoreCase(name)) return st.getAllInfo();
            }
        }
        return "Student not found.";
    }

    // ── Courses ───────────────────────────────────────────────────────────
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
        return s.isEmpty() ? "No courses." : s;
    }

    // ── Files ─────────────────────────────────────────────────────────────
    public void addCourseFile(String fileName, String courseId, String description) {
        Database.files.add(new File(fileName, courseId, description));
    }

    public void deleteCourseFile(String fileName, String courseId) {
        Database.files.removeIf(f ->
                f.getFileName().equals(fileName) && f.getCourseId().equals(courseId));
    }

    // ── Rating ────────────────────────────────────────────────────────────
    public void increaseRating(int r) { ratingCnt++; rating += r; }

    public double viewRating() {
        return ratingCnt == 0 ? 0 : rating / ratingCnt;
    }

    // ── Info ──────────────────────────────────────────────────────────────
    public String getAllInfo() {
        return "Teacher{ " + getName() + " " + getSurname()
                + " | " + teacherStatus
                + " | exp: " + experience
                + " | rating: " + String.format("%.1f", viewRating())
                + " | researcher: " + isResearcher() + " }";
    }

    /**
     * Teacher removes a student from one of their courses.
     */
    public String removeStudentFromCourse(String studentId, String courseId) {
        for (Course course : Database.courses) {
            if (course.getCourseId().equals(courseId)) {
                // Verify this teacher teaches the course
                if (!course.getTeacher().contains(getLogin())) {
                    return "You are not assigned to course " + courseId;
                }
                boolean removed = course.removeStudent(studentId);
                if (removed) {
                    // Also remove from student's own course list
                    for (User u : Database.users) {
                        if (u instanceof Student) {
                            Student st = (Student) u;
                            if (st.getId().equals(studentId)) {
                                st.dropCourse(courseId);
                                break;
                            }
                        }
                    }
                    return "Student " + studentId + " removed from " + course.getCourseName();
                } else {
                    return "Student " + studentId + " is not enrolled in " + course.getCourseName();
                }
            }
        }
        return "Course " + courseId + " not found.";
    }

    @Override
    public String getRole() { return "Teacher"; }

    // ── Getters ───────────────────────────────────────────────────────────
    public Status getTeacherStatus() { return teacherStatus; }
    public void setTeacherStatus(Status s) { this.teacherStatus = s; }
    public Vector<Course> getCourses() { return courses; }
    public String getExperience() { return experience; }
    public double getRating() { return rating; }
    public int getRatingCnt() { return ratingCnt; }
}
