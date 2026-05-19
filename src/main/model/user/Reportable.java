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
 * Contract for classes that can generate text reports.
 */
public interface Reportable {
    /**
     * Generates a human-readable report.
     *
     * @return report text
     */
    String generateReport();
}
