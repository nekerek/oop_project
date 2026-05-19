package model.research;

import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * Interface: Observer pattern  anyone who can subscribe to a UniversityJournal.
 */
public interface Subscribable {
    void onNewPaperPublished(String journalName, String paperTitle, String authorName);
}
