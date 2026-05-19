package model.course;

import java.io.Serializable;
import java.util.Objects;
import java.util.Vector;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * University course. Supports CourseType (MAJOR/MINOR/FREE_ELECTIVE).
 * Can have multiple instructors (e.g. separate lecture + practice teachers).
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseName;
    private Integer credits;
    private String description;
    private String courseId;
    private Boolean isAvailable;
    private CourseType courseType;
    private String school;       // e.g. "SITE", "Oil and Gas"
    private int targetYear;
    protected Vector<String> teacher;
    private Vector<Student> students;
    protected Vector<File> courseFiles;
    private Vector<String> prerequisite;

    public Course() {}

    public Course(String courseName, String description, int credits,
                  String courseId) {
        this(courseName, description, credits, courseId,
                CourseType.MAJOR, "SITE", 1);
    }

    public Course(String courseName, int credits, String courseId) {
        this(courseName, "", credits, courseId, CourseType.MAJOR, "SITE", 1);
    }

    public Course(String courseName, String description, int credits,
                  String courseId, CourseType courseType,
                  String school, int targetYear) {
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.courseId = courseId;
        this.isAvailable = true;
        this.courseType = courseType;
        this.school = school;
        this.targetYear = targetYear;
        this.teacher = new Vector<>();
        this.students = new Vector<>();
        this.courseFiles = new Vector<>();
        this.prerequisite = new Vector<>();
    }

    public boolean addStudent(String studentId) {
        for (User user : Database.users) {
            if (user instanceof Student) {
                Student st = (Student) user;
                if (st.getId().equals(studentId)) {
                    students.add(st);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeStudent(String studentId) {
        return students.removeIf(st -> st.getId().equals(studentId));
    }

    public void addCourseFile(String fileName, String courseId, String desc) {
        File file = new File(fileName, courseId, desc);
        if (!courseFiles.contains(file)) courseFiles.add(file);
    }

    public void removeCourseFile(String fileName, String courseId) {
        courseFiles.removeIf(f -> f.getFileName().equals(fileName)
                && f.getCourseId().equals(courseId));
    }

    public void addPrereq(String prereq) { prerequisite.add(prereq); }
    public void removePrereq(String prereq) { prerequisite.remove(prereq); }

    // Getters & setters
    public String getCourseName() { return courseName; }
    public void setCourseName(String n) { this.courseName = n; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer c) { this.credits = c; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String id) { this.courseId = id; }
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean v) { this.isAvailable = v; }
    public CourseType getCourseType() { return courseType; }
    public void setCourseType(CourseType t) { this.courseType = t; }
    public String getSchool() { return school; }
    public int getTargetYear() { return targetYear; }
    public Vector<String> getTeacher() { return teacher; }
    public Vector<Student> getStudents() { return students; }
    public Vector<String> getPrerequisite() { return prerequisite; }
    public Vector<File> getCourseFiles() { return courseFiles; }

    @Override
    public String toString() {
        return courseId + "  " + courseName
                + " (" + credits + " cr, " + courseType + ", " + school + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Course other)) return false;
        return Objects.equals(courseId, other.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}
