package com.os.speed.modele;

public class Ussd {
    String phonenumber;
    String choiceoperateur;
    String prefix;
    String phone;
    String code;
    String copagnie_name;
    String type;
    String radicale;

    public String getRadicale() {
        return radicale;
    }

    public void setRadicale(String radicale) {
        this.radicale = radicale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCopagnie_name() {
        return copagnie_name;
    }

    public void setCopagnie_name(String copagnie_name) {
        this.copagnie_name = copagnie_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getChoiceoperateur() {
        return choiceoperateur;
    }

    public void setChoiceoperateur(String choiceoperateur) {
        this.choiceoperateur = choiceoperateur;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
