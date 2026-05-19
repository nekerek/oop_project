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
 * Thrown when a supervisor is assigned whose h-index < 3.
 */
public class LowHIndexException extends Exception {
    public LowHIndexException(String supervisorName, int hIndex) {
        super("Cannot assign '" + supervisorName + "' as supervisor: h-index is "
                + hIndex + " (minimum required: 3).");
    }
}
