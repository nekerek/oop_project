package model.user;

import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.research.*;
import repository.*;
import service.*;

/**
 * Interface: any class that can generate a text report.
 */
public interface Reportable {
    String generateReport();
}
