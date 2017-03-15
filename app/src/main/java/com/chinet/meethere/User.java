package com.chinet.meethere;

public class User {

    private int id;
    private String name;
    private String surname;
    private String email;
    private String city;
    private String dayOfBirthday;

    public User() {}

    public User(int id, String name, String surname, String email, String city, String dayOfBirthday) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.city = city;
        this.dayOfBirthday = dayOfBirthday;
    }

    public User(String name, String surname, String email, String city, String dayOfBirthday) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.city = city;
        this.dayOfBirthday = dayOfBirthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDayOfBirthday() {
        return dayOfBirthday;
    }

    public void setDayOfBirthday(String dayOfBirthday) {
        this.dayOfBirthday = dayOfBirthday;
    }
}
