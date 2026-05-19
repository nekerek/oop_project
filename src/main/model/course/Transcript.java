package model.course;

import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * Simple transcript entry containing a course and GPA value.
 */
public class Transcript {
    
    private String courseName;
    private double GPA;


    public Transcript(String courseName, double GPA) {
        this.courseName = courseName;
        this.GPA = GPA;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getGPA() {
        return this.GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }    
    

    //                          Operations                                  
    
    /**
     * Placeholder for transcript GPA recalculation logic.
     */
    public void calculateGPA() {
        //TODO
    }
    
}
