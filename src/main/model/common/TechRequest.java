package model.common;

import java.io.Serializable;
import java.util.Date;
import controller.*;
import enumm.*;
import exception.*;
import model.course.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * A technical support request (e.g. fix projector, printer).
 */
public class TechRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String requesterLogin;
    private String description;
    private RequestStatus status;
    private Date createdAt;

    public TechRequest(String id, String requesterLogin, String description) {
        this.id = id;
        this.requesterLogin = requesterLogin;
        this.description = description;
        this.status = RequestStatus.NEW;
        this.createdAt = new Date();
    }

    public String getId() { return id; }
    public String getRequesterLogin() { return requesterLogin; }
    public String getDescription() { return description; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public Date getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "TechRequest[" + id + "] " + description + " | Status: " + status
                + " | From: " + requesterLogin + " | " + createdAt;
    }
}
