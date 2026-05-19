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
 * Thrown when an operation references a user that does not exist.
 */
public class UserNotExists extends Exception {
    /**
     * Creates an exception with a user lookup failure message.
     *
     * @param message lookup failure message
     */
    public UserNotExists(String message){
        super(message);
    }
}
