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
 * Thrown when a non-researcher tries to join a ResearchProject.
 */
public class NotAResearcherException extends Exception {
    /**
     * Creates an exception for an invalid research project participant.
     *
     * @param userName name of the user who attempted to join
     */
    public NotAResearcherException(String userName) {
        super("'" + userName + "' is not a Researcher and cannot join a ResearchProject.");
    }
}
