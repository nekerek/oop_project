package model.course;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Attendance record for one student in one course lesson.
 */
public class Attendance implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseId;
    private String studentId;
    private Date date;
    private boolean present;
    private String note;

    public Attendance(String courseId, String studentId, boolean present, String note) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.present = present;
        this.note = note;
        this.date = new Date();
    }

    public String getCourseId() { return courseId; }
    public String getStudentId() { return studentId; }
    public Date getDate() { return date; }
    public boolean isPresent() { return present; }
    public String getNote() { return note; }

    @Override
    public String toString() {
        return date + " | " + courseId + " | " + studentId + " | "
                + (present ? "PRESENT" : "ABSENT")
                + (note == null || note.isEmpty() ? "" : " | " + note);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Attendance other)) return false;
        return Objects.equals(courseId, other.courseId)
                && Objects.equals(studentId, other.studentId)
                && Objects.equals(date, other.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, studentId, date);
    }
}
