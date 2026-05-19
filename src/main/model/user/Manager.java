package model.user;

import java.util.Collections;
import java.util.HashMap;
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
 * University manager role for academic operations.
 *
 * <p>Managers create courses, approve registration requests, assign teachers,
 * manage news, inspect students and teachers, and generate academic reports.</p>
 */
public class Manager extends Employee implements Reportable {
    private static final long serialVersionUID = 1L;

    private Managers managerType;

    public Manager(String name, String surname, String birthDate,
                   String phoneNumber, String email, String password,
                   Managers managerType) {
        super(name, surname, birthDate, phoneNumber, email, password);
        this.managerType = managerType;
    }

    // ── Courses ───────────────────────────────────────────────────────────
    /** FIXED: was never adding when courses list was empty */
    /**
     * Creates a basic major course if the course identifier is not already used.
     *
     * @param name course name
     * @param credits credit count
     * @param courseId unique course identifier
     */
    public void createCourse(String name, int credits, String courseId) {
        for (Course course : Database.courses) {
            if (course.getCourseId().equals(courseId)) {
                System.out.println("Course " + courseId + " already exists.");
                return;
            }
        }
        Database.courses.add(new Course(name, credits, courseId));
        System.out.println("Course created: " + name);
    }

    /**
     * Creates a course with registration metadata.
     *
     * @param name course name
     * @param credits credit count
     * @param courseId unique course identifier
     * @param type course category
     * @param school school offering the course
     * @param year target year of study
     */
    public void createCourse(String name, int credits, String courseId,
                              CourseType type, String school, int year) {
        for (Course c : Database.courses) {
            if (c.getCourseId().equals(courseId)) { System.out.println("Already exists."); return; }
        }
        Database.courses.add(new Course(name, "", credits, courseId, type, school, year));
        System.out.println("Course created: " + name + " [" + type + "]");
    }

    /**
     * Assigns a teacher login/name to a course instructor list.
     *
     * @param courseId course identifier
     * @param teacherName teacher value stored for the assignment
     */
    public void assignCourseToTeachers(String courseId, String teacherName) {
        for (Course course : Database.courses) {
            if (course.getCourseId().equals(courseId)) {
                course.getTeacher().add(teacherName);
                System.out.println("Assigned " + teacherName + " to " + courseId);
                return;
            }
        }
    }

    public String viewRequests() {
        return Database.studentRegistration.isEmpty()
                ? "No pending registrations."
                : Database.studentRegistration.toString();
    }

    /**
     * Approves or rejects a pending student course registration.
     *
     * @param studentId student identifier
     * @param courseId course identifier
     * @param approve expected value {@code ACCEPT} to approve, anything else rejects
     * @return result message for the manager menu
     */
    public String approveRegistration(String studentId, String courseId,
                                       String approve) {
        Student st = null;
        for (User user : Database.users) {
            if (user instanceof Student
                    && ((Student) user).getId().equals(studentId)) {
                st = (Student) user;
                break;
            }
        }
        if (st == null) return "Student not found.";

        Course c = null;
        for (Course course : Database.courses) {
            if (course.getCourseId().equals(courseId)) { c = course; break; }
        }
        if (c == null) return "Course not found.";

        if (Database.studentRegistration.containsKey(studentId)) {
            if (approve.equalsIgnoreCase("ACCEPT")) {
                Database.studentRegistration.remove(studentId);
                st.increaseCredits(c.getCredits());
                st.courses.add(c);
                return "Registration ACCEPTED for " + st.getName();
            } else {
                Database.studentRegistration.remove(studentId);
                return "Registration REJECTED for " + st.getName();
            }
        }
        return "No pending registration found for this student/course.";
    }

    // ── Students & teachers info ──────────────────────────────────────────
    public String infoStudents() {
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
                        + " | Faculty: " + gs.getFaculty()
                        + " | Supervisor: " + (gs.getSupervisor() != null
                            ? gs.getSupervisor().getName() : "none")
                        + " | GPA: " + gs.totalGpa() + "\n";
            } else if (user instanceof Student) {
                Student st = (Student) user;
                i++;
                String yearDisplay = st.isGraduated() ? "GRADUATED" : "Year " + st.getYearOfStudy();
                ans += i + ") [BACHELOR] " + st.getName() + " " + st.getSurname()
                        + " | ID: " + st.getId()
                        + " | " + yearDisplay
                        + " | Faculty: " + st.getFaculty()
                        + " | GPA: " + st.totalGpa() + "\n";
            }
        }
        return ans.isEmpty() ? "No students." : ans;
    }

    public String infoTeachers(String teacherName) {
        for (User user : Database.users) {
            if (user instanceof Teacher
                    && user.getName().equalsIgnoreCase(teacherName))
                return ((Teacher) user).getAllInfo();
        }
        return "Teacher not found.";
    }

    /** FIXED: s1 vs s2 (was reversed  alphabetical now A→Z) */
    /**
     * Returns students sorted alphabetically by name.
     *
     * @return formatted sorted student list
     */
    public String orderStudentsAlphabetically() {
        Vector<Student> s = new Vector<>();
        for (User u : Database.users)
            if (u instanceof Student) s.add((Student) u);
        s.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        return buildStudentList(s);
    }

    /**
     * Returns students sorted by GPA using the GPA comparator strategy.
     *
     * @return formatted sorted student list
     */
    public String orderStudentsByGPA() {
        Vector<Student> s = new Vector<>();
        for (User u : Database.users)
            if (u instanceof Student) s.add((Student) u);
        s.sort(new GPASorter());
        return buildStudentList(s);
    }

    public String orderTeachersAlphabetically() {
        Vector<Teacher> t = new Vector<>();
        for (User u : Database.users)
            if (u instanceof Teacher) t.add((Teacher) u);
        t.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return buildTeacherList(t);
    }

    public String orderTeachersStatus() {
        Vector<Teacher> t = new Vector<>();
        for (User u : Database.users)
            if (u instanceof Teacher) t.add((Teacher) u);
        t.sort(new StatusComparator());
        return buildTeacherList(t);
    }

    private String buildStudentList(Vector<Student> s) {
        String ans = "";
        int i = 0;
        for (Student st : s) {
            i++;
            String type = (st instanceof GraduateStudent)
                    ? "[" + ((GraduateStudent) st).getGraduateDegree() + "]"
                    : "[BACHELOR]";
            ans += i + ") " + type + " " + st.getName() + " " + st.getSurname()
                    + " | GPA: " + st.totalGpa()
                    + " | " + st.getFaculty() + "\n";
        }
        return ans.isEmpty() ? "No students." : ans;
    }

    private String buildTeacherList(Vector<Teacher> t) {
        String ans = "";
        int i = 0;
        for (Teacher tt : t) {
            i++;
            ans += i + ") " + tt.getName() + " " + tt.getSurname()
                    + " | " + tt.getTeacherStatus()
                    + " | Rating: " + String.format("%.1f", tt.viewRating()) + "\n";
        }
        return ans.isEmpty() ? "No teachers." : ans;
    }

    // ── News ──────────────────────────────────────────────────────────────
    /**
     * Adds a news item and re-sorts news so research items remain pinned.
     *
     * @param id news identifier
     * @param topic news topic
     * @param title news title
     * @param text news body
     */
    public void addNews(String id, String topic, String title, String text) {
        Database.news.add(new News(id, topic, title, text));
        Collections.sort(Database.news);
        System.out.println("News added: " + title);
    }

    public void removeNews(String id) {
        Database.news.removeIf(n -> n.getId().equals(id));
    }

    public void updateNews(String oldId, String newId, String title, String text) {
        for (int i = 0; i < Database.news.size(); i++) {
            if (Database.news.get(i).getId().equals(oldId)) {
                Database.news.set(i,
                        new News(newId, Database.news.get(i).getTopic(), title, text));
                Collections.sort(Database.news);
                return;
            }
        }
    }

    // ── Statistics ────────────────────────────────────────────────────────
    public Double getMaxScore() {
        return Database.marks.stream()
                .mapToDouble(Mark::getTotalGrade).max().orElse(0);
    }

    public Double getMinScore() {
        return Database.marks.stream()
                .mapToDouble(Mark::getTotalGrade).min().orElse(0);
    }

    public Double getAvgScore() {
        return Database.marks.stream()
                .mapToDouble(Mark::getTotalGrade).average().orElse(0);
    }

    public long retakeCount() {
        return Database.marks.stream()
                .filter(m -> !m.isPassed()).count();
    }

    // ── Reportable interface ──────────────────────────────────────────────
    @Override
    /**
     * Generates aggregate academic performance statistics.
     *
     * @return text report containing student count, mark count, score extremes,
     *         average score, and fail count
     */
    public String generateReport() {
        return "=== ACADEMIC REPORT ===\n"
                + "Total students: " + Database.users.stream()
                    .filter(u -> u instanceof Student).count() + "\n"
                + "Total marks: " + Database.marks.size() + "\n"
                + "Max score:   " + String.format("%.1f", getMaxScore()) + "\n"
                + "Min score:   " + String.format("%.1f", getMinScore()) + "\n"
                + "Avg score:   " + String.format("%.1f", getAvgScore()) + "\n"
                + "Fails:       " + retakeCount() + "\n";
    }

    /**
     * Marks a student as graduated. Their year shows as "GRADUATED" everywhere.
     */
    public String graduateStudent(String studentId) {
        for (User u : Database.users) {
            if (u instanceof Student) {
                Student st = (Student) u;
                if (st.getId().equals(studentId)) {
                    st.graduate();
                    return st.getName() + " " + st.getSurname() + " marked as GRADUATED.";
                }
            }
        }
        return "Student " + studentId + " not found.";
    }

    /**
     * Manager removes a student from a specific course.
     */
    public String removeStudentFromCourse(String studentId, String courseId) {
        for (Course course : Database.courses) {
            if (course.getCourseId().equals(courseId)) {
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

    /**
     * Manager removes (deletes) a course entirely from the system.
     */
    public String deleteCourse(String courseId) {
        for (Course course : Database.courses) {
            if (course.getCourseId().equals(courseId)) {
                Database.courses.remove(course);
                return "Course " + course.getCourseName() + " deleted.";
            }
        }
        return "Course " + courseId + " not found.";
    }

    @Override
    public String getRole() { return "Manager"; }
    public Managers getManagerType() { return managerType; }
}
