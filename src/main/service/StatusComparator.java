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

/** Sorts teachers by status (PROFESSOR first) */
public class StatusComparator implements Comparator<Teacher> {
    @Override
    public int compare(Teacher t1, Teacher t2) {
        return t2.getTeacherStatus().compareTo(t1.getTeacherStatus());
    }
}
