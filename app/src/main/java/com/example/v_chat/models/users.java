package com.example.v_chat.models;

public class users {
    String profilepic,mail,username,password,userid,lastmessage,status;


    public users(){

    }


    public users(String profilepic, String mail, String username, String password, String userid, String lastmessage, String status) {
        this.profilepic = profilepic;
        this.mail = mail;
        this.username = username;
        this.password = password;
        this.userid = userid;
        this.lastmessage = lastmessage;
        this.status = status;
    }

    public users(String username, String mail, String password) {
        this.username = username;
        this.mail = mail;
        this.password = password;

    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
