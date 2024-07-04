package com.example.SchedulEx.models;

import com.example.SchedulEx.helpers.PasswordHelper;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;
    private String email;
    private String password;
    private byte[] salt;
    private String firstName;
    private String lastName;
    private int accessLevel;
    //this.exams will contain different data dependent on this.accessLevel
    //Professors will have comma separated exam request ids, followed by comma separated exam ids
    //The split will be denoted with a colon
    //Eg "Req1,Req2,Req3:Ex1,Ex2,Ex3" where Reqn is a request and Exn is an exam
    //Invigilators will have comma separated exam ids
    //followed by a binary string representing whether an exam is accepted
    //The split will be denoted with a colon
    //Eg "Ex1,Ex2,Ex3:101" where Exn is an exam, and the user has accepted Ex1 and Ex3
    private String exams;
    private boolean newUser;

    public User() {
    }

    public User(String email, String password, String firstName, String lastName, String accessLevel) throws Exception {
        this.email = email;
        this.salt = PasswordHelper.generateSalt();
        this.password = PasswordHelper.encryptPassword(password, this.salt);
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessLevel = AccessLevel.parse(accessLevel);
        this.exams = "";
        this.newUser = true;
    }

    public User(String email, String password, String firstName, String lastName, String accessLevel, String exams) throws Exception {
        this.email = email;
        this.salt = PasswordHelper.generateSalt();
        this.password = PasswordHelper.encryptPassword(password, this.salt);
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessLevel = AccessLevel.parse(accessLevel);
        this.exams = exams;
        this.newUser = true;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        this.password = PasswordHelper.encryptPassword(password, this.salt);
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = AccessLevel.parse(accessLevel);
    }

    public String getExams() {
        return exams;
    }

    public void setExams(String exams) {
        this.exams = exams;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

//    public List<String> getExamList()
//    {
//        return new ArrayList<>(examList);
//    }
}