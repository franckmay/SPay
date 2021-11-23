package com.os.speed.sqlite;

public class Dbusers {
    public static final String SYNC_STATUS="sync";
    public static final int SYNC_STATUS_OK=0;
    public static final int SYNC_STATUS_FAILED=1;
    public static final String SERVER_URL="http://192.168.120.73/users";
    public static final String UI_UPDATE_BROADCAST="com.exemple.sunctest.uiupdatebroadcast";
   // public static final String TABLE_USERS_ONLINE="users";
    public static final String TABLE_USERS_LOCAL="userlocal";
    // CHAMPS
    public static final String USERS_ID="id";
    public static final String USERS_ID_ONLINE="idonline";
    public static final String USERS_PREFIX_ONLINE="prefix";
    public static final String USERS_PHONE_ONLINE="phone";
    public static final String USERS_NAME_ONLINE="users_name";
    public static final String USERS_TOKEN_ONLINE="usert_token";
    public static final String USERS_ACTIVE_ONLINE="active";
    public static final String USERS_VERIFIER_ONLINE="verified";
    public static final String TABLE_PREFERENCE_ONLINE="preferences";
    public static final String TABLE_DISCUSSIONS_ONLINE="discussions";
    public static final String TABLE_LASTCONNECTION_ONLINE="last_connection";
    public static final String TABLE_LASTVERIFICATION_ONLINE="last_verification";
    public static final String TABLE_CREATEAT_ONLINE="created_at";
    public static final String TABLE_UPDATEAT_ONLINE="updated_at";
    public static final String TABLE_IMAGE_ONLINE="image";
    //  REQUETTES
 /*   public static final String CREATE_TABLE_USERS_ONLINE="create Table "+TABLE_USERS_ONLINE+
            "("+USERS_ID+" INTEGER primary key autoincrement,"+USERS_ID_ONLINE+" TEXT,"+USERS_PREFIX_ONLINE+" TEXT,"+USERS_PHONE_ONLINE+" TEXT, "+USERS_NAME_ONLINE+" TEXT, "+USERS_TOKEN_ONLINE+" TEXT,   "+USERS_ACTIVE_ONLINE+" BOOLEAN,"+USERS_VERIFIER_ONLINE+" BOOLEAN,"+TABLE_PREFERENCE_ONLINE+" TEXT,"+TABLE_DISCUSSIONS_ONLINE+" TEXT,"+TABLE_LASTCONNECTION_ONLINE+" TEXT,"+TABLE_LASTVERIFICATION_ONLINE+" TEXT,"+TABLE_CREATEAT_ONLINE+" TEXT,"+TABLE_UPDATEAT_ONLINE+" TEXT)";
*/
    public static final String CREATE_TABLE_USERS_LOCAL="create Table "+TABLE_USERS_LOCAL+
            "("+USERS_ID+" INTEGER primary key autoincrement,"+USERS_ID_ONLINE+" TEXT,"+USERS_PREFIX_ONLINE+" TEXT,"+USERS_PHONE_ONLINE+" TEXT, "+USERS_NAME_ONLINE+" TEXT,   "+USERS_ACTIVE_ONLINE+" BOOLEAN,"+USERS_VERIFIER_ONLINE+" BOOLEAN,"+TABLE_PREFERENCE_ONLINE+" TEXT,"+TABLE_LASTCONNECTION_ONLINE+" TEXT,"+TABLE_LASTVERIFICATION_ONLINE+" TEXT,"+TABLE_CREATEAT_ONLINE+" TEXT,"+TABLE_UPDATEAT_ONLINE+" TEXT,"+TABLE_IMAGE_ONLINE+" BLOB)";

   // public static final String DROP_TABLE_USERS_ONLINE="drop Table if exists "+CREATE_TABLE_USERS_ONLINE;
  //  public static final String SELECTALL_USERS_ONLINE="select * from "+TABLE_USERS_ONLINE;
  //  public static final String SELECTWHEREPHONE_ONLINE="select * from "+TABLE_USERS_ONLINE+" where "+USERS_PHONE_ONLINE+"= ?";



    public static final String DROP_TABLE_USERS_LOCAL="drop Table if exists "+CREATE_TABLE_USERS_LOCAL;
    public static final String SELECTALL_USERS_LOCAL="select * from "+TABLE_USERS_LOCAL;
    public static final String SELECTWHEREPHONE_LOCAL="select * from "+TABLE_USERS_LOCAL+" where "+USERS_PHONE_ONLINE+"= ?";

    public static final String SELECTWHEREUSERID_LOCAL="select * from "+TABLE_USERS_LOCAL+" where "+USERS_ID+"= ?";








}
