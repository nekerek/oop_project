package exception;

import controller.*;
import enumm.*;
import model.common.*;
import model.course.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

public class CreditOverFlow extends Exception {
    public CreditOverFlow(String message){
        super(message);
    }
}
