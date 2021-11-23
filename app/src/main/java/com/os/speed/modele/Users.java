package com.os.speed.modele;

public class Users {
    private int id;
    private String token;
    private int Sync_status;
    ///////////////////
    private String idonline;
    private String prefix;
    private String phone;
    private int active;
    private boolean verified;
    private String preferences;
    private String discussions;
    private String last_connection;
    private String last_verification;
    private String created_at;
    private String updated_at;
    private byte[]image;

    ///////////////////
    private String userName;
    private String description;
    private String imageProfil;


    public Users() {
    }

    public Users(int id, String token, String idonline, String userName, String description, String imageProfil,String last_connection) {
        this.id = id;
        this.token = token;
        this.idonline = idonline;
        this.userName = userName;
        this.description = description;
        this.imageProfil = imageProfil;
        this.last_connection=last_connection;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSync_status() {
        return Sync_status;
    }

    public void setSync_status(int sync_status) {
        Sync_status = sync_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setTokn(String token) {
        this.token = token;
    }


    //////////////////////


    public String getIdonline() {
        return idonline;
    }

    public void setIdonline(String idonline) {
        this.idonline = idonline;
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

    public int isActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getDiscussions() {
        return discussions;
    }

    public void setDiscussions(String discussions) {
        this.discussions = discussions;
    }

    public String getLast_connection() {
        return last_connection;
    }

    public void setLast_connection(String last_connection) {
        this.last_connection = last_connection;
    }

    public String getLast_verification() {
        return last_verification;
    }

    public void setLast_verification(String last_verification) {
        this.last_verification = last_verification;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
    ///////////////////////////////


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageProfil() {
        return imageProfil;
    }

    public void setImageProfil(String imageProfil) {
        this.imageProfil = imageProfil;
    }
}
