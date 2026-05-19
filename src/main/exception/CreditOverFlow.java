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
 * Thrown when a course registration violates credit or availability rules.
 */
public class CreditOverFlow extends Exception {
    /**
     * Creates an exception with a user-facing explanation.
     *
     * @param message validation failure message
     */
    public CreditOverFlow(String message){
        super(message);
    }
}
