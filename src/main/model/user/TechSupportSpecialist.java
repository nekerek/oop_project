package model.user;

import java.util.Vector;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.course.*;
import model.research.*;
import repository.*;
import service.*;

/**
 * Tech support specialist  handles TechRequests.
 * Pattern: works with RequestStatus enum lifecycle.
 */
public class TechSupportSpecialist extends Employee {
    private static final long serialVersionUID = 1L;

    public TechSupportSpecialist(String name, String surname, String birthDate,
                                  String phoneNumber, String email, String password) {
        super(name, surname, birthDate, phoneNumber, email, password);
    }

    /** Receive a new request  sets status to VIEWED */
    public void receiveRequest(TechRequest request) {
        request.setStatus(RequestStatus.VIEWED);
        System.out.println(getName() + " received request: " + request.getDescription());
    }

    public void acceptRequest(TechRequest request) {
        request.setStatus(RequestStatus.ACCEPTED);
        System.out.println("Request ACCEPTED: " + request.getDescription());
    }

    public void rejectRequest(TechRequest request) {
        request.setStatus(RequestStatus.REJECTED);
        System.out.println("Request REJECTED: " + request.getDescription());
    }

    public void completeRequest(TechRequest request) {
        request.setStatus(RequestStatus.DONE);
        System.out.println("Request DONE: " + request.getDescription());
    }

    public String viewNewRequests() {
        String s = "";
        int i = 0;
        for (TechRequest r : Database.techRequests) {
            if (r.getStatus() == RequestStatus.NEW
                    || r.getStatus() == RequestStatus.VIEWED) {
                i++;
                s += i + ") " + r + "\n";
            }
        }
        return s.isEmpty() ? "No pending requests." : s;
    }

    public String viewAllRequests() {
        String s = "";
        int i = 0;
        for (TechRequest r : Database.techRequests) {
            i++;
            s += i + ") " + r + "\n";
        }
        return s.isEmpty() ? "No requests." : s;
    }

    @Override
    public String getRole() { return "TechSupportSpecialist"; }
}
