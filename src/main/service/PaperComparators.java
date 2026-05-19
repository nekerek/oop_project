package service;

import java.util.Comparator;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.research.*;
import model.user.*;
import repository.*;

/**
 * Strategy collection for sorting research papers.
 *
 * <p>Each comparator represents a different paper ordering strategy used by
 * researcher menus and global paper printing.</p>
 */
public class PaperComparators {

    /** Newest first */
    public static final Comparator<ResearchPaper> BY_DATE =
            Comparator.comparing(ResearchPaper::getDatePublished).reversed();

    /** Most cited first */
    public static final Comparator<ResearchPaper> BY_CITATIONS =
            Comparator.comparingInt(ResearchPaper::getCitations).reversed();

    /** Longest article first */
    public static final Comparator<ResearchPaper> BY_LENGTH =
            Comparator.comparingInt(ResearchPaper::getPages).reversed();
}
