package model.course;

import java.io.Serializable;
import java.util.Date;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/**
* @generated
*/
public class File implements Serializable {
    
    /**
    * @generated
    */
    private String fileName;
    private String courseId;
    private String description;
    private Date postDate;

    public File() {
        
    }

    public File(String fileName, String courseId, String description) {
        this.fileName = fileName;
        this.courseId = courseId;
        this.description = description;
        this.postDate = new Date();
    }


    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPostDate() {
        return this.postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }    
    

    
}
