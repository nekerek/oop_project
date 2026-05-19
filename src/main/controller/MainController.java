package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * Console entry point and role-based menu controller.
 *
 * <p>The controller handles startup, language selection, authentication,
 * demo data seeding, and dispatching authenticated users to their role
 * menus. Menu labels are resolved through {@link I18n} for KZ, EN, and RU
 * language support.</p>
 *
 * <p>Architectural patterns used by the application include Singleton
 * ({@link Database}), Factory ({@link UserFactory}), Observer
 * ({@link UniversityJournal} and {@link Subscribable}), and Strategy
 * ({@link PaperComparators}).</p>
 */
public class MainController {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    // ── helpers ───────────────────────────────────────────────────────────
    static String t(String key, Language lang) { return I18n.t(key, lang); }
    static String prompt(String key, Language lang) {
        System.out.print(t(key, lang));
        try { return reader.readLine().trim(); } catch (IOException e) { return ""; }
    }
    static int choose(Language lang) {
        System.out.print(t("prompt.choose", lang));
        try { return Integer.parseInt(reader.readLine().trim()); }
        catch (IOException | NumberFormatException e) { return -1; }
    }

    // ─────────────────────────────────────────────────────────────────────
    /**
     * Starts the console application and authenticates a user.
     *
     * @param args command-line arguments, not used
     * @throws IOException if console input cannot be read
     * @throws CreditOverFlow if seeded or menu-driven registration exceeds credit limits
     * @throws CourseFailLimitException if a student exceeds the course fail limit
     * @throws LowHIndexException if seeded supervisor assignment violates h-index rules
     */
    public static void main(String[] args) throws IOException, CreditOverFlow,
            CourseFailLimitException, LowHIndexException {
        System.out.println("========================================");
        System.out.println("Research-Oriented University System");
        System.out.println("========================================\n");

        Language selectedLanguage = chooseInitialLanguage();

        Database.load();
        if (Database.users.isEmpty()) seedDemoData();
        printLogins();

        System.out.println(t("msg.quit", selectedLanguage));
        System.out.print(t("prompt.login", selectedLanguage));
        String input = reader.readLine().trim();
        if (input.equals("q")) System.exit(0);

        for (User u : Database.users) {
            if (u.getLogin().equals(input)) {
                Language lang = selectedLanguage;
                u.setLanguage(lang);
                System.out.print(t("prompt.password", lang));
                String pw = reader.readLine().trim();
                if (!u.getPassword().equals(pw)) {
                    System.out.println(t("msg.wrong_pass", lang)); return;
                }
                System.out.println(t("msg.welcome", lang) + u.getName()
                        + "! [" + u.getRole() + "]");
                if      (u instanceof GraduateStudent)       gradMenu((GraduateStudent) u, lang);
                else if (u instanceof Student)                studentMenu((Student) u, lang);
                else if (u instanceof Teacher)                teacherMenu((Teacher) u, lang);
                else if (u instanceof Manager)                managerMenu((Manager) u, lang);
                else if (u instanceof Admin)                  adminMenu((Admin) u, lang);
                else if (u instanceof TechSupportSpecialist)  techMenu((TechSupportSpecialist) u, lang);
                else if (u instanceof Librarian)              libMenu((Librarian) u, lang);
                else if (u instanceof Researcher)             researcherMenu((Researcher) u, lang);
                return;
            }
        }
        System.out.println(t("msg.not_found", Language.EN));
    }

    // ─────────────────────────────────────────────────────────────────────

    static Language chooseInitialLanguage() {
        System.out.println("Choose language / Til tandau / Vyberite yazyk");
        System.out.println("1. English");
        System.out.println("2. Kazakh");
        System.out.println("3. Russian");
        System.out.print("Choose: ");
        try {
            String choice = reader.readLine().trim();
            if (choice.equals("2") || choice.equalsIgnoreCase("KZ")) return Language.KZ;
            if (choice.equals("3") || choice.equalsIgnoreCase("RU")) return Language.RU;
        } catch (IOException ignored) {
        }
        return Language.EN;
    }

    // SEED
    // ─────────────────────────────────────────────────────────────────────
    /**
     * Creates initial demo users, courses, marks, journals, and research data.
     *
     * @throws LowHIndexException if the seeded graduate supervisor does not meet
     *                            the minimum h-index rule
     */
    static void seedDemoData() throws LowHIndexException {
        System.out.println("[SEED] Creating demo data...");

        Student s1 = (Student) UserFactory.createUser(UserFactory.UserType.STUDENT,
                "Khydyr","Tabyngalyzyev","29/03/2007","87705 205 97 77","Khydyr@gmail.com","123456",
                "24B031839", 2, Faculty.ISE, Degree.BACHELOR);
        Student s2 = (Student) UserFactory.createUser(UserFactory.UserType.STUDENT,
                "Nursultan","Kenzhegaliev","30/05/2007","8707 442 16 16","Nursultan@gmail.com","12345",
                "24B030991", 2, Faculty.FIT, Degree.BACHELOR);
        GraduateStudent grad = (GraduateStudent) UserFactory.createUser(
                UserFactory.UserType.GRADUATE_STUDENT,
                "Yerbatyr","Nursultan","22/06/2007","8707 232 44 55","Erbatyr@gmail.com","phd123",
                "22B031663", 4, Faculty.FIT, Degree.PHD);
        Teacher t1 = (Teacher) UserFactory.createUser(UserFactory.UserType.TEACHER,
                "Pakizar","Shamoi","05/05/1990","87771234567","pakizar@gmail.com","12345",
                Status.PROFESSOR, "11 years");
        Teacher t2 = (Teacher) UserFactory.createUser(UserFactory.UserType.TEACHER,
                "Assylzhan","Izbassar","01/01/2000","87012345678","assylzhan@gmail.com","12001",
                Status.LECTOR, "5 years");
        Manager m1 = (Manager) UserFactory.createUser(UserFactory.UserType.MANAGER,
                "Manager","Managerov","01/01/1990","87071111111","manager@gmail.com","12222",
                Managers.DEPARTMENTS);
        Admin a1 = (Admin) UserFactory.createUser(UserFactory.UserType.ADMIN,
                "Admin","Adminov","22/02/1972","87072222222","admin@gmail.com","11111");
        TechSupportSpecialist tech = (TechSupportSpecialist) UserFactory.createUser(
                UserFactory.UserType.TECH_SUPPORT,
                "Tech","Support","01/01/1990","87011234567","tech@gmail.com","tech123");
        Librarian lib = (Librarian) UserFactory.createUser(UserFactory.UserType.LIBRARIAN,
                "Librarian","Librarianov","21/12/1980","87771233455","lib@gmail.com","lib123");
        Researcher res = (Researcher) UserFactory.createUser(UserFactory.UserType.RESEARCHER,
                "Researcher","Researcherov","05/05/1990","87051234567","researcher@gmail.com","res123");

        // Default languages
        s1.setLanguage(Language.KZ);
        s2.setLanguage(Language.RU);
        t1.setLanguage(Language.EN);

        for (User u : new User[]{s1,s2,grad,t1,t2,m1,a1,tech,lib,res})
            Database.users.add(u);

        Course c1 = new Course("Object-Oriented Programming","OOP concepts",3,"CSCI2106",
                CourseType.MAJOR,"SITE",2);
        Course c2 = new Course("Web Development","HTML and CSS and JavaScript",4,"INFT2205",
                CourseType.MAJOR,"SITE",3);
        Course c3 = new Course("Intro to Oil & Gas","Energy basics",3,"OG101",
                CourseType.FREE_ELECTIVE,"Oil and Gas",1);
        for (Course c : new Course[]{c1,c2,c3}) Database.courses.add(c);
        c1.getTeacher().add(t1.getLogin()); c1.getTeacher().add(t2.getLogin());

        Database.lessons.add(new Lesson(LessonType.LECTURE, new java.util.Date(),
                "Room 301","CSCI2106", t1.getLogin()));
        Database.lessons.add(new Lesson(LessonType.PRACTICE, new java.util.Date(),
                "Lab 1","CSCI2106", t2.getLogin()));
        Database.attendance.add(new Attendance("CSCI2106", s1.getId(), true, "Lecture 1"));
        Database.attendance.add(new Attendance("INFT2205", s2.getId(), false, "Absent"));

        Database.marks.add(new Mark("Object-Oriented Programming","24B031839",24.0,24.6,40.0));
        Database.marks.add(new Mark("Web Development","24B030991",30.0,29.0,37.0));
        Database.messages.add(new Message("Zharkyn","p.shamoi@kbtu.kz","Greetings","Welcome!"));

        Database.books.add(new Book("Thomas Calculus","B1","Thomas"));
        Database.books.add(new Book("Clean Code","B2","R. Martin"));
        Database.orders.put("24B031839", new Book("Thomas Calculus","B1","Thomas"));

        Database.news.add(new News("N1","General","Registration open","July 10th"));
        Database.news.add(new News("N2","Research","KBTU AI Lab grant","$500K approved"));
        Collections.sort(Database.news);

        Database.files.add(new File("OOP_Lecture1","CSCI2106","Intro slides"));
        Database.files.add(new File("WEB_Lab1","INFT2205","First lab"));
        Database.techRequests.add(new TechRequest("R001",s1.getLogin(),"Projector broken Room 301"));
        Database.techRequests.add(new TechRequest("R002",t1.getLogin(),"Printer 3rd floor jammed"));

        ResearchPaper p1 = new ResearchPaper("LMS Logs and Student Performance",
                Arrays.asList("Pakizar Shamoi","Alua Akhmetzhanova"),
                "Journal of Educational Technology",15,LocalDate.of(2022,6,10),"10.1000/jet.2022");
        p1.setCitations(12);
        ResearchPaper p2 = new ResearchPaper("Influence of Retaking a Course",
                Arrays.asList("Pakizar Shamoi"),
                "Journal of Educational Data Mining",22,LocalDate.of(2021,3,5),"10.1000/jedm.2021");
        p2.setCitations(8);
        ResearchPaper p3 = new ResearchPaper("Deep Learning for Grade Prediction",
                Arrays.asList("Pakizar Shamoi"),
                "IEEE Trans. on Education",18,LocalDate.of(2023,1,20),"10.1109/te.2023");
        p3.setCitations(5);
        t1.getResearchMixin().addPaper(p1);
        t1.getResearchMixin().addPaper(p2);
        t1.getResearchMixin().addPaper(p3);

        System.out.println("[SEED] Prof. Pakizar h-index = " + t1.getResearchMixin().calculateHIndex());
        grad.setSupervisor(t1);
        ResearchPaper diploma = new ResearchPaper("Adaptive Learning with RL",
                Arrays.asList("Alua Akhmetzhanova","Pakizar Shamoi"),
                "KBTU Research Bulletin",30,LocalDate.of(2024,4,1),"10.9999/kbtu.2024");
        grad.addDiplomaPaper(diploma);

        UniversityJournal journal = new UniversityJournal("KBTU Research Bulletin");
        journal.subscribe(s1); journal.subscribe(s2); journal.subscribe(grad);
        Database.journals.add(journal);
        journal.publishPaper(p1, t1.getName()+" "+t1.getSurname());

        ResearchProject proj = new ResearchProject("AI in Education");
        try { proj.addParticipant(t1); proj.addParticipant(grad); proj.addParticipant(s1); }
        catch (NotAResearcherException e) { System.out.println("[SEED] " + e.getMessage()); }
        s1.becomeResearcher();
        try { proj.addParticipant(s1); } catch (NotAResearcherException ignored) {}
        Database.researchProjects.add(proj);

        StudentOrganization club = new StudentOrganization("AI Research Club");
        club.setHead(s1.getId()); club.addMember(s2.getId());
        Database.organizations.add(club);
        s1.joinOrganization("AI Research Club", true);
        s2.joinOrganization("AI Research Club", false);

        Database.save();
        System.out.println("[SEED] Done.\n");
    }

    static void printLogins() {
        System.out.println("------------------------------------------------------------");
        System.out.println("DEMO LOGINS");
        System.out.println("------------------------------------------------------------");
        for (User u : Database.users)
            System.out.printf("%-22s %-28s %-7s%n",
                    u.getRole(), u.getLogin(), u.getPassword());
        System.out.println("------------------------------------------------------------\n");
    }

    /**
     * Allows any authenticated user to subscribe to an available university journal.
     *
     * @param user user subscribing to the journal
     */
    static void subscribeToJournal(User user) {
        if (Database.journals.isEmpty()) {
            System.out.println("No journals.");
            return;
        }
        for (int i = 0; i < Database.journals.size(); i++) {
            UniversityJournal j = Database.journals.get(i);
            System.out.println((i + 1) + ") " + j);
        }
        System.out.print("Journal number: ");
        try {
            int idx = Integer.parseInt(reader.readLine().trim()) - 1;
            if (idx < 0 || idx >= Database.journals.size()) {
                System.out.println("Invalid journal.");
                return;
            }
            Database.journals.get(idx).subscribe(user);
            Database.save();
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid journal.");
        }
    }

    /**
     * Collects research papers from every researcher-capable user in the system.
     *
     * @return combined list of research papers owned by researchers, teachers,
     *         graduate students, and researcher-enabled bachelor students
     */
    static List<ResearchPaper> collectAllResearchPapers() {
        List<ResearchPaper> papers = new ArrayList<>();
        for (User u : Database.users) {
            if (u instanceof Researcher) {
                papers.addAll(((Researcher) u).getResearchMixin().getPapers());
            } else if (u instanceof GraduateStudent) {
                papers.addAll(((GraduateStudent) u).getResearchMixin().getPapers());
            } else if (u instanceof Teacher && ((Teacher) u).isResearcher()) {
                papers.addAll(((Teacher) u).getResearchMixin().getPapers());
            } else if (u instanceof Student && ((Student) u).isResearcher()) {
                papers.addAll(((Student) u).getResearchMixin().getPapers());
            }
        }
        return papers;
    }

    /**
     * Prints all university research papers using the requested ordering strategy.
     *
     * @param comparator strategy used to sort papers before printing
     */
    static void printAllResearchPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> papers = collectAllResearchPapers();
        papers.sort(comparator);
        if (papers.isEmpty()) {
            System.out.println("No research papers.");
            return;
        }
        for (ResearchPaper p : papers) System.out.println(p);
    }

    /**
     * Reads a paper sorting option from the console.
     *
     * @param lang language used for the sort prompt
     * @return comparator matching the selected strategy
     * @throws IOException if console input cannot be read
     */
    static Comparator<ResearchPaper> choosePaperComparator(Language lang) throws IOException {
        System.out.println(I18n.t("msg.sort_choose", lang));
        int sc = Integer.parseInt(reader.readLine().trim());
        if (sc == 1) return PaperComparators.BY_CITATIONS;
        if (sc == 2) return PaperComparators.BY_DATE;
        return PaperComparators.BY_LENGTH;
    }

    // ─────────────────────────────────────────────────────────────────────
    // STUDENT MENU
    // ─────────────────────────────────────────────────────────────────────
    static void studentMenu(Student st, Language lang) throws IOException,
            CreditOverFlow, CourseFailLimitException {
        while (true) {
            System.out.println("\n" + t("menu.student.title", lang));
            System.out.printf(" [1]  %-22s  [2]  %s%n", t("menu.student.1",lang), t("menu.student.2",lang));
            System.out.printf(" [3]  %-22s  [4]  %s%n", t("menu.student.3",lang), t("menu.student.4",lang));
            System.out.printf(" [5]  %-22s  [6]  %s%n", t("menu.student.5",lang), t("menu.student.6",lang));
            System.out.printf(" [7]  %-22s  [8]  %s%n", t("menu.student.7",lang), t("menu.student.8",lang));
            System.out.printf(" [9]  %-22s  [10] %s%n", t("menu.student.9",lang), t("menu.student.10",lang));
            System.out.printf(" [11] %-22s  [12] %s%n", t("menu.student.11",lang), t("menu.student.12",lang));
            System.out.printf(" [13] %-22s  [14] %s%n", t("menu.student.13",lang), t("menu.student.14",lang));
            System.out.printf(" [15] %-22s  [16] %s%n", t("menu.student.15",lang), t("menu.student.16",lang));
            System.out.printf(" [0]  %-22s  [99] %s%n", t("menu.student.0",lang), t("menu.student.99",lang));
            int ch = choose(lang);
            switch (ch) {
                case 1:  System.out.println(st.getAllInfo()); break;
                case 2:  System.out.println(st.viewCourses()); break;
                case 3:  System.out.println(st.viewAvailableCourses()); break;
                case 4:
                    String cid4 = prompt("prompt.course_id", lang);
                    System.out.println(st.viewCourseFiles(cid4)); break;
                case 5:
                    String tn5 = prompt("prompt.teacher_name", lang);
                    System.out.println(st.viewTeacherInfo(tn5)); break;
                case 6:  System.out.println(st.viewMarks()); break;
                case 7:  System.out.println(st.viewTranscript()); break;
                case 8:
                    String tn8 = prompt("prompt.teacher_name", lang);
                    String rt8 = prompt("prompt.rating", lang);
                    st.rateTeacher(tn8, Integer.parseInt(rt8));
                    Database.save(); break;
                case 9:
                    String bid9 = prompt("prompt.book_id", lang);
                    st.orderBook(bid9); Database.save(); break;
                case 10:
                    String cid10 = prompt("prompt.course_id", lang);
                    st.registerToCourse(cid10); Database.save(); break;
                case 11:
                    String cid11 = prompt("prompt.course_id", lang);
                    st.dropCourse(cid11); Database.save(); break;
                case 12: System.out.println(st.viewNewsTab()); break;
                case 13:
                    System.out.println(st.viewBooks());
                    break;
                case 14:
                    if (st.getOrganizationName() == null) {
                        System.out.println(t("msg.org_none", lang));
                    } else {
                        System.out.println(st.getOrganizationName()
                                + (st.isOrgHead()
                                    ? t("msg.org_head", lang)
                                    : t("msg.org_member", lang)));
                    }
                    break;
                case 15: System.out.println(st.viewAttendance()); break;
                case 16: subscribeToJournal(st); break;
                case 0:
                    String op0 = prompt("prompt.old_pass", lang);
                    String np0 = prompt("prompt.new_pass", lang);
                    System.out.println(st.changePassword(op0, np0)
                            ? t("msg.pass_changed", lang) : t("msg.pass_wrong", lang));
                    Database.save(); break;
                case 99: System.exit(0);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // GRADUATE STUDENT MENU
    // ─────────────────────────────────────────────────────────────────────
    static void gradMenu(GraduateStudent gs, Language lang) throws IOException,
            CreditOverFlow, CourseFailLimitException {
        while (true) {
            System.out.println("\n" + t("menu.grad.title", lang));
            System.out.printf(" [1] %-24s  [2] %s%n", t("menu.grad.1",lang), t("menu.grad.2",lang));
            System.out.printf(" [3] %-24s  [4] %s%n", t("menu.grad.3",lang), t("menu.grad.4",lang));
            System.out.printf(" [5] %-24s  [6] %s%n", t("menu.grad.5",lang), t("menu.grad.6",lang));
            System.out.printf(" [7] %-24s  [8] %s%n", t("menu.grad.7",lang), t("menu.grad.8",lang));
            System.out.printf(" [9] %-24s  [10] %s%n", t("menu.grad.9",lang), t("menu.grad.10",lang));
            System.out.printf(" [0] %-24s  [99] %s%n",
                    t("menu.grad.0",lang), t("menu.grad.99",lang));
            int ch = choose(lang);
            switch (ch) {
                case 1: System.out.println(gs.getAllInfo()); break;
                case 2: System.out.println(gs.viewTranscript()); break;
                case 3:
                    if (gs.getSupervisor() != null)
                        System.out.println(gs.getSupervisor().getAllInfo());
                    else System.out.println(t("msg.no_supervisor", lang)); break;
                case 4: gs.getDiplomaPapers().forEach(System.out::println); break;
                case 5: System.out.println("h-index: " + gs.calculateHIndex()); break;
                case 6:
                    System.out.println(t("msg.sort_cit", lang));
                    gs.printPapers(PaperComparators.BY_CITATIONS); break;
                case 7: System.out.println(gs.viewNewsTab()); break;
                case 8:
                    System.out.print("Title: "); String title = reader.readLine().trim();
                    System.out.print("Journal: "); String journal = reader.readLine().trim();
                    System.out.print("Pages: "); int pages = Integer.parseInt(reader.readLine().trim());
                    System.out.print("DOI: "); String doi = reader.readLine().trim();
                    ResearchPaper diplomaPaper = new ResearchPaper(title,
                            Arrays.asList(gs.getName() + " " + gs.getSurname()),
                            journal, pages, LocalDate.now(), doi);
                    gs.addDiplomaPaper(diplomaPaper);
                    Database.save();
                    System.out.println(I18n.t("msg.saved", lang));
                    break;
                case 9: System.out.println(gs.viewAttendance()); break;
                case 10: subscribeToJournal(gs); break;
                case 0:
                    String op = prompt("prompt.old_pass", lang);
                    String np = prompt("prompt.new_pass", lang);
                    System.out.println(gs.changePassword(op, np)
                            ? t("msg.pass_changed", lang) : t("msg.pass_wrong", lang));
                    Database.save(); break;
                case 99: System.exit(0);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // TEACHER MENU
    // ─────────────────────────────────────────────────────────────────────
    static void teacherMenu(Teacher t, Language lang) throws IOException {
        while (true) {
            System.out.println("\n" + I18n.t("menu.teacher.title", lang));
            System.out.printf(" [1]  %-22s  [2]  %s%n", I18n.t("menu.teacher.1",lang), I18n.t("menu.teacher.2",lang));
            System.out.printf(" [3]  %-22s  [4]  %s%n", I18n.t("menu.teacher.3",lang), I18n.t("menu.teacher.4",lang));
            System.out.printf(" [5]  %-22s  [6]  %s%n", I18n.t("menu.teacher.5",lang), I18n.t("menu.teacher.6",lang));
            System.out.printf(" [7]  %-22s  [8]  %s%n", I18n.t("menu.teacher.7",lang), I18n.t("menu.teacher.8",lang));
            System.out.printf(" [9]  %-22s  [10] %s%n", I18n.t("menu.teacher.9",lang), I18n.t("menu.teacher.10",lang));
            System.out.printf(" [11] %-22s  [12] %s%n", I18n.t("menu.teacher.11",lang), I18n.t("menu.teacher.12",lang));
            System.out.printf(" [13] %-22s  [14] %s%n", I18n.t("menu.teacher.13",lang), I18n.t("menu.teacher.14",lang));
            System.out.printf(" [15] %-22s  [0]  %s  [99] %s%n",
                    I18n.t("menu.teacher.15",lang), I18n.t("menu.teacher.0",lang), I18n.t("menu.teacher.99",lang));
            System.out.printf(" [16] %s%n", I18n.t("menu.teacher.16",lang));
            System.out.printf(" [17] %-22s  [18] %s%n", I18n.t("menu.teacher.17",lang), I18n.t("menu.teacher.18",lang));
            System.out.printf(" [19] %-22s  [20] %s%n", I18n.t("menu.teacher.19",lang), I18n.t("menu.teacher.20",lang));
            int ch = choose(lang);
            switch (ch) {
                case 1: System.out.println(t.getAllInfo()); break;
                case 2: System.out.println(t.viewCourses()); break;
                case 3:
                    System.out.print("File name: ");   String fn = reader.readLine().trim();
                    System.out.print("Course ID: ");   String ci = reader.readLine().trim();
                    System.out.print("Description: "); String dc = reader.readLine().trim();
                    t.addCourseFile(fn,ci,dc); Database.save();
                    System.out.println(I18n.t("msg.saved",lang)); break;
                case 4:
                    System.out.print("File name: "); String fn2 = reader.readLine().trim();
                    System.out.print("Course ID: "); String ci2 = reader.readLine().trim();
                    t.deleteCourseFile(fn2,ci2); Database.save();
                    System.out.println(I18n.t("msg.saved",lang)); break;
                case 5: System.out.println(t.viewStudents()); break;
                case 6:
                    String sn6 = prompt("prompt.student_name", lang);
                    System.out.println(t.viewStudentInfo(sn6)); break;
                case 7:
                    System.out.print("Course name: ");  String cn = reader.readLine().trim();
                    System.out.print("Student ID: ");   String sid = reader.readLine().trim();
                    System.out.print("1st att: ");      double a1 = Double.parseDouble(reader.readLine().trim());
                    System.out.print("2nd att: ");      double a2 = Double.parseDouble(reader.readLine().trim());
                    System.out.print("Final: ");        double fg = Double.parseDouble(reader.readLine().trim());
                    t.putMark(cn,sid,a1,a2,fg); Database.save();
                    System.out.println(I18n.t("msg.saved",lang)); break;
                case 8:
                    System.out.print("Course name: "); String cm8 = reader.readLine().trim();
                    System.out.println(t.viewMarks(cm8)); break;
                case 9:
                    System.out.print("From: ");      String mf = reader.readLine().trim();
                    System.out.print("To (login): ");String mt = reader.readLine().trim();
                    System.out.print("Title: ");     String ttl= reader.readLine().trim();
                    System.out.print("Text: ");      String tx = reader.readLine().trim();
                    t.sendMessage(mf,mt,ttl,tx); Database.save();
                    System.out.println(I18n.t("msg.sent",lang)); break;
                case 10: System.out.println(t.getMessages()); break;
                case 11: System.out.printf("Rating: %.2f%n", t.viewRating()); break;
                case 12:
                    String sid12 = prompt("prompt.student_name", lang);
                    Student cmpSt = null;
                    for (User u : Database.users)
                        if (u instanceof Student && ((Student)u).getId().equals(sid12)) cmpSt=(Student)u;
                    if (cmpSt == null) for (User u : Database.users)
                        if (u instanceof Student && u.getName().equalsIgnoreCase(sid12)) cmpSt=(Student)u;
                    if (cmpSt == null) { System.out.println("Not found."); break; }
                    Manager dean = null;
                    for (User u : Database.users) if (u instanceof Manager) { dean=(Manager)u; break; }
                    if (dean == null) { System.out.println("No manager."); break; }
                    String reason = prompt("prompt.reason", lang);
                    String urgStr = prompt("prompt.urgency", lang);
                    ComplaintUrgency urg = ComplaintUrgency.valueOf(urgStr.toUpperCase());
                    t.sendComplaint(cmpSt, reason, urg, dean); Database.save(); break;
                case 13: System.out.println(t.viewNewsTab()); break;
                case 14:
                    if (t.isResearcher()) System.out.println("h-index: "+t.getResearchMixin().calculateHIndex());
                    else System.out.println(I18n.t("msg.not_researcher",lang)); break;
                case 15:
                    if (!t.isResearcher()) { System.out.println(I18n.t("msg.not_researcher",lang)); break; }
                    System.out.println(I18n.t("msg.sort_choose",lang));
                    int sc = Integer.parseInt(reader.readLine().trim());
                    t.getResearchMixin().printPapers(
                            sc==1 ? PaperComparators.BY_CITATIONS
                          : sc==2 ? PaperComparators.BY_DATE
                          : PaperComparators.BY_LENGTH); break;
                case 16:
                    System.out.print("Student ID: "); String sid16 = reader.readLine().trim();
                    String cid16 = prompt("prompt.course_id", lang);
                    System.out.println(t.removeStudentFromCourse(sid16, cid16));
                    Database.save(); break;
                case 17:
                    String cid17 = prompt("prompt.course_id", lang);
                    System.out.print("Student ID: "); String sid17 = reader.readLine().trim();
                    System.out.print("Present (true/false): "); boolean present = Boolean.parseBoolean(reader.readLine().trim());
                    System.out.print("Note: "); String note = reader.readLine().trim();
                    t.markAttendance(cid17, sid17, present, note);
                    Database.save();
                    System.out.println(I18n.t("msg.saved", lang)); break;
                case 18:
                    String cid18 = prompt("prompt.course_id", lang);
                    System.out.println(t.viewAttendance(cid18)); break;
                case 19: subscribeToJournal(t); break;
                case 20: printAllResearchPapers(choosePaperComparator(lang)); break;
                
                case 0:
                    String op = prompt("prompt.old_pass",lang);
                    String np = prompt("prompt.new_pass",lang);
                    System.out.println(t.changePassword(op,np)
                            ? I18n.t("msg.pass_changed",lang) : I18n.t("msg.pass_wrong",lang));
                    Database.save(); break;
                case 99: System.exit(0);
            }
        }
    }

 // ─────────────────────────────────────────────────────────────────────
// MANAGER MENU
// ─────────────────────────────────────────────────────────────────────
static void managerMenu(Manager m, Language lang) throws IOException {
    while (true) {
        System.out.println("\n" + I18n.t("menu.manager.title",lang));
        for (int i=1; i<=22; i+=2) {
            String left  = I18n.t("menu.manager."+i,     lang);
            String right = (i+1<=22) ? I18n.t("menu.manager."+(i+1),lang) : "";
            System.out.printf(" [%-2d] %-26s  [%-2d] %s%n", i, left, i+1, right);
        }
        // Теперь не нужно отдельно печатать строку для 22
        System.out.printf(" [0]  %-26s  [99] %s%n",
                I18n.t("menu.manager.0", lang), 
                I18n.t("menu.manager.99", lang));
        int ch = choose(lang);
        switch (ch) {
            case 1:
                System.out.print("Name: ");    String cn = reader.readLine().trim();
                System.out.print("Credits: "); int cr = Integer.parseInt(reader.readLine().trim());
                System.out.print("ID: ");      String cid= reader.readLine().trim();
                m.createCourse(cn,cr,cid); Database.save(); break;
            case 2:  System.out.println(m.infoStudents()); break;
            case 3:
                String tn3 = prompt("prompt.teacher_name",lang);
                System.out.println(m.infoTeachers(tn3)); break;
            case 4:  System.out.println(m.viewRequests()); break;
            case 5:
                System.out.print("Student ID: "); String sid5=reader.readLine().trim();
                String cid5 = prompt("prompt.course_id",lang);
                System.out.print("ACCEPT/REJECT: "); String app=reader.readLine().trim();
                System.out.println(m.approveRegistration(sid5,cid5,app)); Database.save(); break;
            case 6:
                String cid6 = prompt("prompt.course_id",lang);
                String tn6  = prompt("prompt.teacher_name",lang);
                m.assignCourseToTeachers(cid6,tn6); Database.save(); break;
            case 7:
                System.out.print("ID: ");    String nid=reader.readLine().trim();
                System.out.print("Topic: "); String ntopic=reader.readLine().trim();
                System.out.print("Title: "); String ntitle=reader.readLine().trim();
                System.out.print("Text: ");  String ntext=reader.readLine().trim();
                m.addNews(nid,ntopic,ntitle,ntext); Database.save(); break;
            case 8:
                System.out.print("News ID: "); m.removeNews(reader.readLine().trim()); Database.save(); break;
            case 9:
                System.out.print("Old ID: ");String oid=reader.readLine().trim();
                System.out.print("New ID: ");String nid2=reader.readLine().trim();
                System.out.print("Title: "); String nt=reader.readLine().trim();
                System.out.print("Text: ");  String nx=reader.readLine().trim();
                m.updateNews(oid,nid2,nt,nx); Database.save(); break;
            case 10: System.out.printf("Max: %.1f%n", m.getMaxScore()); break;
            case 11: System.out.printf("Min: %.1f%n", m.getMinScore()); break;
            case 12: System.out.printf("Avg: %.1f%n", m.getAvgScore()); break;
            case 13: System.out.println("Fails: "+m.retakeCount()); break;
            case 14: System.out.println(m.orderStudentsByGPA()); break;
            case 15: System.out.println(m.orderStudentsAlphabetically()); break;
            case 16: System.out.println(m.orderTeachersAlphabetically()); break;
            case 17: System.out.println(m.orderTeachersStatus()); break;
            case 18: System.out.println(m.viewNewsTab()); break;
            case 19:
                System.out.print("From: ");String mf=reader.readLine().trim();
                System.out.print("To: ");  String mt=reader.readLine().trim();
                System.out.print("Title: ");String ttl=reader.readLine().trim();
                System.out.print("Text: "); String tx=reader.readLine().trim();
                m.sendMessage(mf,mt,ttl,tx); Database.save();
                System.out.println(I18n.t("msg.sent",lang)); break;
            case 20: System.out.println(m.getMessages()); break;
            case 21: System.out.println(m.generateReport()); break;
            case 22:
                String cid22 = prompt("prompt.course_id", lang);
                System.out.println(m.deleteCourse(cid22));
                Database.save(); break;

            case 0:
                String op=prompt("prompt.old_pass",lang); String np=prompt("prompt.new_pass",lang);
                System.out.println(m.changePassword(op,np)
                        ? I18n.t("msg.pass_changed",lang) : I18n.t("msg.pass_wrong",lang));
                Database.save(); break;
            case 99: System.exit(0);
            default: break;
        }
    }
}


    // ─────────────────────────────────────────────────────────────────────
    // ADMIN MENU
    // ─────────────────────────────────────────────────────────────────────
    static void adminMenu(Admin a, Language lang) throws IOException {
        while (true) {
            System.out.println("\n" + I18n.t("menu.admin.title",lang));
            System.out.printf(" [1]  %-22s  [2]  %s%n",I18n.t("menu.admin.1",lang),I18n.t("menu.admin.2",lang));
            System.out.printf(" [3]  %-22s  [4]  %s%n",I18n.t("menu.admin.3",lang),I18n.t("menu.admin.4",lang));
            System.out.printf(" [5]  %-22s  [6]  %s%n",I18n.t("menu.admin.5",lang),I18n.t("menu.admin.6",lang));
            System.out.printf(" [7]  %-22s  [8]  %s%n",I18n.t("menu.admin.7",lang),I18n.t("menu.admin.8",lang));
            System.out.printf(" [9]  %-22s  [10] %s%n",I18n.t("menu.admin.9",lang),I18n.t("menu.admin.10",lang));
            System.out.printf(" [11] %-22s  [0]  %s  [99] %s%n",
                    I18n.t("menu.admin.11",lang),I18n.t("menu.admin.0",lang),I18n.t("menu.admin.99",lang));
            int ch = choose(lang);
            switch (ch) {
                case 1:
                    System.out.print("Name: ");     String n1=reader.readLine().trim();
                    System.out.print("Surname: ");  String s1=reader.readLine().trim();
                    System.out.print("Birthdate: ");String bd=reader.readLine().trim();
                    System.out.print("Phone: ");    String ph=reader.readLine().trim();
                    System.out.print("Email: ");    String em=reader.readLine().trim();
                    System.out.print("Password: "); String pw=reader.readLine().trim();
                    System.out.print("ID: ");       String id=reader.readLine().trim();
                    System.out.print("Year: ");     int yr=Integer.parseInt(reader.readLine().trim());
                    a.createStudent(n1,s1,bd,ph,em,pw,id,yr); Database.save(); break;
                case 2:
                    System.out.print("Name: ");     String n2=reader.readLine().trim();
                    System.out.print("Surname: ");  String s2=reader.readLine().trim();
                    System.out.print("Birthdate: ");String bd2=reader.readLine().trim();
                    System.out.print("Phone: ");    String ph2=reader.readLine().trim();
                    System.out.print("Email: ");    String em2=reader.readLine().trim();
                    System.out.print("Password: "); String pw2=reader.readLine().trim();
                    System.out.print("Experience: ");String ex=reader.readLine().trim();
                    a.createTeacher(n2,s2,bd2,ph2,em2,pw2,ex); Database.save(); break;
                case 3:
                    System.out.print("Name: ");     String n3=reader.readLine().trim();
                    System.out.print("Surname: ");  String s3=reader.readLine().trim();
                    System.out.print("Birthdate: ");String bd3=reader.readLine().trim();
                    System.out.print("Phone: ");    String ph3=reader.readLine().trim();
                    System.out.print("Email: ");    String em3=reader.readLine().trim();
                    System.out.print("Password: "); String pw3=reader.readLine().trim();
                    a.createManager(n3,s3,bd3,ph3,em3,pw3); Database.save(); break;
                case 4:
                    System.out.print("Name: ");     String n4=reader.readLine().trim();
                    System.out.print("Surname: ");  String s4=reader.readLine().trim();
                    System.out.print("Birthdate: ");String bd4=reader.readLine().trim();
                    System.out.print("Phone: ");    String ph4=reader.readLine().trim();
                    System.out.print("Email: ");    String em4=reader.readLine().trim();
                    System.out.print("Password: "); String pw4=reader.readLine().trim();
                    a.createLibrarian(n4,s4,bd4,ph4,em4,pw4); Database.save(); break;
                case 5:
                    System.out.print("Login: ");
                    System.out.println(a.deleteUser(reader.readLine().trim()) ? "Deleted." : "Not found.");
                    Database.save(); break;
                case 6:
                    System.out.print("Login: ");    String lg=reader.readLine().trim();
                    System.out.print("New email: ");String ne=reader.readLine().trim();
                    a.updateUserEmail(lg,ne); Database.save(); break;
                case 7:  System.out.println(a.getUsers()); break;
                case 8:  a.seeLogFiles(); break;
                case 9:  System.out.println(a.viewNewsTab()); break;
                case 10:
                    System.out.print("From: ");String mf=reader.readLine().trim();
                    System.out.print("To: ");  String mt=reader.readLine().trim();
                    System.out.print("Title: ");String ttl=reader.readLine().trim();
                    System.out.print("Text: "); String tx=reader.readLine().trim();
                    a.sendMessage(mf,mt,ttl,tx); Database.save(); break;
                case 11: System.out.println(a.getMessages()); break;
                case 0:
                    String op=prompt("prompt.old_pass",lang); String np=prompt("prompt.new_pass",lang);
                    System.out.println(a.changePassword(op,np)
                            ? I18n.t("msg.pass_changed",lang) : I18n.t("msg.pass_wrong",lang));
                    Database.save(); break;
                case 99: System.exit(0);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // TECH SUPPORT MENU
    // ─────────────────────────────────────────────────────────────────────
    static void techMenu(TechSupportSpecialist ts, Language lang) throws IOException {
        while (true) {
            System.out.println("\n" + I18n.t("menu.tech.title",lang));
            System.out.printf(" [1] %-24s  [2] %s%n",I18n.t("menu.tech.1",lang),I18n.t("menu.tech.2",lang));
            System.out.printf(" [3] %-24s  [4] %s%n",I18n.t("menu.tech.3",lang),I18n.t("menu.tech.4",lang));
            System.out.printf(" [5] %-24s  [6] %s%n",I18n.t("menu.tech.5",lang),I18n.t("menu.tech.6",lang));
            System.out.printf(" [7] %-24s  [0] %s  [99] %s%n",
                    I18n.t("menu.tech.7",lang),I18n.t("menu.tech.0",lang),I18n.t("menu.tech.99",lang));
            int ch = choose(lang);
            switch (ch) {
                case 1: System.out.println(ts.viewNewRequests()); break;
                case 2: System.out.println(ts.viewAllRequests()); break;
                case 3: case 4: case 5:
                    String rid = prompt("prompt.request_id",lang);
                    TechRequest tr = null;
                    for (TechRequest r : Database.techRequests)
                        if (r.getId().equals(rid)) { tr=r; break; }
                    if (tr==null) { System.out.println("Not found."); break; }
                    ts.receiveRequest(tr);
                    if (ch==3) ts.acceptRequest(tr);
                    else if (ch==4) ts.rejectRequest(tr);
                    else ts.completeRequest(tr);
                    Database.save(); break;
                case 6:
                    System.out.print("ID: ");          String nrid=reader.readLine().trim();
                    System.out.print("Description: "); String nd=reader.readLine().trim();
                    Database.techRequests.add(new TechRequest(nrid, ts.getLogin(), nd));
                    Database.save(); System.out.println(I18n.t("msg.saved",lang)); break;
                case 7: System.out.println(ts.viewNewsTab()); break;
                case 0:
                    String op=prompt("prompt.old_pass",lang); String np=prompt("prompt.new_pass",lang);
                    System.out.println(ts.changePassword(op,np)
                            ? I18n.t("msg.pass_changed",lang) : I18n.t("msg.pass_wrong",lang));
                    Database.save(); break;
                case 99: System.exit(0);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // LIBRARIAN MENU
    // ─────────────────────────────────────────────────────────────────────
    static void libMenu(Librarian l, Language lang) throws IOException {
        while (true) {
            System.out.println("\n" + I18n.t("menu.lib.title",lang));
            System.out.printf(" [1] %-24s  [2] %s%n",I18n.t("menu.lib.1",lang),I18n.t("menu.lib.2",lang));
            System.out.printf(" [3] %-24s  [4] %s%n",I18n.t("menu.lib.3",lang),I18n.t("menu.lib.4",lang));
            System.out.printf(" [5] %-24s  [6] %s%n",I18n.t("menu.lib.5",lang),I18n.t("menu.lib.6",lang));
            System.out.printf(" [0] %-24s  [99] %s%n",I18n.t("menu.lib.0",lang),I18n.t("menu.lib.99",lang));
            int ch = choose(lang);
            switch (ch) {
                case 1:
                    System.out.print("Title: ");  String bt=reader.readLine().trim();
                    System.out.print("ID: ");     String bi=reader.readLine().trim();
                    System.out.print("Author: "); String ba=reader.readLine().trim();
                    l.addBook(bt,bi,ba); Database.save(); break;
                case 2:
                    System.out.print("Book ID: ");
                    l.removeBook(reader.readLine().trim()); Database.save(); break;
                case 3:
                    System.out.print("Student ID: ");   String sid7=reader.readLine().trim();
                    System.out.print("Book ID: ");      String bid=reader.readLine().trim();
                    System.out.print("ACCEPT/REJECT: ");String req=reader.readLine().trim();
                    System.out.println(l.updateOrderBook(sid7,bid,req)); Database.save(); break;
                case 4: System.out.println(l.viewBooks()); break;
                case 5: System.out.println(l.viewOrders()); break;
                case 6: System.out.println(l.viewNewsTab()); break;
                case 0:
                    String op=prompt("prompt.old_pass",lang); String np=prompt("prompt.new_pass",lang);
                    System.out.println(l.changePassword(op,np)
                            ? I18n.t("msg.pass_changed",lang) : I18n.t("msg.pass_wrong",lang));
                    Database.save(); break;
                case 99: System.exit(0);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // RESEARCHER MENU
    // ─────────────────────────────────────────────────────────────────────
    static void researcherMenu(Researcher r, Language lang) throws IOException {
        while (true) {
            System.out.println("\n" + I18n.t("menu.researcher.title",lang));
            System.out.printf(" [1] %-24s  [2] %s%n",I18n.t("menu.researcher.1",lang),I18n.t("menu.researcher.2",lang));
            System.out.printf(" [3] %-24s  [4] %s%n",I18n.t("menu.researcher.3",lang),I18n.t("menu.researcher.4",lang));
            System.out.printf(" [5] %-24s  [6] %s%n",I18n.t("menu.researcher.5",lang),I18n.t("menu.researcher.6",lang));
            System.out.printf(" [7] %-24s  [8] %s%n",I18n.t("menu.researcher.7",lang),I18n.t("menu.researcher.8",lang));
            System.out.printf(" [9] %-24s  [10] %s%n",I18n.t("menu.researcher.9",lang),I18n.t("menu.researcher.10",lang));
            System.out.printf(" [0] %-24s  [99] %s%n",I18n.t("menu.researcher.0",lang),I18n.t("menu.researcher.99",lang));
            int ch = choose(lang);
            switch (ch) {
                case 1: System.out.println("h-index: " + r.calculateHIndex()); break;
                case 2: System.out.println(I18n.t("msg.sort_cit",lang)); r.printPapers(PaperComparators.BY_CITATIONS); break;
                case 3: System.out.println(I18n.t("msg.sort_date",lang)); r.printPapers(PaperComparators.BY_DATE); break;
                case 4: System.out.println(I18n.t("msg.sort_len",lang)); r.printPapers(PaperComparators.BY_LENGTH); break;
                case 5:
                    System.out.print("Title: ");   String pt=reader.readLine().trim();
                    System.out.print("Journal: "); String pj=reader.readLine().trim();
                    System.out.print("Pages: ");   int pp=Integer.parseInt(reader.readLine().trim());
                    System.out.print("DOI: ");     String pd=reader.readLine().trim();
                    ResearchPaper np2 = new ResearchPaper(pt,
                            Arrays.asList(r.getName()+" "+r.getSurname()),
                            pj, pp, LocalDate.now(), pd);
                    r.addPaper(np2);
                    if (!Database.journals.isEmpty())
                        Database.journals.get(0).publishPaper(np2, r.getName()+" "+r.getSurname());
                    Database.save(); System.out.println(I18n.t("msg.saved",lang)); break;
                case 6: Database.researchProjects.forEach(System.out::println); break;
                case 7: System.out.println(r.viewNewsTab()); break;
                case 8: System.out.println(r.getMessages()); break;
                case 9: printAllResearchPapers(choosePaperComparator(lang)); break;
                case 10: subscribeToJournal(r); break;
                case 0:
                    String op=prompt("prompt.old_pass",lang); String np=prompt("prompt.new_pass",lang);
                    System.out.println(r.changePassword(op,np)
                            ? I18n.t("msg.pass_changed",lang) : I18n.t("msg.pass_wrong",lang));
                    Database.save(); break;
                case 99: System.exit(0);
            }
        }
    }
}
