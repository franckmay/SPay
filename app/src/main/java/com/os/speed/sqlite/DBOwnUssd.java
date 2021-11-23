package com.os.speed.sqlite;

public class DBOwnUssd {
    public static final String SYNC_STATUS="sync";
    public static final int SYNC_STATUS_OK=0;
    public static final int SYNC_STATUS_FAILED=1;
    public static final String UI_UPDATE_BROADCAST="com.os.speed.uiupdatebroadcast";


    public static final String SERVER_URL="http://192.168.120.73/users";


    public static final String TABLE_OWNUSSD_LOCAL="ownussd";

    // CHAMPS OWNUSSD
    public static final String OWNUSSD_ID="ussd_id";
    public static final String OWNUSSD_ONLINE="ussd_idonline";
    public static final String PREFIX="prefix";
    public static final String PHONE="phone";
    public static final String EXPIRATION_AT="expiration_at";
    public static final String OWNUSSDNAME="name";
    public static final String OWNUSSDCODE="code";
    public static final String OWNUSSDCODEUSSD="codeussd";
    public static final String OWNUSSDACTIVE="active";
    public static final String OWNUSSDOPERATEUR="operateur";
    public static final String OWNUSSDNUMERO="numero";
    public static final String OWNUSSDTYPE="type";
    public static final String DATETIME="datetime";
    public static final String DATEUPDATE="dateupdate";













    //  REQUETTES
   /* public static final String CREATE_TABLE_USERS_ONLINE="create Table "+TABLE_USERS_ONLINE+
            "("+USERS_ID+" INTEGER primary key autoincrement,"+USERS_ID_ONLINE+" TEXT,"+USERS_PREFIX_ONLINE+" TEXT,"+USERS_PHONE_ONLINE+" TEXT, "+USERS_NAME_ONLINE+" TEXT, "+USERS_TOKEN_ONLINE+" TEXT,   "+USERS_ACTIVE_ONLINE+" BOOLEAN,"+USERS_VERIFIER_ONLINE+" BOOLEAN,"+TABLE_PREFERENCE_ONLINE+" TEXT,"+TABLE_DISCUSSIONS_ONLINE+" TEXT,"+TABLE_LASTCONNECTION_ONLINE+" TEXT,"+TABLE_LASTVERIFICATION_ONLINE+" TEXT,"+TABLE_CREATEAT_ONLINE+" TEXT,"+TABLE_UPDATEAT_ONLINE+" TEXT)";
*/
    public static final String CREATE_TABLE_OWNUSSD_LOCAL="create Table "+TABLE_OWNUSSD_LOCAL+
            "("+OWNUSSD_ID+" INTEGER primary key autoincrement,"+ OWNUSSD_ONLINE+" TEXT,"+ OWNUSSDNAME+" TEXT,"+OWNUSSDCODE+" TEXT,"+OWNUSSDCODEUSSD+" TEXT,"+OWNUSSDACTIVE+" BOOLEAN,"+OWNUSSDOPERATEUR+" TEXT, "+OWNUSSDNUMERO+" TEXT, "+OWNUSSDTYPE+" TEXT, "+DATETIME+" TEXT, "+DATEUPDATE+" TEXT, "+PREFIX+" TEXT, "+PHONE+" TEXT, "+EXPIRATION_AT+" TEXT)";


    /*   public static final String DROP_TABLE_USERS_ONLINE="drop Table if exists "+CREATE_TABLE_USERS_ONLINE;
    public static final String SELECTALL_USERS_ONLINE="select * from "+TABLE_USERS_ONLINE;
    public static final String SELECTWHEREPHONE_ONLINE="select * from "+TABLE_USERS_ONLINE+" where "+USERS_PHONE_ONLINE+"= ?";
*/


    public static final String DROP_TABLE_OWNUSSD_LOCAL="drop Table if exists "+CREATE_TABLE_OWNUSSD_LOCAL;
    public static final String SELECTALL_OWNUSSD_LOCAL="select * from "+TABLE_OWNUSSD_LOCAL;
    public static final String SELECTWHEREOWNUSSDID_LOCAL="select * from "+TABLE_OWNUSSD_LOCAL+" where "+OWNUSSD_ID+"= ?";
    public static final String SELECTWHEREOWNUSSDIDONLINE="select * from "+TABLE_OWNUSSD_LOCAL+" where "+OWNUSSD_ONLINE+"= ?";
    public static final String SELECTWHEREOWNUSSDCODE="select * from "+TABLE_OWNUSSD_LOCAL+" where "+OWNUSSDCODE+"= ?";








}
