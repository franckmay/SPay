package com.os.speed.modele;

import java.util.Date;

public class CurentDate {
    private int id;
    private Date curentdate;
    private Date datetime;

    public CurentDate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCurentdate() {
        return curentdate;
    }

    public void setCurentdate(Date curentdate) {
        this.curentdate = curentdate;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
