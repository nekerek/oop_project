package model.research;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * Research publication model used by researchers, projects, and journals.
 *
 * <p>The natural ordering sorts papers by citation count in descending order.</p>
 */
public class ResearchPaper implements Serializable, Comparable<ResearchPaper> {
    private static final long serialVersionUID = 1L;

    private String title;
    private List<String> authors;
    private String journal;
    private int pages;
    private LocalDate datePublished;
    private String doi;
    private int citations;

    /**
     * Creates a research paper.
     *
     * @param title paper title
     * @param authors paper authors
     * @param journal publishing journal
     * @param pages article length in pages
     * @param datePublished publication date
     * @param doi digital object identifier
     */
    public ResearchPaper(String title, List<String> authors, String journal,
                         int pages, LocalDate datePublished, String doi) {
        this.title = title;
        this.authors = authors;
        this.journal = journal;
        this.pages = pages;
        this.datePublished = datePublished;
        this.doi = doi;
        this.citations = 0;
    }

    /**
     * Formats this paper as a citation.
     *
     * @param format citation format, either plain text or BibTeX
     * @return formatted citation string
     */
    public String getCitation(CitationFormat format) {
        if (format == CitationFormat.BIBTEX) {
            return "@article{" + doi.replace("/", "_") + ",\n"
                    + "  title   = {" + title + "},\n"
                    + "  author  = {" + String.join(" and ", authors) + "},\n"
                    + "  journal = {" + journal + "},\n"
                    + "  year    = {" + datePublished.getYear() + "},\n"
                    + "  pages   = {" + pages + "},\n"
                    + "  doi     = {" + doi + "}\n"
                    + "}";
        } else {
            return String.join(", ", authors)
                    + " (" + datePublished.getYear() + "). "
                    + title + ". " + journal + ". "
                    + pages + " pp. DOI: " + doi;
        }
    }

    public void addCitation() { this.citations++; }

    @Override
    public int compareTo(ResearchPaper other) {
        return Integer.compare(other.citations, this.citations); // descending
    }

    // Getters & setters
    public String getTitle() { return title; }
    public List<String> getAuthors() { return authors; }
    public String getJournal() { return journal; }
    public int getPages() { return pages; }
    public LocalDate getDatePublished() { return datePublished; }
    public String getDoi() { return doi; }
    public int getCitations() { return citations; }
    public void setCitations(int c) { this.citations = c; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper)) return false;
        return Objects.equals(doi, ((ResearchPaper) o).doi);
    }

    @Override
    public int hashCode() { return Objects.hash(doi); }

    @Override
    public String toString() {
        return "\"" + title + "\" (" + datePublished.getYear() + ")  "
                + citations + " citations, " + pages + " pp.";
    }
}
