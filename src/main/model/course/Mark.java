package model.course;

import java.io.Serializable;
import java.util.Objects;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * Assessment result for one student in one course.
 *
 * <p>The total grade is calculated from first attestation, second attestation,
 * and final exam scores.</p>
 */
public class Mark implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseName;
    private String studentId;
    private Double firstAtt;
    private Double secondAtt;
    private Double finalGrade;
    private Double totalGrade;

    /**
     * Creates a mark and calculates the total grade.
     *
     * @param courseName course title
     * @param studentId student identifier
     * @param firstAtt first attestation score
     * @param secondAtt second attestation score
     * @param finalGrade final exam score
     */
    public Mark(String courseName, String studentId,
                Double firstAtt, Double secondAtt, Double finalGrade) {
        this.courseName = courseName;
        this.studentId = studentId;
        this.firstAtt = firstAtt;
        this.secondAtt = secondAtt;
        this.finalGrade = finalGrade;
        this.totalGrade = firstAtt + secondAtt + finalGrade;
    }

    /**
     * Checks whether the student passed the course.
     *
     * @return {@code true} when total grade is at least 50
     */
    public boolean isPassed() { return totalGrade >= 50; }

    /**
     * Converts the numeric total grade to a letter grade.
     *
     * @return letter grade according to the university scale
     */
    public String convertToLetter() {
        if (totalGrade >= 95) return "A";
        if (totalGrade >= 90) return "A-";
        if (totalGrade >= 85) return "B+";
        if (totalGrade >= 80) return "B";
        if (totalGrade >= 75) return "B-";
        if (totalGrade >= 70) return "C+";
        if (totalGrade >= 65) return "C";
        if (totalGrade >= 60) return "C-";
        if (totalGrade >= 55) return "D+";
        if (totalGrade >= 50) return "D";
        return "F";
    }

    /**
     * Converts the numeric total grade to a GPA value.
     *
     * @return GPA value formatted as text
     */
    public String convertToGPA() {
        if (totalGrade >= 95) return "4.0";
        if (totalGrade >= 90) return "3.67";
        if (totalGrade >= 85) return "3.33";
        if (totalGrade >= 80) return "3.0";
        if (totalGrade >= 75) return "2.67";
        if (totalGrade >= 70) return "2.33";
        if (totalGrade >= 65) return "2.0";
        if (totalGrade >= 60) return "1.67";
        if (totalGrade >= 55) return "1.33";
        if (totalGrade >= 50) return "1.0";
        return "0.0";
    }

    public String getCourseName() { return courseName; }
    public void setCourseName(String n) { this.courseName = n; }
    public String getStudentId() { return studentId; }
    public Double getTotalGrade() { return totalGrade; }
    public Double getFirstAtt() { return firstAtt; }
    public void setFirstAtt(Double v) { this.firstAtt = v; recalc(); }
    public Double getSecondAtt() { return secondAtt; }
    public void setSecondAtt(Double v) { this.secondAtt = v; recalc(); }
    public Double getFinalGrade() { return finalGrade; }
    public void setFinalGrade(Double v) { this.finalGrade = v; recalc(); }

    private void recalc() { totalGrade = firstAtt + secondAtt + finalGrade; }

    @Override
    public String toString() {
        return courseName + ": " + totalGrade + " " + convertToLetter()
                + " (GPA " + convertToGPA() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Mark other)) return false;
        return Objects.equals(courseName, other.courseName)
                && Objects.equals(studentId, other.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseName, studentId);
    }
}
