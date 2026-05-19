package model.common;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import controller.*;
import enumm.*;
import exception.*;
import model.course.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/**
* @generated
*/
public class Message implements Serializable {
    
    private String messageFrom;
    private String messageTo;
    private String title;
    private String text;
    private Date msgDate;

    public Message(String messageFrom, String messageTo, String title, String text) {
        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
        this.title = title;
        this.text = text;
        this.msgDate = new Date();
    }

    public String getMessageFrom() {
        return this.messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public String getMessageTo() {
        return this.messageTo;
    }

    public void setMessageTo(String messageTo) {
        this.messageTo = messageTo;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getMsgDate() {
        return this.msgDate;
    }

    public void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }
    
    

    //                          Operations                                  


    @Override
    public String toString() {
        return "{" +
            " messageFrom='" + getMessageFrom() + "'" +
            ", messageTo='" + getMessageTo() + "'" +
            ", title='" + getTitle() + "'" +
            ", text='" + getText() + "'" +
            ", msgDate='" + getMsgDate() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Message other)) return false;
        return Objects.equals(messageFrom, other.messageFrom)
                && Objects.equals(messageTo, other.messageTo)
                && Objects.equals(title, other.title)
                && Objects.equals(msgDate, other.msgDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageFrom, messageTo, title, msgDate);
    }

}
