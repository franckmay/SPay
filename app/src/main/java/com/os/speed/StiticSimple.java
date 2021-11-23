package com.os.speed;

import android.graphics.Bitmap;

public class StiticSimple {
    // public static final String NOMCOMPAGNY = "MSGBRD";
    public static final int LENTUSERCODE = 4;
    public static final int LENTUSSDCODE = 5;
    public static final int REQUEST_EXCECUTE_USSD = 1004;
    public static final String CODE_RECONNAISSANCE = "code";
    public static final String NUMEROACHILLE = "657385146";
    public static final String NUMEROACHILLEMTN = "672778594";
    public static final String MONTANT = "100";
    public static final String MONTANTSCAN = "150";
    public static final String NOMCOMPAGNY = "+237691268128";
    public static final String TAG = "MAXADS";
    public static final String DATABASE = "Arintech.db";
    public static final int DATABASE_VERSION = 1;
    public static final int SPLASH_SCREEN = 5000;
    public static final int IMAGE_GALLERY_REQUEST = 111;
    public static final int REQUEST_TAKE_PHOTO = 440;
    public static final int REQUEST_SMS_PERMISION = 555;
    public static final int REQUEST_CORD_PERMISSION = 332;
    public static final int DOCUMENT_REQUEST = 553;
    public static final int PERMISSION_CAMERA = 221;
    public static final int PERMISSION_READ_CONTACT = 100;
    public static final int PERMISSION_EXTERNAL_STORAGE = 222;
    public static Bitmap IMAGE_BITMAP;
  //  public static final String urlUsers = "http://192.168.137.214:8888/users/list-user";

    public static final int  LAUNCH_SECOND_ACTIVITY = 666;
    public static final int LENGHT_CAMEROON = 9;
    public static final int LENGHT_CODEENTREPRISE = 4;

    public static final int MULTIPLE_PERMISSIONS = 122;


    public static String MyId = "123";
    // broadcast
    public static String senBroadcastExepmple = "com.arintech.maxad.Broacast";
    public static String SMS = "android.provider.Telephony.SMS_RECEIVED";
    //public static final String urlPhoneNUmber = "http://192.168.137.214:8888/users/list-user";
    // public static final String urlRacine="http://192.168.88.245:8888/";
    //public static final String urlRacine = "http://192.168.8.107:8888/";
    public  static final String IPSERVER="93.113.206.199";
    public static final String urlRacine="http://"+IPSERVER+":8888/";
    public static final String urlgetCode = urlRacine + "registrer/check-registration";
    public static final String urlMaxAdd = urlRacine + "registrer/check-registration";

    public static final String urlvalidecode = urlRacine + "registrer/check-code";
    public static final String urlimageAndName = urlRacine + "registrer/images";
    public static final String urllanguage = urlRacine + "registrer/language";
    // public static final String urllistuser=urlRacine+"clients/listclient";
    public static final String urllistuser = urlRacine + "clients/listclient";
    public static final String urllistussdown = urlRacine + "clients/listownussd";
    public static final String urlcreateussd = urlRacine + "clients/createussd";
    public static final String urlactiveussd = urlRacine + "clients/activeussd";
    public static final String urlactiveuserlocal = urlRacine + "users/activeuserlocal";
    public static final String urldeleteussd = urlRacine + "clients/deleteclient";
    public static final String urlallpub = urlRacine + "adverts/listpub";
    public static final String urluserdata = urlRacine + "users/getuserdata";

}
