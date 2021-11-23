package com.os.speed.sqlite;

public class DBPub {
    public static final String SYNC_STATUS="sync";
    public static final int SYNC_STATUS_OK=0;
    public static final int SYNC_STATUS_FAILED=1;
    public static final String UI_UPDATE_BROADCASTPUB="com.os.speed.Home.uiupdatebroadcastpub";


    public static final String SERVER_URL="http://192.168.120.73/users";


    public static final String TABLE_PUB_LOCAL="publicite";

    // CHAMPS
    public static final String PUB_ID="id";
    public static final String IDAVERT_ID="id_advertiser";
    public static final String TITLE="title";
    public static final String DESCRIPTION="description";
    public static final String IMAGE="image";
    public static final String URL="url";
    public static final String DATE_PUBLICATION="date_publication";
    public static final String DURATION="duration";
    public static final String PEOPLE_COUNT="people_count";


    public static final String VIEWS_ACOUNT="views_count";
    public static final String STATUS="status";
    public static final String AMOUNT="amount";
    public static final String GENRE="gender";
    public static final String MINAGE="minage";
    public static final String MAXAGE="maxage";
    public static final String CITY="city";
    public static final String CREATEAT="created_at";
    public static final String UPDATEAT="updated_at";



    //  REQUETTES
   /* public static final String CREATE_TABLE_USERS_ONLINE="create Table "+TABLE_USERS_ONLINE+
            "("+USERS_ID+" INTEGER primary key autoincrement,"+USERS_ID_ONLINE+" TEXT,"+USERS_PREFIX_ONLINE+" TEXT,"+USERS_PHONE_ONLINE+" TEXT, "+USERS_NAME_ONLINE+" TEXT, "+USERS_TOKEN_ONLINE+" TEXT,   "+USERS_ACTIVE_ONLINE+" BOOLEAN,"+USERS_VERIFIER_ONLINE+" BOOLEAN,"+TABLE_PREFERENCE_ONLINE+" TEXT,"+TABLE_DISCUSSIONS_ONLINE+" TEXT,"+TABLE_LASTCONNECTION_ONLINE+" TEXT,"+TABLE_LASTVERIFICATION_ONLINE+" TEXT,"+TABLE_CREATEAT_ONLINE+" TEXT,"+TABLE_UPDATEAT_ONLINE+" TEXT)";
*/
    public static final String CREATE_TABLE_PUB_LOCAL="create Table "+TABLE_PUB_LOCAL+
            "("+PUB_ID+" INTEGER primary key autoincrement,"+ IDAVERT_ID+" TEXT,"+ TITLE+" TEXT,"+DESCRIPTION+" TEXT,"+IMAGE+" TEXT,"+URL+" TEXT,"+DATE_PUBLICATION+" TEXT, "+DURATION+" INTEGER, "+PEOPLE_COUNT+" INTEGER, "+VIEWS_ACOUNT+" INTEGER, "+STATUS+" BOOLEAN, "+AMOUNT+" INTEGER, "+GENRE+" TEXT, "+MINAGE+" INTEGER, "+MAXAGE+" INTEGER, "+CITY+" TEXT, "+CREATEAT+" TEXT, "+UPDATEAT+" TEXT)";


    /*   public static final String DROP_TABLE_USERS_ONLINE="drop Table if exists "+CREATE_TABLE_USERS_ONLINE;
    public static final String SELECTALL_USERS_ONLINE="select * from "+TABLE_USERS_ONLINE;
    public static final String SELECTWHEREPHONE_ONLINE="select * from "+TABLE_USERS_ONLINE+" where "+USERS_PHONE_ONLINE+"= ?";
*/


    public static final String DROP_TABLE_PUB_LOCAL="drop Table if exists "+CREATE_TABLE_PUB_LOCAL;
    public static final String SELECTALL_PUB_LOCAL="select * from "+TABLE_PUB_LOCAL;
    public static final String SELECTWHEREPUBID_LOCAL="select * from "+TABLE_PUB_LOCAL+" where "+PUB_ID+"= ?";
    public static final String SELECTWHEREPUBIDONLINE="select * from "+TABLE_PUB_LOCAL+" where "+IDAVERT_ID+"= ?";
    //public static final String SELECTWHEREPUBCODE="select * from "+TABLE_PUB_LOCAL+" where "+CODE+"= ?";








}
