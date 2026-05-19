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
 * Thrown when a supervisor is assigned whose h-index is below 3.
 */
public class LowHIndexException extends Exception {
    /**
     * Creates an exception for an invalid graduate supervisor assignment.
     *
     * @param supervisorName supervisor candidate name
     * @param hIndex calculated h-index of the supervisor candidate
     */
    public LowHIndexException(String supervisorName, int hIndex) {
        super("Cannot assign '" + supervisorName + "' as supervisor: h-index is "
                + hIndex + " (minimum required: 3).");
    }
}
