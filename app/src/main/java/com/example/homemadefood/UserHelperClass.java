package com.example.homemadefood;

public class UserHelperClass {
//    private int id;
    String name, username, email, phone, passwd, type;
//    private static int nextId = 0;

    public UserHelperClass(String name, String username, String email, String phone, String passwd, String type) {
//        this.id = nextId++;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.passwd = passwd;
        this.type = type;
//        id += 1;
    }

    public UserHelperClass() {
//        this.id = nextId++;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public String getUsertype() {
//        return usertype;
//    }
//
//    public void setUsertype(String usertype) {
//        this.usertype = usertype;
//    }
//
//    public String getEmail() {
//        return email;
//    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
