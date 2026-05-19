package service;

import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.research.*;
import model.user.*;
import repository.*;

/**
 * PATTERN: Factory
 * Creates User subclass instances without exposing constructors to callers.
 */
public class UserFactory {

    public enum UserType {
        STUDENT, GRADUATE_STUDENT, TEACHER, MANAGER,
        ADMIN, LIBRARIAN, TECH_SUPPORT, RESEARCHER
    }

    public static User createUser(UserType type,
                                   String name, String surname, String birthDate,
                                   String phoneNumber, String email, String password,
                                   Object... extras) {
        switch (type) {
            case STUDENT:
                return new Student(name, surname, birthDate, phoneNumber, email, password,
                        (String)  extras[0],   // id
                        (Integer) extras[1],   // yearOfStudy
                        (Faculty) extras[2],   // faculty
                        (Degree)  extras[3]);  // degree

            case GRADUATE_STUDENT:
                return new GraduateStudent(name, surname, birthDate, phoneNumber,
                        email, password,
                        (String)  extras[0],   // id
                        (Integer) extras[1],   // yearOfStudy
                        (Faculty) extras[2],   // faculty
                        (Degree)  extras[3]);  // degree (MASTER or PHD)

            case TEACHER:
                return new Teacher(name, surname, birthDate, phoneNumber, email, password,
                        (Status) extras[0],    // teacherStatus
                        (String) extras[1]);   // experience

            case MANAGER:
                return new Manager(name, surname, birthDate, phoneNumber, email, password,
                        (Managers) extras[0]); // managerType

            case ADMIN:
                return new Admin(name, surname, birthDate, phoneNumber, email, password);

            case LIBRARIAN:
                return new Librarian(name, surname, birthDate, phoneNumber, email, password);

            case TECH_SUPPORT:
                return new TechSupportSpecialist(name, surname, birthDate,
                        phoneNumber, email, password);

            case RESEARCHER:
                return new Researcher(name, surname, birthDate, phoneNumber, email, password);

            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}
