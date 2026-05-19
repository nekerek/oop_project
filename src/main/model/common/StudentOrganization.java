package model.common;

import java.io.Serializable;
import java.util.Vector;
import controller.*;
import enumm.*;
import exception.*;
import model.course.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * A student organization (club, society, etc.).
 * Students can be members or the head.
 */
public class StudentOrganization implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String headStudentId;
    private Vector<String> memberIds;

    public StudentOrganization(String name) {
        this.name = name;
        this.memberIds = new Vector<>();
    }

    public void setHead(String studentId) {
        this.headStudentId = studentId;
        if (!memberIds.contains(studentId)) memberIds.add(studentId);
        System.out.println("Student " + studentId + " is now head of " + name);
    }

    public void addMember(String studentId) {
        if (!memberIds.contains(studentId)) {
            memberIds.add(studentId);
            System.out.println("Student " + studentId + " joined " + name);
        }
    }

    public void removeMember(String studentId) {
        memberIds.remove(studentId);
    }

    public String getName() { return name; }
    public String getHeadStudentId() { return headStudentId; }
    public Vector<String> getMemberIds() { return memberIds; }

    @Override
    public String toString() {
        return "Organization: " + name + " | Head: " + headStudentId
                + " | Members: " + memberIds.size();
    }
}
