package model.course;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import controller.*;
import enumm.*;
import exception.*;
import model.common.*;
import model.research.*;
import model.user.*;
import repository.*;
import service.*;

/**
 * A single lesson session. Type is now a proper LessonType enum.
 */
public class Lesson implements Serializable {
    private static final long serialVersionUID = 1L;

    private LessonType lessonType;
    private Date time;
    private String room;
    private String courseId;
    private String teacherLogin;

    public Lesson() {}

    public Lesson(LessonType lessonType, Date time, String room,
                  String courseId, String teacherLogin) {
        this.lessonType = lessonType;
        this.time = time;
        this.room = room;
        this.courseId = courseId;
        this.teacherLogin = teacherLogin;
    }

    public LessonType getLessonType() { return lessonType; }
    public void setLessonType(LessonType t) { this.lessonType = t; }
    public Date getTime() { return time; }
    public void setTime(Date time) { this.time = time; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getCourseId() { return courseId; }
    public String getTeacherLogin() { return teacherLogin; }

    @Override
    public String toString() {
        return lessonType + " | Room: " + room
                + " | Course: " + courseId
                + " | Teacher: " + teacherLogin
                + " | " + time;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Lesson other)) return false;
        return lessonType == other.lessonType
                && Objects.equals(time, other.time)
                && Objects.equals(room, other.room)
                && Objects.equals(courseId, other.courseId)
                && Objects.equals(teacherLogin, other.teacherLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonType, time, room, courseId, teacherLogin);
    }
}
