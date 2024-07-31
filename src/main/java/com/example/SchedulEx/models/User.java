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
    private String accessLevel;
    private boolean newUser;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "instructor")
    private List<Course> courses = new ArrayList<>();

    public User() {
    }

    public User(String email, String password, String firstName, String lastName, String accessLevel) throws Exception
    {
        this.email = email;
        this.salt = PasswordHelper.generateSalt();
        this.password = PasswordHelper.encryptPassword(password, this.salt);
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessLevel = accessLevel;
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

    public AccessLevel getAccessLevel()
    {
        return AccessLevel.parse(accessLevel);
    }

    public void setAccessLevel(AccessLevel accessLevel)
    {
        this.accessLevel = accessLevel.toString();
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public List<Course> getCourses()
    {
        return new ArrayList<>(courses);
    }

    public void addCourse(Course course)
    {
        course.setInstructor(this);
        this.courses.add(course);
    }

    public void addInvigCourse(Course course) {
        this.courses.add(course);
        System.out.println("ADDED COURSE");
        System.out.println(this.courses);
    }

    public void removeCourse(Course course)
    {
        this.courses.remove(course);
    }

    public void setCourses(List<Course> courses)
    {
        this.courses = courses;
    }

    @Override
    public String toString(){
        return this.firstName + " " + this.lastName + " (" + this.email + ")";
    }
}