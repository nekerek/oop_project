package exception;

import controller.*;
import enumm.*;
import model.common.*;
import model.course.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * Thrown when a student tries to retake a course they have already failed 3 times.
 */
public class CourseFailLimitException extends Exception {
    /**
     * Creates an exception for a blocked course registration.
     *
     * @param courseName course that reached the fail limit
     */
    public CourseFailLimitException(String courseName) {
        super("Cannot register for '" + courseName + "': failed 3 times already.");
    }
}
