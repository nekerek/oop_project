package enumm;

import controller.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/** Status lifecycle for tech support requests */
public enum RequestStatus {
    NEW, VIEWED, ACCEPTED, REJECTED, DONE
}
