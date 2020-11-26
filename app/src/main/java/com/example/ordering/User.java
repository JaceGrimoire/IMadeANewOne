package com.example.ordering;

public class User {
    public String uid, firstName, lastName, address, number, email, bday, city, province;

    public User() {
    }

    public User(String firstName, String lastName, String address, String number, String email, String bday, String city, String province, String uid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.number = number;
        this.email = email;
        this.bday = bday;
        this.city = city;
        this.province = province;
        this.uid = uid;
    }

    public String getUid() {return uid;}
    public void setUid(String uid) {
        this.uid = uid;
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

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getBday() {
        return bday;
    }
    public void setBday(String bday) {
        this.bday = bday;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
}
