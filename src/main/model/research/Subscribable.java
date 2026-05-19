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
 * Observer contract for users that can subscribe to university journals.
 */
public interface Subscribable {
    /**
     * Handles notification that a subscribed journal published a paper.
     *
     * @param journalName journal name
     * @param paperTitle published paper title
     * @param authorName author displayed in the announcement
     */
    void onNewPaperPublished(String journalName, String paperTitle, String authorName);
}
