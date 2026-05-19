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

/** Sorts students by GPA descending (highest first) */
public class GPASorter implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return s2.getGPA().compareTo(s1.getGPA());
    }
}
