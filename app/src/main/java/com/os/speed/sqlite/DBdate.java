package com.os.speed.sqlite;

public class DBdate {
    public static final String SYNC_STATUS="sync";
    public static final int SYNC_STATUS_OK=0;
    public static final int SYNC_STATUS_FAILED=1;
    public static final String TABLE_DATE_LOCAL="datejour";

    // CHAMPS
    public static final String DATE_ID="id";
    public static final String DATETIME="datetime";
    public static final String CURRENTDATE="curentdate";


    //  REQUETTES
   /* public static final String CREATE_TABLE_USERS_ONLINE="create Table "+TABLE_USERS_ONLINE+
            "("+USERS_ID+" INTEGER primary key autoincrement,"+USERS_ID_ONLINE+" TEXT,"+USERS_PREFIX_ONLINE+" TEXT,"+USERS_PHONE_ONLINE+" TEXT, "+USERS_NAME_ONLINE+" TEXT, "+USERS_TOKEN_ONLINE+" TEXT,   "+USERS_ACTIVE_ONLINE+" BOOLEAN,"+USERS_VERIFIER_ONLINE+" BOOLEAN,"+TABLE_PREFERENCE_ONLINE+" TEXT,"+TABLE_DISCUSSIONS_ONLINE+" TEXT,"+TABLE_LASTCONNECTION_ONLINE+" TEXT,"+TABLE_LASTVERIFICATION_ONLINE+" TEXT,"+TABLE_CREATEAT_ONLINE+" TEXT,"+TABLE_UPDATEAT_ONLINE+" TEXT)";
*/
    public static final String CREATE_TABLE_DATE_LOCAL="create Table "+TABLE_DATE_LOCAL+
            "("+DATE_ID+" INTEGER primary key autoincrement,"+DATETIME+" TEXT,"+CURRENTDATE+" TEXT)";




    public static final String DROP_TABLE_DATE_LOCAL="drop Table if exists "+CREATE_TABLE_DATE_LOCAL;
    public static final String SELECTALL_CLIENT_LOCAL="select * from "+TABLE_DATE_LOCAL;
    public static final String SELECTWHEREDATEID_LOCAL="select * from "+TABLE_DATE_LOCAL+" where "+DATE_ID+"= ?";
   // public static final String SELECTWHERECLIENTIDONLINE="select * from "+TABLE_DATE_LOCAL+" where "+DATE_ID+"= ?";
   // public static final String SELECTWHERECLIENTCODE="select * from "+TABLE_DATE_LOCAL+" where "+DATE_ID+"= ?";



}
