package com.os.speed.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.os.speed.modele.Client;
import com.os.speed.modele.CurentDate;
import com.os.speed.modele.OwnUssd;
import com.os.speed.modele.Pub;
import com.os.speed.modele.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.os.speed.StiticSimple.DATABASE;
import static com.os.speed.StiticSimple.DATABASE_VERSION;
import static com.os.speed.sqlite.DBClient.ACTIVE;
import static com.os.speed.sqlite.DBClient.CLIENT_ID;
import static com.os.speed.sqlite.DBClient.CLIENT_ONLINE;
import static com.os.speed.sqlite.DBClient.CODE;
import static com.os.speed.sqlite.DBClient.CODEUSSD;
import static com.os.speed.sqlite.DBClient.CREATE_TABLE_CLIENT_LOCAL;
import static com.os.speed.sqlite.DBClient.DATETIME;
import static com.os.speed.sqlite.DBClient.DATEUPDATE;
import static com.os.speed.sqlite.DBClient.DROP_TABLE_CLIENT_LOCAL;
import static com.os.speed.sqlite.DBClient.NAME;
import static com.os.speed.sqlite.DBClient.NUMERO;
import static com.os.speed.sqlite.DBClient.OPERATEUR;
import static com.os.speed.sqlite.DBClient.SELECTALL_CLIENT_LOCAL;
import static com.os.speed.sqlite.DBClient.SELECTWHERECLIENTCODE;
import static com.os.speed.sqlite.DBClient.SELECTWHERECLIENTIDONLINE;
import static com.os.speed.sqlite.DBClient.SELECTWHERECLIENTID_LOCAL;
import static com.os.speed.sqlite.DBClient.TABLE_CLIENT_LOCAL;
import static com.os.speed.sqlite.DBClient.TYPE;
import static com.os.speed.sqlite.DBOwnUssd.CREATE_TABLE_OWNUSSD_LOCAL;
import static com.os.speed.sqlite.DBOwnUssd.DROP_TABLE_OWNUSSD_LOCAL;
import static com.os.speed.sqlite.DBOwnUssd.EXPIRATION_AT;
import static com.os.speed.sqlite.DBOwnUssd.OWNUSSDACTIVE;
import static com.os.speed.sqlite.DBOwnUssd.OWNUSSDCODE;
import static com.os.speed.sqlite.DBOwnUssd.OWNUSSDCODEUSSD;
import static com.os.speed.sqlite.DBOwnUssd.OWNUSSDNAME;
import static com.os.speed.sqlite.DBOwnUssd.OWNUSSDNUMERO;
import static com.os.speed.sqlite.DBOwnUssd.OWNUSSDOPERATEUR;
import static com.os.speed.sqlite.DBOwnUssd.OWNUSSDTYPE;
import static com.os.speed.sqlite.DBOwnUssd.OWNUSSD_ONLINE;
import static com.os.speed.sqlite.DBOwnUssd.PHONE;
import static com.os.speed.sqlite.DBOwnUssd.PREFIX;
import static com.os.speed.sqlite.DBOwnUssd.SELECTALL_OWNUSSD_LOCAL;
import static com.os.speed.sqlite.DBOwnUssd.SELECTWHEREOWNUSSDCODE;
import static com.os.speed.sqlite.DBOwnUssd.SELECTWHEREOWNUSSDIDONLINE;
import static com.os.speed.sqlite.DBOwnUssd.SELECTWHEREOWNUSSDID_LOCAL;
import static com.os.speed.sqlite.DBOwnUssd.TABLE_OWNUSSD_LOCAL;
import static com.os.speed.sqlite.DBPub.AMOUNT;
import static com.os.speed.sqlite.DBPub.CITY;
import static com.os.speed.sqlite.DBPub.CREATEAT;
import static com.os.speed.sqlite.DBPub.CREATE_TABLE_PUB_LOCAL;
import static com.os.speed.sqlite.DBPub.DATE_PUBLICATION;
import static com.os.speed.sqlite.DBPub.DROP_TABLE_PUB_LOCAL;
import static com.os.speed.sqlite.DBPub.DURATION;
import static com.os.speed.sqlite.DBPub.GENRE;
import static com.os.speed.sqlite.DBPub.IDAVERT_ID;
import static com.os.speed.sqlite.DBPub.IMAGE;
import static com.os.speed.sqlite.DBPub.MAXAGE;
import static com.os.speed.sqlite.DBPub.MINAGE;
import static com.os.speed.sqlite.DBPub.PEOPLE_COUNT;
import static com.os.speed.sqlite.DBPub.PUB_ID;
import static com.os.speed.sqlite.DBPub.SELECTALL_PUB_LOCAL;
import static com.os.speed.sqlite.DBPub.SELECTWHEREPUBIDONLINE;
import static com.os.speed.sqlite.DBPub.STATUS;
import static com.os.speed.sqlite.DBPub.TABLE_PUB_LOCAL;
import static com.os.speed.sqlite.DBPub.TITLE;
import static com.os.speed.sqlite.DBPub.UPDATEAT;
import static com.os.speed.sqlite.DBPub.URL;
import static com.os.speed.sqlite.DBPub.VIEWS_ACOUNT;
import static com.os.speed.sqlite.DBdate.CREATE_TABLE_DATE_LOCAL;
import static com.os.speed.sqlite.DBdate.CURRENTDATE;
import static com.os.speed.sqlite.DBdate.DATE_ID;
import static com.os.speed.sqlite.DBdate.DROP_TABLE_DATE_LOCAL;
import static com.os.speed.sqlite.DBdate.SELECTWHEREDATEID_LOCAL;
import static com.os.speed.sqlite.DBdate.TABLE_DATE_LOCAL;
import static com.os.speed.sqlite.Dbusers.CREATE_TABLE_USERS_LOCAL;
import static com.os.speed.sqlite.Dbusers.SELECTALL_USERS_LOCAL;
import static com.os.speed.sqlite.Dbusers.SELECTWHEREUSERID_LOCAL;
import static com.os.speed.sqlite.Dbusers.TABLE_CREATEAT_ONLINE;
import static com.os.speed.sqlite.Dbusers.TABLE_LASTCONNECTION_ONLINE;
import static com.os.speed.sqlite.Dbusers.TABLE_LASTVERIFICATION_ONLINE;
import static com.os.speed.sqlite.Dbusers.TABLE_PREFERENCE_ONLINE;
import static com.os.speed.sqlite.Dbusers.TABLE_UPDATEAT_ONLINE;
import static com.os.speed.sqlite.Dbusers.TABLE_USERS_LOCAL;
import static com.os.speed.sqlite.Dbusers.USERS_ACTIVE_ONLINE;
import static com.os.speed.sqlite.Dbusers.USERS_ID_ONLINE;
import static com.os.speed.sqlite.Dbusers.USERS_NAME_ONLINE;
import static com.os.speed.sqlite.Dbusers.USERS_PHONE_ONLINE;
import static com.os.speed.sqlite.Dbusers.USERS_PREFIX_ONLINE;
import static com.os.speed.sqlite.Dbusers.USERS_VERIFIER_ONLINE;
import static com.os.speed.staticclass.DateToString;
import static com.os.speed.staticclass.StringTodate;


public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase database;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
        database = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS_LOCAL);// creation of users table LOCAL
        db.execSQL(CREATE_TABLE_DATE_LOCAL);// creation of users table DISTANT
        db.execSQL(CREATE_TABLE_CLIENT_LOCAL);// creation of users table DISTANT
        db.execSQL(CREATE_TABLE_OWNUSSD_LOCAL);// creation of users table DISTANT
        db.execSQL(CREATE_TABLE_PUB_LOCAL);// creation of users table DISTANT
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_OWNUSSD_LOCAL);// drop table users
        db.execSQL(DROP_TABLE_DATE_LOCAL);// drop table users
        db.execSQL(CREATE_TABLE_USERS_LOCAL);// drop table users
        db.execSQL(DROP_TABLE_CLIENT_LOCAL);// drop table users
        db.execSQL(DROP_TABLE_PUB_LOCAL);// drop table users
        onCreate(db);
    }


    ////////////////////////////////////////////////
    ///////////////////////////////
    public Boolean insertPub(Pub pub) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IDAVERT_ID, pub.getIdonline());
        contentValues.put(TITLE, pub.getTitle());
        contentValues.put(IMAGE, pub.getUrl_destination());
        contentValues.put(URL, pub.getUrl_image());
        contentValues.put(DATE_PUBLICATION, pub.getDate_publication());
        contentValues.put(DURATION, pub.getDuration());
        contentValues.put(PEOPLE_COUNT, pub.getPeople_count());
        contentValues.put(VIEWS_ACOUNT, pub.getViews_count());
        contentValues.put(STATUS, pub.getStatus());
        contentValues.put(AMOUNT, pub.getAmount());
        contentValues.put(GENRE, pub.getGender());
        contentValues.put(MINAGE, pub.getMinage());
        contentValues.put(MAXAGE, pub.getMaxage());
        contentValues.put(CITY, pub.getCity());
        contentValues.put(CREATEAT, pub.getCreated_at());
        contentValues.put(UPDATEAT, pub.getUpdated_at());


        long resutl = database.insert(TABLE_PUB_LOCAL, null, contentValues);
        if (resutl == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Boolean updatePubl(Pub pub) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, pub.getStatus());
        Cursor cursor = database.rawQuery(SELECTWHEREDATEID_LOCAL, new String[]{String.valueOf(pub.getIdonline())});
        if (cursor.getCount() > 0) {
            long resutl = database.update(TABLE_PUB_LOCAL, contentValues, DATE_ID + "=?", new String[]{String.valueOf(pub.getId())});
            if (resutl == -1) {
                return false;
            } else {
                return true;
            }

        }
        return false;
    }

    public List<Pub> getAllPub() {
        List<Pub> listOwn = new ArrayList<>();
        Cursor cursor = database.rawQuery(SELECTALL_PUB_LOCAL, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Pub client = new Pub();
                client.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PUB_ID))));
                client.setIdonline(cursor.getString(cursor.getColumnIndex(DBPub.IDAVERT_ID)));
                client.setTitle(cursor.getString(cursor.getColumnIndex(DBPub.TITLE)));
                client.setUrl_destination(cursor.getString(cursor.getColumnIndex(DBPub.IMAGE)));
                client.setUrl_image(cursor.getString(cursor.getColumnIndex(DBPub.URL)));
                client.setDate_publication(String.valueOf(cursor.getString(cursor.getColumnIndex(DBPub.DATE_PUBLICATION))));
                client.setDuration(cursor.getString(cursor.getColumnIndex(DBPub.DURATION)));
                client.setPeople_count(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBPub.PEOPLE_COUNT))));
                client.setViews_count(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBPub.VIEWS_ACOUNT))));
                client.setStatus(cursor.getString(cursor.getColumnIndex(DBPub.STATUS)));
                client.setAmount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBPub.AMOUNT))));
                client.setGender(cursor.getString(cursor.getColumnIndex(DBPub.GENRE)));
                client.setMinage(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBPub.MINAGE))));
                client.setMaxage(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MAXAGE))));
                client.setCity(cursor.getString(cursor.getColumnIndex(DBPub.CITY)));
                client.setCreated_at(cursor.getString(cursor.getColumnIndex(DBPub.CREATEAT)));
                client.setUpdated_at(cursor.getString(cursor.getColumnIndex(DBPub.UPDATEAT)));
                listOwn.add(client);
            }
            Log.e("hhhhhfffffffffffffffffffh", String.valueOf(listOwn.size()));
        }
        return listOwn;

    }

    public Pub getSpecificPub(String idonline) {
        Pub client = null;
        Cursor cursor = database.rawQuery(SELECTWHEREPUBIDONLINE, new String[]{String.valueOf(idonline)});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                client = new Pub();
                client.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PUB_ID))));
                client.setIdonline(cursor.getString(cursor.getColumnIndex(DBPub.IDAVERT_ID)));
                client.setTitle(cursor.getString(cursor.getColumnIndex(DBPub.TITLE)));
                client.setUrl_destination(cursor.getString(cursor.getColumnIndex(DBPub.IMAGE)));
                client.setUrl_image(cursor.getString(cursor.getColumnIndex(DBPub.URL)));
                client.setDate_publication(String.valueOf(cursor.getString(cursor.getColumnIndex(DBPub.DATE_PUBLICATION))));
                client.setDuration(cursor.getString(cursor.getColumnIndex(DBPub.DURATION)));
                client.setPeople_count(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBPub.PEOPLE_COUNT))));
                client.setViews_count(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBPub.VIEWS_ACOUNT))));
                client.setStatus(cursor.getString(cursor.getColumnIndex(DBPub.STATUS)));
                client.setAmount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBPub.AMOUNT))));
                client.setGender(cursor.getString(cursor.getColumnIndex(DBPub.GENRE)));
                client.setMinage(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBPub.MINAGE))));
                client.setMaxage(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MAXAGE))));
                client.setCity(cursor.getString(cursor.getColumnIndex(DBPub.CITY)));
                client.setCreated_at(cursor.getString(cursor.getColumnIndex(DBPub.CREATEAT)));
                client.setUpdated_at(cursor.getString(cursor.getColumnIndex(DBPub.UPDATEAT)));
            }
        } else {
            Log.e("dddd", "rient comme date");
        }
        return client;
    }


    public Boolean deletePub(Pub pub) {
        long resutl = database.delete(TABLE_PUB_LOCAL, PUB_ID + "=?", new String[]{String.valueOf(pub.getId())});
        if (resutl == -1) {
            return false;
        } else {
            return true;
        }

    }


    ////////////////////////////////////

    /////////////////////////////////////
    // Message
    public Boolean insertDate(CurentDate date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATETIME, DateToString(date.getDatetime()));
        contentValues.put(CURRENTDATE, DateToString(date.getCurentdate()));

        long resutl = database.insert(TABLE_DATE_LOCAL, null, contentValues);
        if (resutl == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateCurentdate(int iddate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CURRENTDATE, DateToString(new Date()));
        Cursor cursor = database.rawQuery(SELECTWHEREDATEID_LOCAL, new String[]{String.valueOf(iddate)});
        if (cursor.getCount() > 0) {
            long resutl = database.update(TABLE_DATE_LOCAL, contentValues, DATE_ID + "=?", new String[]{String.valueOf(iddate)});
            if (resutl == -1) {
                return false;
            } else {
                return true;
            }

        }
        return false;
    }
    public static final String CREATE_TABLE_CLIENT_LOCAL="create Table "+TABLE_CLIENT_LOCAL+
            "("+CLIENT_ID+" INTEGER primary key autoincrement,"+ NAME+" TEXT,"+ CLIENT_ONLINE+" TEXT,"+DATETIME+" TEXT,"+DATEUPDATE+" TEXT,"+CODE+" TEXT,"+CODEUSSD+" TEXT, "+ACTIVE+" BOOLEAN, "+OPERATEUR+" TEXT, "+NUMERO+" TEXT UNIQUE, "+TYPE+" TEXT, "+PREFIX+" TEXT, "+PHONE+" TEXT)";

    public Boolean insertCLient(Client client) {
        //SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // contentValues.put(MESSAGE_ID,message.);
        contentValues.put(NAME, String.valueOf(client.getName()));
        contentValues.put(CLIENT_ONLINE, String.valueOf(client.getIdonline()));
        contentValues.put(DATETIME, String.valueOf(client.getDateTime()));
        contentValues.put(DATEUPDATE, String.valueOf(client.getDateupdate()));
        contentValues.put(CODE, String.valueOf(client.getCode()));
        contentValues.put(CODEUSSD, String.valueOf(client.getCodeussd()));
        contentValues.put(ACTIVE, String.valueOf(client.isActive()));
        contentValues.put(OPERATEUR, String.valueOf(client.getOperateur()));
        contentValues.put(NUMERO, String.valueOf(client.getNumero()));
        contentValues.put(TYPE, String.valueOf(client.getType()));
        contentValues.put(DBClient.PREFIX, String.valueOf(client.getPrefix()));
        contentValues.put(DBClient.PHONE, String.valueOf(client.getPhone()));
        long resutl = database.insert(TABLE_CLIENT_LOCAL, null, contentValues);
        if (resutl == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean insertUssd(OwnUssd ussd) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OWNUSSD_ONLINE, String.valueOf(ussd.getUssd_idonline()));
        contentValues.put(OWNUSSDNAME, String.valueOf(ussd.getName()));
        contentValues.put(OWNUSSDCODE, String.valueOf(ussd.getCode()));
        contentValues.put(OWNUSSDCODEUSSD, String.valueOf(ussd.getCodeussd()));
        contentValues.put(OWNUSSDACTIVE, String.valueOf(ussd.isActive()));
        contentValues.put(OWNUSSDOPERATEUR, String.valueOf(ussd.getOperateur()));
        contentValues.put(OWNUSSDNUMERO, String.valueOf(ussd.getNumero()));
        contentValues.put(OWNUSSDTYPE, String.valueOf(ussd.getType()));
        contentValues.put(DBOwnUssd.DATETIME, String.valueOf(ussd.getDatetime()));
        contentValues.put(DBOwnUssd.DATEUPDATE, String.valueOf(ussd.getDateupdate()));
        contentValues.put(PREFIX, String.valueOf(ussd.getPrefix()));
        contentValues.put(PHONE, String.valueOf(ussd.getPhone()));
        contentValues.put(EXPIRATION_AT, String.valueOf(ussd.getExpiration_at()));

        long resutl = database.insert(TABLE_OWNUSSD_LOCAL, null, contentValues);
        if (resutl == -1) {
            Log.e("hhhhhfffffffffffffffffffh", "l555555555555555555555555555mq");
            return false;
        } else {
            Log.e("hhhhhfffffffffffffffffffh", "l666666666666666666666666666666666666");
            return true;
        }
    }

    public List<OwnUssd> getAllUssd() {
        List<OwnUssd> listOwn = new ArrayList<>();
        //  SQLiteDatabase DB=this.getWritableDatabase();
        Log.e("hhhhhfffffffffffffffffffh", "3333333333333333333333333333333333");
        Cursor cursor = database.rawQuery(SELECTALL_OWNUSSD_LOCAL, null);
        if (cursor.getCount() > 0) {
            Log.e("hhhhhfffffffffffffffffffh", "00000000000000000000000000000000000");
            while (cursor.moveToNext()) {
                OwnUssd client = new OwnUssd();
                client.setUssd_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSD_ID))));

                client.setPrefix(cursor.getString(cursor.getColumnIndex(DBOwnUssd.PREFIX)));
                client.setPhone(cursor.getString(cursor.getColumnIndex(DBOwnUssd.PHONE)));
                client.setExpiration_at(cursor.getString(cursor.getColumnIndex(DBOwnUssd.EXPIRATION_AT)));
                client.setUssd_idonline(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSD_ONLINE)));

                client.setName(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDNAME)));
                client.setCode(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDCODE)));
                client.setCodeussd(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDCODEUSSD)));
                client.setActive(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDACTIVE))));
                client.setOperateur(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDOPERATEUR)));
                client.setNumero(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDNUMERO)));
                client.setType(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDTYPE)));
                client.setDatetime(cursor.getString(cursor.getColumnIndex(DBOwnUssd.DATETIME)));
                client.setDateupdate(cursor.getString(cursor.getColumnIndex(DBOwnUssd.DATEUPDATE)));
                listOwn.add(client);
            }
            Log.e("hhhhhfffffffffffffffffffh", String.valueOf(listOwn.size()));
        }
        return listOwn;

    }

    public OwnUssd getSpecificOwnUssdWithCode(String code) {
        OwnUssd client = null;
        Cursor cursor = database.rawQuery(SELECTWHEREOWNUSSDCODE, new String[]{String.valueOf(code)});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                client = new OwnUssd();
                client.setUssd_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSD_ID))));
                client.setPrefix(cursor.getString(cursor.getColumnIndex(DBOwnUssd.PREFIX)));
                client.setPhone(cursor.getString(cursor.getColumnIndex(DBOwnUssd.PHONE)));
                client.setExpiration_at(cursor.getString(cursor.getColumnIndex(DBOwnUssd.EXPIRATION_AT)));
                client.setUssd_idonline(cursor.getString(cursor.getColumnIndex(OWNUSSD_ONLINE)));
                client.setName(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDNAME)));
                client.setCode(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDCODE)));
                client.setCodeussd(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDCODEUSSD)));
                client.setActive(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDACTIVE))));
                client.setOperateur(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDOPERATEUR)));
                client.setNumero(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDNUMERO)));
                client.setType(cursor.getString(cursor.getColumnIndex(DBOwnUssd.OWNUSSDTYPE)));
                client.setDatetime(cursor.getString(cursor.getColumnIndex(DBOwnUssd.DATETIME)));
                client.setDateupdate(cursor.getString(cursor.getColumnIndex(DBOwnUssd.DATEUPDATE)));


            }
        }
        return client;
    }


    public Boolean deleteUssd(OwnUssd ussd) {
        //  SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor = database.rawQuery(SELECTWHEREOWNUSSDID_LOCAL, new String[]{String.valueOf(ussd.getUssd_id())});
        if (cursor.getCount() > 0) {
            long resutl = database.delete(TABLE_OWNUSSD_LOCAL, DBOwnUssd.OWNUSSD_ID + "=?", new String[]{String.valueOf(ussd.getUssd_id())});
            if (resutl == -1) {
                return false;
            } else {
                return true;
            }
        }
        return false;

    }
    public Boolean updateOwnUssd(OwnUssd ussd){
        ContentValues contentValues=new ContentValues();
       // Log.e("onwactividd",String.valueOf(ussd.isActive()));
        contentValues.put(OWNUSSDACTIVE,String.valueOf(ussd.isActive()));
        contentValues.put(EXPIRATION_AT,ussd.getExpiration_at());
        Cursor cursor =database.rawQuery(SELECTWHEREOWNUSSDIDONLINE,new String[]{ussd.getUssd_idonline()});
        if(cursor.getCount()>0){
           // Log.e("atoubar", "debut updat Ussd own inwwxx  b fffff  ");
            long resutl=database.update(TABLE_OWNUSSD_LOCAL,contentValues, OWNUSSD_ONLINE+"=?",new String[]{ussd.getUssd_idonline()});
            if(resutl==-1){
                return false;
            }else{
                return true;
            }

        }
        return false;
    }

    public Boolean updateClient_online(Client client) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACTIVE, client.isActive());
        contentValues.put(DATEUPDATE, client.getDateupdate());
        Cursor cursor = database.rawQuery(SELECTALL_CLIENT_LOCAL, new String[]{String.valueOf(client.getIdonline())});
        if (cursor.getCount() > 0) {
            long resutl = database.update(TABLE_CLIENT_LOCAL, contentValues, CLIENT_ID + "=?", new String[]{String.valueOf(client.getId())});
            if (resutl == -1) {
                return false;
            } else {
                return true;
            }

        }
        return false;
    }

    public CurentDate getSpecificDate(int idonline) {
        CurentDate date = new CurentDate();
        Cursor cursor = database.rawQuery(SELECTWHEREDATEID_LOCAL, new String[]{String.valueOf(idonline)});
        if (cursor.getCount() > 0) {
            Log.e("dddd", "date existe");
            while (cursor.moveToNext()) {
                date.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBdate.DATE_ID))));
                date.setDatetime(StringTodate(cursor.getString(cursor.getColumnIndex(DBdate.DATETIME))));
                date.setCurentdate(StringTodate(cursor.getString(cursor.getColumnIndex(CURRENTDATE))));
            }
        } else {
            Log.e("dddd", "rient comme date");
        }
        return date;
    }

    public Client getSpecificClientWithOnlineId(String idonline) {
        Client client = new Client();
        Cursor cursor = database.rawQuery(SELECTWHERECLIENTIDONLINE, new String[]{String.valueOf(idonline)});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                client.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBClient.CLIENT_ID))));
                client.setIdonline(cursor.getString(cursor.getColumnIndex(DBClient.CLIENT_ONLINE)));
                client.setDateTime(cursor.getString(cursor.getColumnIndex(DBClient.DATETIME)));
                client.setDateupdate(cursor.getString(cursor.getColumnIndex(DATEUPDATE)));
                client.setActive(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(DBClient.ACTIVE))));
                client.setName(cursor.getString(cursor.getColumnIndex(DBClient.NAME)));
                client.setCode(cursor.getString(cursor.getColumnIndex(DBClient.CODE)));
                client.setCodeussd(cursor.getString(cursor.getColumnIndex(CODEUSSD)));

            }
        }
        return client;
    }

    public Client getSpecificClientWithCode(String code) {
        Client client = null;
        Cursor cursor = database.rawQuery(SELECTWHERECLIENTCODE, new String[]{String.valueOf(code)});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                client = new Client();
                client.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBClient.CLIENT_ID))));
                client.setIdonline(cursor.getString(cursor.getColumnIndex(DBClient.CLIENT_ONLINE)));
                client.setDateTime(cursor.getString(cursor.getColumnIndex(DBClient.DATETIME)));
                client.setDateupdate(cursor.getString(cursor.getColumnIndex(DATEUPDATE)));
                client.setActive(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(DBClient.ACTIVE))));
                client.setName(cursor.getString(cursor.getColumnIndex(DBClient.NAME)));
                client.setCode(cursor.getString(cursor.getColumnIndex(DBClient.CODE)));
                client.setCodeussd(cursor.getString(cursor.getColumnIndex(DBClient.CODEUSSD)));

                client.setOperateur(cursor.getString(cursor.getColumnIndex(DBClient.OPERATEUR)));
                client.setNumero(cursor.getString(cursor.getColumnIndex(DBClient.NUMERO)));
                client.setType(cursor.getString(cursor.getColumnIndex(DBClient.TYPE)));
                client.setPrefix(cursor.getString(cursor.getColumnIndex(DBClient.PREFIX)));
                client.setPhone(cursor.getString(cursor.getColumnIndex(DBClient.PHONE)));


            }
        }
        return client;
    }


    public List<Client> getAllCLient() {
        List<Client> listclients = new ArrayList<>();
        //  SQLiteDatabase DB=this.getWritableDatabase();

        Cursor cursor = database.rawQuery(SELECTALL_CLIENT_LOCAL, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Client client = new Client();
                client.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBClient.CLIENT_ID))));
                client.setIdonline(cursor.getString(cursor.getColumnIndex(DBClient.CLIENT_ONLINE)));
                client.setDateTime(cursor.getString(cursor.getColumnIndex(DBClient.DATETIME)));
                client.setDateupdate(cursor.getString(cursor.getColumnIndex(DATEUPDATE)));
                client.setActive(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(DBClient.ACTIVE))));
                client.setName(cursor.getString(cursor.getColumnIndex(DBClient.NAME)));
                client.setCode(cursor.getString(cursor.getColumnIndex(DBClient.CODE)));
                client.setCodeussd(cursor.getString(cursor.getColumnIndex(DBClient.CODEUSSD)));
                client.setOperateur(cursor.getString(cursor.getColumnIndex(DBClient.OPERATEUR)));
                client.setNumero(cursor.getString(cursor.getColumnIndex(DBClient.NUMERO)));
                client.setType(cursor.getString(cursor.getColumnIndex(DBClient.TYPE)));
                client.setPrefix(cursor.getString(cursor.getColumnIndex(DBClient.PREFIX)));
                client.setPhone(cursor.getString(cursor.getColumnIndex(DBClient.PHONE)));
                listclients.add(client);
            }
        }
        return listclients;

    }


    public Boolean deleteUser(Client client) {
        //  SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor = database.rawQuery(SELECTWHERECLIENTID_LOCAL, new String[]{String.valueOf(client.getId())});
        if (cursor.getCount() > 0) {
            long resutl = database.delete(TABLE_CLIENT_LOCAL, CLIENT_ID + "=?", new String[]{String.valueOf(client.getId())});
            if (resutl == -1) {
                return false;
            } else {
                return true;
            }
        }
        return false;

    }


    public void deleteAll() {
        database.delete(TABLE_CLIENT_LOCAL, null, null);
    }

    public void deleteAllPub() {
        database.delete(TABLE_PUB_LOCAL, null, null);
    }

    public void deleteAllUssd() {
        database.delete(TABLE_OWNUSSD_LOCAL, null, null);
    }
    /////////////////////////////
   /* public Boolean insertUsersonline(Users user){
        //SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(USERS_ID_ONLINE,user.getIdonline());
        contentValues.put(USERS_PREFIX_ONLINE,user.getPrefix());
        contentValues.put(USERS_PHONE_ONLINE,user.getPhone());
        contentValues.put(USERS_NAME_ONLINE,user.getUserName());
        contentValues.put(USERS_TOKEN_ONLINE,user.getToken());
        contentValues.put(USERS_ACTIVE_ONLINE,user.isActive());
        contentValues.put(USERS_VERIFIER_ONLINE,user.isVerified());
        contentValues.put(TABLE_PREFERENCE_ONLINE,user.getPreferences());
        contentValues.put(TABLE_DISCUSSIONS_ONLINE,user.getDiscussions());
        contentValues.put(TABLE_LASTCONNECTION_ONLINE,user.getLast_connection());
        contentValues.put(TABLE_LASTVERIFICATION_ONLINE,user.getLast_verification());
        contentValues.put(TABLE_CREATEAT_ONLINE,user.getCreated_at());
        contentValues.put(TABLE_UPDATEAT_ONLINE,user.getUpdated_at());
        long resutl=database.insert(TABLE_USERS_ONLINE,null,contentValues);
        if(resutl==-1){
            return false;
        }else{
            return true;
        }
    }
*/

    public Boolean insertUsers(Users user) {
        //SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_ID_ONLINE, user.getIdonline());
        contentValues.put(USERS_PREFIX_ONLINE, user.getPrefix());
        contentValues.put(USERS_PHONE_ONLINE, user.getPhone());
        contentValues.put(USERS_NAME_ONLINE, user.getUserName());
        contentValues.put(USERS_ACTIVE_ONLINE, user.isActive());
        contentValues.put(USERS_VERIFIER_ONLINE, user.isVerified());
        contentValues.put(TABLE_PREFERENCE_ONLINE, user.getPreferences());
        contentValues.put(TABLE_LASTCONNECTION_ONLINE, user.getLast_connection());
        contentValues.put(TABLE_LASTVERIFICATION_ONLINE, user.getLast_verification());
        contentValues.put(TABLE_CREATEAT_ONLINE, user.getCreated_at());
        contentValues.put(TABLE_UPDATEAT_ONLINE, user.getUpdated_at());
        // contentValues.put(TABLE_IMAGE_ONLINE,user.getImage());
        //   contentValues.put(SYNC_STATUS,user.getSync_status());
        long resutl = database.insert(TABLE_USERS_LOCAL, null, contentValues);
        if (resutl == -1) {
            return false;
        } else {
            return true;
        }
    }


    //////////////////////////////////////////


/*
    public Boolean updateUser_online(Users user){
        ContentValues contentValues=new ContentValues();
        contentValues.put(USERS_PHONE_ONLINE,user.getPhone());
        Cursor cursor =database.rawQuery(SELECTWHEREPHONE_ONLINE,new String[]{user.getToken()});
        if(cursor.getCount()>0){
            long resutl=database.update(TABLE_USERS_ONLINE,contentValues,USERS_PHONE_ONLINE+"=?",new String[]{user.getPhone()});
            if(resutl==-1){
                return false;
            }else{
                return true;
            }

        }
        return false;
    }

*/







/*

    public Boolean updateUser(Users user){
        ContentValues contentValues=new ContentValues();
        contentValues.put(USERS_TOKEN,user.getToken());
        Cursor cursor =database.rawQuery(SELECTWHERETOKEN,new String[]{user.getToken()});
        if(cursor.getCount()>0){
            long resutl=database.update(TABLE_USERS,contentValues,USERS_TOKEN+"=?",new String[]{user.getToken()});
            if(resutl==-1){
                return false;
            }else{
                return true;
            }

       }
        return false;
    }

 */
//  delete user online
    /*
public Boolean deleteUser_online(Users user){
    //  SQLiteDatabase DB=this.getWritableDatabase();
    Cursor cursor =database.rawQuery(SELECTWHEREPHONE_ONLINE,new String[]{user.getPhone()});
    if(cursor.getCount()>0){
        long resutl=database.delete(TABLE_USERS_ONLINE,USERS_PHONE_ONLINE+"=?",new String[]{user.getPhone()});
        if(resutl==-1){
            return false;
        }else{
            return true;
        }
    }
    return false;

}
*/


//  delete users
    /*
public Boolean deleteUser(Users user){
  //  SQLiteDatabase DB=this.getWritableDatabase();
    Cursor cursor =database.rawQuery(SELECTWHERETOKEN,new String[]{user.getToken()});
    if(cursor.getCount()>0){
        long resutl=database.delete(TABLE_USERS,USERS_TOKEN+"=?",new String[]{user.getToken()});
        if(resutl==-1){
            return false;
        }else{
            return true;
        }
    }
    return false;

}

     */


// get All users oline


/*

    public List <Users>getAllUsersOnline(){
        List <Users> listUsers=new ArrayList<>();
        //  SQLiteDatabase DB=this.getWritableDatabase();

        Cursor cursor =database.rawQuery( SELECTALL_USERS_ONLINE,null);
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                Users user=new Users();
                user.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_ID))));
                user.setIdonline(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_ID_ONLINE)));
                user.setPrefix(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_PREFIX_ONLINE)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_PHONE_ONLINE)));
                user.setActive(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_ACTIVE_ONLINE))));
                user.setVerified(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_VERIFIER_ONLINE))));
                user.setPreferences(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_PREFERENCE_ONLINE)));
                user.setDiscussions(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_DISCUSSIONS_ONLINE)));
                user.setLast_connection(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_LASTCONNECTION_ONLINE)));
                user.setLast_verification(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_LASTVERIFICATION_ONLINE)));
                user.setCreated_at(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_CREATEAT_ONLINE)));
                user.setUpdated_at(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_UPDATEAT_ONLINE)));
                user.setImage(cursor.getBlob(cursor.getColumnIndex(Dbusers.TABLE_IMAGE_ONLINE)));
                listUsers.add(user);
            }
        }
        return listUsers;

    }
*/


    //  get All Users
    public List<Users> getAllUserslocal() {
        List<Users> listUsers = new ArrayList<>();
        //  SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cursor = database.rawQuery(SELECTALL_USERS_LOCAL, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Users user = new Users();
                user.setId(cursor.getInt(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_ID)))));
                user.setIdonline(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_ID_ONLINE)));
                user.setPrefix(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_PREFIX_ONLINE)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_PHONE_ONLINE)));
                user.setUserName(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_NAME_ONLINE)));
                user.setActive(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_ACTIVE_ONLINE))));
                user.setVerified(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_VERIFIER_ONLINE))));
                user.setPreferences(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_PREFERENCE_ONLINE)));
                user.setLast_connection(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_LASTCONNECTION_ONLINE)));
                user.setLast_verification(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_LASTVERIFICATION_ONLINE)));
                user.setCreated_at(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_CREATEAT_ONLINE)));
                user.setUpdated_at(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_UPDATEAT_ONLINE)));
                user.setImage(cursor.getBlob(cursor.getColumnIndex(Dbusers.TABLE_IMAGE_ONLINE)));
                listUsers.add(user);
            }
        }
        return listUsers;

    }


    public Users getUserLocal(int iduser) {
        //Log.e("idusers","cursor.getString(cursor.getColumnIndex(DBClient.ACTIVE))");
        Users user = null;
        Cursor cursor = database.rawQuery(SELECTWHEREUSERID_LOCAL, new String[]{String.valueOf(iduser)});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                user = new Users();
                user.setId(cursor.getInt(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_ID)))));
                user.setIdonline(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_ID_ONLINE)));
                user.setPrefix(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_PREFIX_ONLINE)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_PHONE_ONLINE)));
                user.setUserName(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_NAME_ONLINE)));
                try{
                    Log.e("idusers","isactivation "+cursor.getString(cursor.getColumnIndex(Dbusers.USERS_ACTIVE_ONLINE)));
                    if(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_ACTIVE_ONLINE)).equalsIgnoreCase("false")){
                        user.setActive(0);
                    }else
                    if(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_ACTIVE_ONLINE)).equalsIgnoreCase("true")){
                        user.setActive(1);
                    }else
                       user.setActive(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_ACTIVE_ONLINE))));
                }catch (Exception e){

                }


                user.setVerified(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(Dbusers.USERS_VERIFIER_ONLINE))));
                user.setPreferences(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_PREFERENCE_ONLINE)));
                user.setLast_connection(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_LASTCONNECTION_ONLINE)));
                user.setLast_verification(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_LASTVERIFICATION_ONLINE)));
                user.setCreated_at(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_CREATEAT_ONLINE)));
                user.setUpdated_at(cursor.getString(cursor.getColumnIndex(Dbusers.TABLE_UPDATEAT_ONLINE)));
                user.setImage(cursor.getBlob(cursor.getColumnIndex(Dbusers.TABLE_IMAGE_ONLINE)));

            }
        }
        return user;

    }


    public Boolean updateUserLocal(int userid,String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Dbusers.USERS_ACTIVE_ONLINE, value);
        long resutl = database.update(TABLE_USERS_LOCAL, contentValues, Dbusers.USERS_ID + "=?", new String[]{String.valueOf(userid)});
        if (resutl == -1) {
            return false;
        } else {
            return true;
        }

    }
    //  get All Users
    /*
    public List <Users>getAllUsers_ONLINE_Projection(){
        List <Users> listUsers=new ArrayList<>();
        //  SQLiteDatabase DB=this.getWritableDatabase();
        String[] projection={USERS_ID,USERS_ID_ONLINE,USERS_PREFIX_ONLINE,USERS_PHONE_ONLINE ,USERS_ACTIVE_ONLINE , USERS_VERIFIER_ONLINE, TABLE_PREFERENCE_ONLINE, TABLE_DISCUSSIONS_ONLINE,TABLE_LASTCONNECTION_ONLINE ,TABLE_LASTVERIFICATION_ONLINE , TABLE_CREATEAT_ONLINE,TABLE_UPDATEAT_ONLINE};
        Cursor cursor =database.query(TABLE_USERS,projection,null,null,null,null,null);
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                Users user=new Users();
                user.setTokn(cursor.getString(1));
                user.setTokn(cursor.getString(1));
                listUsers.add(user);
            }
        }
        return listUsers;

    }
    public List <Users>getAllUsersProjection(){
        List <Users> listUsers=new ArrayList<>();
        //  SQLiteDatabase DB=this.getWritableDatabase();
        String[] projection={USERS_ID,USERS_NAME,USERS_TOKEN};
        Cursor cursor =database.query(TABLE_USERS,projection,null,null,null,null,null);
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                Users user=new Users();
                user.setTokn(cursor.getString(1));
                user.setTokn(cursor.getString(1));
                listUsers.add(user);
            }
        }
        return listUsers;

    }

     */
}
