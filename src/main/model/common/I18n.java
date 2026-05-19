package model.common;

import java.util.HashMap;
import java.util.Map;
import controller.*;
import enumm.*;
import exception.*;
import model.course.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * Internationalization helper.
 * Provides translated strings for KZ / EN / RU.
 */
public class I18n {

    private static final Map<String, Map<Language, String>> strings = new HashMap<>();

    static {
        add("menu.student.15", "Qatysu", "Attendance", "Poseshchaemost");
        add("menu.student.16", "Jurnalga jazylu", "Subscribe journal", "Podpisatsya na zhurnal");
        add("menu.teacher.17", "Qatysu belgileu", "Mark attendance", "Otmetit poseshchaemost");
        add("menu.teacher.18", "Qatysudy koru", "View attendance", "Posmotret poseshchaemost");
        add("menu.teacher.19", "Jurnalga jazylu", "Subscribe journal", "Podpisatsya na zhurnal");
        add("menu.teacher.20", "Barlyq maqala", "All research papers", "Vse nauchnye stati");
        add("menu.grad.9", "Qatysu", "Attendance", "Poseshchaemost");
        add("menu.grad.10", "Jurnalga jazylu", "Subscribe journal", "Podpisatsya na zhurnal");
        add("menu.researcher.9", "Barlyq maqalalar", "All research papers", "Vse nauchnye stati");
        add("menu.researcher.10", "Jurnalga jazylu", "Subscribe journal", "Podpisatsya na zhurnal");
        add("menu.student.title",
            "--- СТУДЕНТ МЕНІ ---",
            "/--- STUDENT MENU ---/",
            "/--- МЕНЮ СТУДЕНТА ---/");
        add("menu.student.1",  "Менің ақпаратым",     "My info",            "Мой профиль");
        add("menu.student.2",  "Курстарды қарау",     "View courses",       "Просмотр курсо");
        add("menu.student.3",  "Қол жетімді курстар", "Available courses",  "Доступные курсы");
        add("menu.student.4",  "Курс файлдары",       "Course files",       "Файлы курса");
        add("menu.student.5",  "Оқытушы туралы",      "Teacher info",       "Инфо о преподаателе");
        add("menu.student.6",  "Бағаларды қарау",     "View marks",         "Просмотр оценок");
        add("menu.student.7",  "Транскрипт",          "Transcript",         "Транскрипт");
        add("menu.student.8",  "Оқытушыны бағалау",   "Rate teacher",       "Оценить преподаателя");
        add("menu.student.9",  "Кітап тапсырысы",     "Order book",         "Заказать книгу");
        add("menu.student.10", "Курсқа тіркелу",      "Register course",    "Запись на курс");
        add("menu.student.11", "Курстан шығу",        "Drop course",        "Отказ от курса");
        add("menu.student.12", "Жаңалықтар",          "News",               "Ноости");
                add("menu.student.13", "Kitaptar", "View library books", "Spisok knig");        add("menu.student.14", "Uyym aqparaty", "Organization info", "Info ob organizatsii");        add("menu.student.0",  "Құпия сөзді өзгерту", "Change password",    "Сменить пароль");
        add("menu.student.99", "Шығу",                "Quit",               "Выход");

        add("menu.teacher.title",
            "--- ОҚЫТУШЫ МЕНІ ---",
            "/--- TEACHER MENU ---/",
            "/--- МЕНЮ ПРЕПОДАВАТЕЛЯ ---/");
        add("menu.teacher.1",  "Менің ақпаратым",   "My info",          "Мой профиль");
        add("menu.teacher.2",  "Курстар",           "Courses",          "Курсы");
        add("menu.teacher.3",  "Файл қосу",         "Add file",         "Добаить файл");
        add("menu.teacher.4",  "Файлды жою",        "Delete file",      "Удалить файл");
        add("menu.teacher.5",  "Студенттер тізімі", "Students list",    "Список студенто");
        add("menu.teacher.6",  "Студент ақпараты",  "Student info",     "Инфо студента");
        add("menu.teacher.7",  "Баға қою",          "Put mark",         "Выстаить оценку");
        add("menu.teacher.8",  "Бағаларды қарау",   "View marks",       "Просмотр оценок");
        add("menu.teacher.9",  "Хабар жіберу",      "Send message",     "Отпраить сообщение");
        add("menu.teacher.10", "Хабарларым",        "My messages",      "Мои сообщения");
        add("menu.teacher.11", "Рейтинг",           "Rating",           "Рейтинг");
        add("menu.teacher.12", "Шағым жіберу",      "Send complaint",   "Отпраить жалобу");
        add("menu.teacher.13", "Жаңалықтар",        "News",             "Ноости");
        add("menu.teacher.14", "h-индекс",          "h-index",          "h-индекс");
        add("menu.teacher.15", "Мақалаларды басып шығару", "Print papers", "Распечатать статьи");  
        add("menu.teacher.0",  "Құпия сөзді өзгерту","Change password", "Сменить пароль");
        add("menu.teacher.99", "Шығу",              "Quit",             "Выход");

        add("menu.manager.title",
            "--- МЕНЕДЖЕР МЕНІ ---",
            "/--- MANAGER MENU ---/",
            "/--- МЕНЮ МЕНЕДЖЕРА ---/");
        add("menu.manager.1",  "Курс жасау",         "Create course",        "Создать курс");
        add("menu.manager.2",  "Студенттер туралы",  "Students info",        "Инфо о студентах");
        add("menu.manager.3",  "Оқытушы туралы",     "Teacher info",         "Инфо о преподаателе");
        add("menu.manager.4",  "Тіркеу сұрауларын қарау", "Pending regs",   "Заяки на регистрацию");
        add("menu.manager.5",  "Тіркеуді бекіту",    "Approve reg",          "Подтердить запись");
        add("menu.manager.6",  "Оқытушы тағайындау", "Assign teacher",       "Назначить преподаателя");
        add("menu.manager.7",  "Жаңалық қосу",       "Add news",             "Добаить ноость");
        add("menu.manager.8",  "Жаңалықты жою",      "Remove news",          "Удалить ноость");
        add("menu.manager.9",  "Жаңалықты жаңарту",  "Update news",          "Обноить ноость");
        add("menu.manager.10", "Максималды балл",     "Max score",            "Макс. балл");
        add("menu.manager.11", "Минималды балл",      "Min score",            "Мин. балл");
        add("menu.manager.12", "Орташа балл",         "Avg score",            "Средний балл");
        add("menu.manager.13", "Қайта тапсырулар",   "Retake count",         "Кол-о пересдач");
        add("menu.manager.14", "ҰОК бойынша студенттер", "Students by GPA",  "Студенты по GPA");
        add("menu.manager.15", "Студенттер А-Я",     "Students A-Z",         "Студенты А-Я");
        add("menu.manager.16", "Оқытушылар А-Я",     "Teachers A-Z",         "Преподаатели А-Я");
        add("menu.manager.17", "Оқытушылар мәртебесі","Teachers by status",  "Преподаатели по статусу");
        add("menu.manager.18", "Жаңалықтар",          "News",                "Ноости");
        add("menu.manager.19", "Хабар жіберу",        "Send message",        "Отпраить сообщение");
        add("menu.manager.20", "Хабарларым",          "My messages",         "Мои сообщения");
        add("menu.manager.21", "Толық есеп",          "Full report",         "Полный отчёт");
        add("menu.manager.0",  "Құпия сөзді өзгерту","Change password",      "Сменить пароль");
        add("menu.manager.99", "Шығу",                "Quit",                "Выход");

        add("menu.admin.title",
            "--- ӘКІМШІ  МӘЗІР ---",
            "/--- ADMIN MENU ---/",
            "/--- МЕНЮ АДМИНИСТРАТОРА ---/");
        add("menu.admin.1",  "Студент жасау",   "Create student",  "Создать студента");
        add("menu.admin.2",  "Оқытушы жасау",   "Create teacher",  "Создать преподаателя");
        add("menu.admin.3",  "Менеджер жасау",  "Create manager",  "Создать менеджера");
        add("menu.admin.4",  "Кітапханашы жасау","Create librarian","Создать библиотекаря");
        add("menu.admin.5",  "Пайдаланушыны жою","Delete user",    "Удалить пользоателя");
        add("menu.admin.6",  "Эл. пошта жаңарту","Update email",   "Обноить email");
        add("menu.admin.7",  "Барлық пайдаланушылар","All users",  "Все пользоатели");
        add("menu.admin.8",  "Журнал файлдары", "See logs",        "Просмотр лого");
        add("menu.admin.9",  "Жаңалықтар",      "News",            "Ноости");
        add("menu.admin.10", "Хабар жіберу",    "Send message",    "Отпраить сообщение");
        add("menu.admin.11", "Хабарларым",      "My messages",     "Мои сообщения");
        add("menu.admin.0",  "Құпия сөз",       "Change password", "Сменить пароль");
        add("menu.admin.99", "Шығу",            "Quit",            "Выход");

        add("menu.tech.title",
            "--- ТЕХНОЛОГИЯЛЫҚ ҚОЛДАУ МӘЗІРІ ---",
            "/--- TECH SUPPORT MENU ---/",
            "/--- МЕНЮ ТЕХПОДДЕРЖКИ ---/");
        add("menu.tech.1",  "Жаңа сұраулар",    "View pending",      "Ноые заяки");
        add("menu.tech.2",  "Барлық сұраулар",  "View all",          "Все заяки");
        add("menu.tech.3",  "Қабылдау",         "Accept request",    "Принять заяку");
        add("menu.tech.4",  "Бас тарту",        "Reject request",    "Отклонить заяку");
        add("menu.tech.5",  "Аяқтау",           "Complete request",  "Заершить заяку");
        add("menu.tech.6",  "Жаңа сұрау жіберу","Submit request",   "Подать заяку");
        add("menu.tech.7",  "Жаңалықтар",       "News",              "Ноости");
        add("menu.tech.0",  "Құпия сөз",        "Change password",   "Сменить пароль");
        add("menu.tech.99", "Шығу",             "Quit",              "Выход");

        add("menu.lib.title",
            "--- КІТАПХАНАШЫ МӘЗІРІ ---",
            "/--- LIBRARIAN MENU ---/",
            "/--- МЕНЮ БИБЛИОТЕКАРЯ ---/");
        add("menu.lib.1",  "Кітап қосу",    "Add book",      "Добаить книгу");
        add("menu.lib.2",  "Кітапты жою",   "Remove book",   "Удалить книгу");
        add("menu.lib.3",  "Тапсырысты жаңарту","Update order","Обноить заказ");
        add("menu.lib.4",  "Кітаптар",      "View books",    "Список книг");
        add("menu.lib.5",  "Тапсырыстар",   "View orders",   "Заказы");
        add("menu.lib.6",  "Жаңалықтар",    "News",          "Ноости");
        add("menu.lib.0",  "Құпия сөз",     "Change password","Сменить пароль");
        add("menu.lib.99", "Шығу",          "Quit",          "Выход");

        add("menu.researcher.title",
            "--- ЗЕРТТЕУШІ МӘЗІРІ ---",
            "/--- RESEARCHER MENU ---/",
            "/--- МЕНЮ ИССЛЕДОВАТЕЛЯ ---/");
        add("menu.researcher.1",  "h-индекс",          "h-index",          "h-индекс");
        add("menu.researcher.2",  "Цитаталар бойынша", "Papers by citations","По цитироаниям");
        add("menu.researcher.3",  "Күні бойынша",      "Papers by date",    "По дате");
        add("menu.researcher.4",  "Ұзындығы бойынша",  "Papers by length",  "По длине");
        add("menu.researcher.5",  "Мақала қосу",       "Add paper",         "Добаить статью");
        add("menu.researcher.6",  "Жобалар",           "Research projects", "Проекты");
        add("menu.researcher.7",  "Жаңалықтар",        "News",              "Ноости");
        add("menu.researcher.8",  "Хабарларым",        "My messages",       "Мои сообщения");
        add("menu.researcher.0",  "Құпия сөз",         "Change password",   "Сменить пароль");
        add("menu.researcher.99", "Шығу",              "Quit",              "Выход");

        add("menu.manager.24", "Студентті бітіруші ету", "Graduate a student",  "Выпустить студента");
        add("menu.manager.22", "Студентті курстан шығару", "Remove student from course", "Убрать студента с курса");
        add("menu.manager.23", "Курсты жою",               "Delete course",              "Удалить курс");
        add("menu.teacher.16", "Студентті курстан шығару", "Remove student from course", "Убрать студента с курса");
        add("menu.grad.title",
            "--- МАГИСТРАНТ/ДОКТОРАНТ МӘЗІРІ ---",
            "/--- GRADUATE STUDENT MENU ---/",
            "/--- МЕНЮ АСПИРАНТА ---/");
        add("menu.grad.1",  "Менің ақпаратым",   "My info",            "Мой профиль");
        add("menu.grad.2",  "Транскрипт",        "Transcript",         "Транскрипт");
        add("menu.grad.3",  "Ғылыми жетекші",    "Supervisor info",    "Научный рукоодитель");
        add("menu.grad.4",  "Диплом жұмыстары",  "Diploma papers",     "Дипломные работы");
        add("menu.grad.5",  "h-индекс",          "h-index",            "h-индекс");
        add("menu.grad.6",  "Мақалалар (цит.)",  "Papers by citations","Статьи по цитироаниям");
        add("menu.grad.7",  "Жаңалықтар",        "News",               "Ноости");
        add("menu.grad.8", "Diplom jumysyn kosu", "Add diploma paper", "Dobavit diplomnuyu rabotu");
        add("menu.grad.0",  "Құпия сөз",         "Change password",    "Сменить пароль");
        add("menu.grad.99", "Шығу",              "Quit",               "Выход");

        // Prompts
        add("prompt.login",      "Логин енгізіңіз: ",  "Enter your login: ",      "Ведите логин: ");
        add("prompt.password",   "Құпия сөз: ",        "Enter password: ",        "Ведите пароль: ");
        add("prompt.old_pass",   "Ескі құпия сөз: ",   "Old password: ",          "Старый пароль: ");
        add("prompt.new_pass",   "Жаңа құпия сөз: ",   "New password: ",          "Ноый пароль: ");
        add("prompt.choose",     "Таңдаңыз: ",         "Choose: ",                "Выберите: ");
        add("prompt.teacher_name","Оқытушы аты: ",     "Teacher name: ",          "Имя преподаателя: ");
        add("prompt.student_name","Студент аты: ",     "Student name: ",          "Имя студента: ");
        add("prompt.course_id",  "Курс ID: ",          "Course ID: ",             "ID курса: ");
        add("prompt.rating",     "Рейтинг (1-5): ",    "Rating (1-5): ",          "Рейтинг (1-5): ");
        add("prompt.book_id",    "Кітап ID: ",         "Book ID: ",               "ID книги: ");
        add("prompt.language",   "Тіл (KZ/EN/RU): ",  "Language (KZ/EN/RU): ",   "Язык (KZ/EN/RU): ");
        add("prompt.reason",     "Себеп: ",            "Reason: ",                "Причина: ");
        add("prompt.urgency",    "Маңыздылық (LOW/MEDIUM/HIGH): ",
                                 "Urgency (LOW/MEDIUM/HIGH): ",
                                 "Срочность (LOW/MEDIUM/HIGH): ");
        add("prompt.request_id", "Сұрау ID: ",        "Request ID: ",            "ID заяки: ");

        // Messages
        add("msg.welcome",       "Қош келдіңіз, ",     "Welcome, ",               "Добро пожалоать, ");
        add("msg.quit",          "Шығу үшін q басыңыз.","Press q to quit.",        "Нажмите q для ыхода.");
        add("msg.wrong_pass",    "Қате құпия сөз.",    "Wrong password.",          "Неерный пароль.");
        add("msg.not_found",     "Пайдаланушы табылмады.","User not found.",       "Пользоатель не найден.");
        add("msg.pass_changed",  "Құпия сөз өзгертілді.","Password changed.",     "Пароль изменён.");
        add("msg.pass_wrong",    "Қате ескі пароль.",  "Wrong old password.",      "Неерный старый пароль.");
        add("msg.sent",          "Жіберілді.",         "Sent.",                    "Отпралено.");
        add("msg.saved",         "Сақталды.",          "Saved.",                   "Сохранено.");
        add("msg.lang_set",      "Тіл орнатылды: ",    "Language set: ",           "Язык устанолен: ");
        add("msg.no_supervisor", "Ғылыми жетекші жоқ.","No supervisor assigned.", "Рукоодитель не назначен.");
        add("msg.not_researcher","Зерттеуші емес.",    "Not a researcher.",        "Не яляется исследоателем.");
        add("msg.sort_cit",      "-- Цитаталар бойынша --","-- Papers by citations --","-- По цитироаниям --");
        add("msg.sort_date",     "-- Күні бойынша --", "-- Papers by date --",    "-- По дате --");
        add("msg.sort_len",      "-- Ұзындығы бойынша --","-- Papers by length --","-- По длине --");
        add("msg.sort_choose",   "Сұрыптау: [1]Цит. [2]Күн [3]Ұзындық",
                                 "Sort: [1]Citations [2]Date [3]Length",
                                 "Сортирока: [1]Цит. [2]Дата [3]Длина");
        add("msg.org_head",      " (Жетекші)",        " (HEAD)",                  " (РУКОВОДИТЕЛЬ)");
        add("msg.org_member",    " (Мүше)",           " (member)",                " (участник)");
        add("msg.org_none",      "Ұйымға мүше емес.", "Not in any organization.", "Не состоит  организации.");
    }

    private static void add(String key, String kz, String en, String ru) {
        Map<Language, String> m = new HashMap<>();
        m.put(Language.KZ, kz);
        m.put(Language.EN, en);
        m.put(Language.RU, ru);
        strings.put(key, m);
    }

    /** Get translated string for current user language. Falls back to EN. */
    public static String t(String key, Language lang) {
        Map<Language, String> m = strings.get(key);
        if (m == null) return key;
        return m.getOrDefault(lang, m.get(Language.EN));
    }

    /** Convenience overload  uses EN if lang is null. */
    public static String t(String key) {
        return t(key, Language.EN);
    }
}
