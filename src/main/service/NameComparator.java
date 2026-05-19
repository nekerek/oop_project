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

/** Sorts Users alphabetically A→Z by name (FIXED: was reversed Z→A) */
public class NameComparator implements Comparator<User> {
    @Override
    public int compare(User s1, User s2) {
        return s1.getName().compareToIgnoreCase(s2.getName());
    }
}
