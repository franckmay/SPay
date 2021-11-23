package com.os.speed.modele;

public class OwnUssd {
    private  int ussd_id;
    private String  prefix;
    private String  phone;
    private String ussd_idonline;
    private String name;
    private String code;
    private String codeussd;
    private boolean active;
    private String operateur;
    private String numero;
    private String type;
    private String datetime;
    private String dateupdate;
    private String expiration_at;

    public int getUssd_id() {
        return ussd_id;
    }

    public void setUssd_id(int ussd_id) {
        this.ussd_id = ussd_id;
    }

    public String getExpiration_at() {
        return expiration_at;
    }

    public void setExpiration_at(String expiration_at) {
        this.expiration_at = expiration_at;
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

    public String getUssd_idonline() {
        return ussd_idonline;
    }

    public void setUssd_idonline(String ussd_idonline) {
        this.ussd_idonline = ussd_idonline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeussd() {
        return codeussd;
    }

    public void setCodeussd(String codeussd) {
        this.codeussd = codeussd;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDateupdate() {
        return dateupdate;
    }

    public void setDateupdate(String dateupdate) {
        this.dateupdate = dateupdate;
    }
}
