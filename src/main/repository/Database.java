package repository;

import java.io.*;
import java.util.*;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.research.*;
import model.user.*;
import service.*;
import model.course.File;

/**
 * Central persistence and in-memory storage component for the university system.
 *
 * <p>The class exposes shared collections used by role services and serializes
 * them into the {@code data} directory. It also provides a singleton access
 * point for the storage layer, matching the project architecture requirement
 * for a single data source.</p>
 */
@SuppressWarnings("unchecked")
public class Database implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_DIR = "data";

    // ── Singleton ─────────────────────────────────────────────────────────
    private static Database instance;

    /**
     * Returns the single database instance.
     *
     * @return shared {@code Database} instance
     */
    public static Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }

    private Database() {}

    // ── Static data collections (accessible as Database.users etc.) ───────
    public static Vector<Course>  courses            = new Vector<>();
    public static Vector<User>    users              = new Vector<>();
    public static HashMap<String, Course>  studentRegistration = new HashMap<>();
    public static HashMap<String, Integer> teacherRatings      = new HashMap<>();
    public static Vector<Mark>    marks              = new Vector<>();
    public static HashMap<String, Book>  orders      = new HashMap<>();
    public static Vector<Message> messages           = new Vector<>();
    public static Vector<File>    files              = new Vector<>();
    public static HashMap<String, String> logFiles   = new HashMap<>();
    public static HashSet<Book>   books              = new HashSet<>();
    public static Vector<News>    news               = new Vector<>();
    public static Vector<Lesson>  lessons            = new Vector<>();
    public static Vector<Attendance> attendance       = new Vector<>();
    public static Vector<TechRequest>      techRequests        = new Vector<>();
    public static Vector<UniversityJournal> journals           = new Vector<>();
    public static Vector<StudentOrganization> organizations     = new Vector<>();
    public static Vector<ResearchProject>   researchProjects    = new Vector<>();

    // ── Save / Load ───────────────────────────────────────────────────────
    /**
     * Serializes all persistent collections to disk.
     *
     * <p>The method is called after menu operations that mutate application
     * state, such as registration approval, mark entry, user management,
     * attendance updates, journal subscription, and news creation.</p>
     */
    public static void save() {
        write("users.txt",       users);
        write("courses.txt",     courses);
        write("marks.txt",       marks);
        write("messages.txt",    messages);
        write("news.txt",        news);
        write("files.txt",       files);
        write("books.txt",       books);
        write("orders.txt",      orders);
        write("studentReg.txt",  studentRegistration);
        write("techRequests.txt",techRequests);
        write("journals.txt",    journals);
        write("attendance.txt",  attendance);
        write("logFiles.txt",    logFiles);
        System.out.println("[DB] Data saved.");
    }

    /**
     * Loads persistent collections from disk.
     *
     * <p>If a file is missing, the corresponding collection is initialized with
     * an empty default value. Corrupted serialized files are reset by
     * {@link #read(String, Object)}.</p>
     */
    public static void load() {
        users              = (Vector<User>)              read("users.txt",       new Vector<>());
        courses            = (Vector<Course>)            read("courses.txt",     new Vector<>());
        marks              = (Vector<Mark>)              read("marks.txt",       new Vector<>());
        messages           = (Vector<Message>)           read("messages.txt",    new Vector<>());
        news               = (Vector<News>)              read("news.txt",        new Vector<>());
        files              = (Vector<File>)              read("files.txt",       new Vector<>());
        books              = (HashSet<Book>)             read("books.txt",       new HashSet<>());
        orders             = (HashMap<String, Book>)     read("orders.txt",      new HashMap<>());
        studentRegistration= (HashMap<String, Course>)   read("studentReg.txt",  new HashMap<>());
        techRequests       = (Vector<TechRequest>)       read("techRequests.txt",new Vector<>());
        journals           = (Vector<UniversityJournal>) read("journals.txt",    new Vector<>());
        attendance         = (Vector<Attendance>)        read("attendance.txt",  new Vector<>());
        logFiles           = (HashMap<String, String>)   read("logFiles.txt",    new HashMap<>());
        System.out.println("[DB] Data loaded: " + users.size() + " users.");
    }

    /**
     * Writes a serializable object to the configured data directory.
     *
     * @param filename target file name inside {@code data}
     * @param obj object to serialize
     */
    private static void write(String filename, Object obj) {
        new java.io.File(DATA_DIR).mkdirs();
        java.io.File target = new java.io.File(DATA_DIR, filename);
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(target))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println("[DB] Save failed: " + filename);
        }
    }

    /**
     * Reads a serialized object from the configured data directory.
     *
     * @param filename source file name inside {@code data}
     * @param defaultVal fallback value returned when the file is missing or invalid
     * @return deserialized object or {@code defaultVal}
     */
    private static Object read(String filename, Object defaultVal) {
        java.io.File f = new java.io.File(DATA_DIR, filename);
        if (!f.exists()) return defaultVal;
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(f))) {
            return ois.readObject();
        } catch (Exception e) {
            System.err.println("[DB] Load failed (corrupted): " + filename + "  resetting.");
            f.delete(); // remove corrupted file so next run reseeds cleanly
            return defaultVal;
        }
    }

    // ── Getters (kept for compatibility) ──────────────────────────────────
    public static Vector<Course>  getCourses()  { return courses; }
    public static Vector<User>    getUsers()    { return users; }
    public static Vector<Mark>    getMarks()    { return marks; }
    public static Vector<News>    getNews()     { return news; }
    public static Vector<Lesson>  getLessons()  { return lessons; }
    public static Vector<Attendance> getAttendance() { return attendance; }
    public static Vector<TechRequest> getTechRequests() { return techRequests; }
    public static Vector<UniversityJournal> getJournals() { return journals; }
}
