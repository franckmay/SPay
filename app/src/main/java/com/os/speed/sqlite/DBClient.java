package com.os.speed.sqlite;

public class DBClient {
    public static final String SYNC_STATUS="sync";
    public static final int SYNC_STATUS_OK=0;
    public static final int SYNC_STATUS_FAILED=1;
    public static final String UI_UPDATE_BROADCAST="com.os.speed.uiupdatebroadcast";


    public static final String SERVER_URL="http://192.168.120.73/users";

    public static final String TABLE_MESSAGE_ONLINE="users";
    public static final String TABLE_CLIENT_LOCAL="client";

    // CHAMPS
    public static final String CLIENT_ID="id";
    public static final String CLIENT_ONLINE="idonline";
    public static final String DATETIME="datetime";
    public static final String DATEUPDATE="dateupdate";
    public static final String NAME="name";
    public static final String CODE="code";
    public static final String CODEUSSD="codeussd";
    public static final String ACTIVE="active";


    public static final String OPERATEUR="operateur";
    public static final String NUMERO="numero";
    public static final String TYPE="type";
    public static final String PREFIX="prefix";
    public static final String PHONE="phone";













    //  REQUETTES
   /* public static final String CREATE_TABLE_USERS_ONLINE="create Table "+TABLE_USERS_ONLINE+
            "("+USERS_ID+" INTEGER primary key autoincrement,"+USERS_ID_ONLINE+" TEXT,"+USERS_PREFIX_ONLINE+" TEXT,"+USERS_PHONE_ONLINE+" TEXT, "+USERS_NAME_ONLINE+" TEXT, "+USERS_TOKEN_ONLINE+" TEXT,   "+USERS_ACTIVE_ONLINE+" BOOLEAN,"+USERS_VERIFIER_ONLINE+" BOOLEAN,"+TABLE_PREFERENCE_ONLINE+" TEXT,"+TABLE_DISCUSSIONS_ONLINE+" TEXT,"+TABLE_LASTCONNECTION_ONLINE+" TEXT,"+TABLE_LASTVERIFICATION_ONLINE+" TEXT,"+TABLE_CREATEAT_ONLINE+" TEXT,"+TABLE_UPDATEAT_ONLINE+" TEXT)";
*/
    public static final String CREATE_TABLE_CLIENT_LOCAL="create Table "+TABLE_CLIENT_LOCAL+
            "("+CLIENT_ID+" INTEGER primary key autoincrement,"+ NAME+" TEXT,"+ CLIENT_ONLINE+" TEXT,"+DATETIME+" TEXT,"+DATEUPDATE+" TEXT,"+CODE+" TEXT,"+CODEUSSD+" TEXT, "+ACTIVE+" BOOLEAN, "+OPERATEUR+" TEXT, "+NUMERO+" TEXT UNIQUE, "+TYPE+" TEXT, "+PREFIX+" TEXT, "+PHONE+" TEXT)";


    /*   public static final String DROP_TABLE_USERS_ONLINE="drop Table if exists "+CREATE_TABLE_USERS_ONLINE;
    public static final String SELECTALL_USERS_ONLINE="select * from "+TABLE_USERS_ONLINE;
    public static final String SELECTWHEREPHONE_ONLINE="select * from "+TABLE_USERS_ONLINE+" where "+USERS_PHONE_ONLINE+"= ?";
*/


    public static final String DROP_TABLE_CLIENT_LOCAL="drop Table if exists "+CREATE_TABLE_CLIENT_LOCAL;
    public static final String SELECTALL_CLIENT_LOCAL="select * from "+TABLE_CLIENT_LOCAL;
    public static final String SELECTWHERECLIENTID_LOCAL="select * from "+TABLE_CLIENT_LOCAL+" where "+CLIENT_ID+"= ?";
    public static final String SELECTWHERECLIENTIDONLINE="select * from "+TABLE_CLIENT_LOCAL+" where "+CLIENT_ONLINE+"= ?";
    public static final String SELECTWHERECLIENTCODE="select * from "+TABLE_CLIENT_LOCAL+" where "+CODE+"= ?";








}
