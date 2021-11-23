package com.os.speed.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.os.speed.modele.Client;
import com.os.speed.modele.OwnUssd;
import com.os.speed.modele.Pub;
import com.os.speed.modele.Users;
import com.os.speed.sqlite.DBHelper;
import com.os.speed.sqlite.DBPub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.os.speed.StiticSimple.urlMaxAdd;
import static com.os.speed.staticclass.checkNetworkConnection;
import static com.os.speed.staticclass.isURLReachable;
import static com.os.speed.staticclass.recursekeys;
import static com.os.speed.staticclass.showNotifications;

public class BackgroundService extends Service {
    DBHelper dbHelper;
    Handler handler = new Handler();
    Handler handlerpub = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //

        // create Dbhelper;
        dbHelper = new DBHelper(getApplicationContext());

        // handler.postDelayed(serviceaction,5000);
        serviceaction.run();
        servicepub.run();
        modificationuserandussdcreate.run();
        serviceownussd.run();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private Runnable modificationuserandussdcreate = new Runnable() {// modification des champ ussd et du client status
        @Override
        public void run() {
            getALModificationUsers();
            handlerpub.postDelayed(this, 10000);
        }
    };
    private Runnable servicepub = new Runnable() {
        @Override
        public void run() {
            getALpubOnline();
            handlerpub.postDelayed(this, 70000);
        }
    };

    private Runnable serviceaction = new Runnable() {
        @Override
        public void run() {
            getALclientfromOnline();
            handler.postDelayed(this, 10000);
        }
    };
    private Runnable serviceownussd = new Runnable() {
        @Override
        public void run() {
            getOWNussd();
            handler.postDelayed(this, 10000);
        }
    };

    public void getALpubOnline() {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Users user = dbHelper.getUserLocal(1);
                    Log.e("isllpub", "user " + user.getPrefix());
                    if (user != null)
                        // if (checkNetworkConnection(getApplicationContext())) {
                        if (isURLReachable(getApplicationContext(), urlMaxAdd)) {
                            getAllPubInbdOnline(user.getPrefix(), user.getPhone());
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }
    public void getALModificationUsers() {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Users user = dbHelper.getUserLocal(1); // rechercher l utilisateur si son champ est à 1 sinon ça ne vaux pas la peine
                    Log.e("isllpub","lats "+ user.isActive());
                    if (user != null&&user.isActive()==1)
                        if (isURLReachable(getApplicationContext(), urlMaxAdd)) {
                            getAllModificationUsers(user.getPrefix(), user.getPhone());
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }


    private void getAllModificationUsers(String prefix, String phone) {

        new CLientOnlineservice(this).getAllModificationUsers(prefix, phone, new CLientOnlineservice.OnCallsBack() {
            @Override
            public void onUpoadSuccess(String value) {
                // progressDialog.dismiss();
                // Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onUpoadSuccess(JSONObject response) {


                try {
                  //  Log.e("isllpub","lats "+ response.toString());
                    String value = response.getString("value");
                    if(Boolean.parseBoolean(value)) {
                        String active = recursekeys(response,"active");
                        String expiration_at = recursekeys(response,"expiration_at");
                        if(active.equalsIgnoreCase("false")){
                            dbHelper. updateUserLocal(1,"0");
                        }else{
                            dbHelper. updateUserLocal(1,"1");
                        }

                       /* Log.e("isllpub","existe value "+ active);
                        Log.e("isllpub","existe value "+ expiration_at);*/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onUpoadSuccess(JSONArray response) {


            }

            @Override
            public void onUpoadSuccess(List<?> listPhone) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                Log.e("isllpub", "user errer");
//                progressDialog.dismiss();
                // Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();

            }
        });
    }
















    private void getAllPubInbdOnline(String prefix, String phone) {
      /*  progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.show();*/
        new CLientOnlineservice(this).getAllPubOnline(prefix, phone, new CLientOnlineservice.OnCallsBack() {
            @Override
            public void onUpoadSuccess(String value) {
                // progressDialog.dismiss();
                // Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onUpoadSuccess(JSONObject response) {


            }

            @Override
            public void onUpoadSuccess(JSONArray response) {
                // Log.e("numbersddd", "debut insertion sqlite");
               // Log.e("numbersddd", response.toString());
                List<Pub> listpub = new ArrayList<>();
                try {
                    //    if (response.length() > 0) {


                    for (int i = 0; i < response.length(); i++) {
                        JSONObject row = response.getJSONObject(i);
                        Pub pub = new Pub();


                        try {
                            pub.setIdonline(row.getString("_id"));
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            pub.setTitle(row.getString("title"));
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }
                        try {
                            pub.setDescription(row.getString("description"));
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }



                        try {
                            pub.setUrl_destination(row.getString("url_destination"));
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }



                        try {
                            pub.setUrl_image(row.getString("url_image"));
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            pub.setDate_publication(String.valueOf(row.getString("date_publication")));
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            pub.setDuration(row.getString("duration"));
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            pub.setPeople_count(Integer.parseInt(row.getString("people_count")));
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            pub.setViews_count(Integer.parseInt(row.getString("views_count")));
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            pub.setStatus(row.getString("status"));
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            pub.setAmount(Integer.parseInt(row.getString("amount")));
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            pub.setGender(row.getString("gender"));
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }
                        String minage = recursekeys(row, "min");
                        String maxage = recursekeys(row, "max");
                        pub.setMinage(Integer.parseInt(minage));
                        pub.setMaxage(Integer.parseInt(maxage));
                        pub.setCity(row.getString("city"));
                        pub.setCreated_at(row.getString("created_at"));
                        pub.setUpdated_at(row.getString("updated_at"));
                        listpub.add(pub);

                    }

                       /* Client newClient=dbHelper.getSpecificClientWithCode(client.getCode());
                        if(newClient!=null) {
                            dbHelper.updateClient_online(newClient);
                        }else{*/
                    List<Pub> listpubbd = new ArrayList<>();
                    listpubbd = dbHelper.getAllPub();
                    if (listpubbd.size() > 0) {
                        for (Pub pubd : listpubbd) {
                            Pub pubonline = isexist(pubd, listpub);
                            if (pubonline != null) {// si la pub existe ecore soit modifier ou ne rien faire
                                dbHelper.updatePubl(pubonline);
                                listpub.remove(pubonline);
                            } else {// sion supprimer
                                dbHelper.deletePub(pubd);
                            }
                        }

                    }
                    for (Pub pu : listpub) {
                        if (dbHelper.insertPub(pu)) {
                            showNotifications(pu.getTitle(), pu.getDescription(), pu.getUrl_image(), getApplicationContext());
                            // creation de la notification

                        }
                    }


                  /*  dbHelper.deleteAllPub();
                    Pub pu = dbHelper.getSpecificPub(row.getString("_id"));
                    if (pu == null) {
                        if (dbHelper.insertPub(pub)) {
                            Log.e("numbersddd", "insertion d la pub");


                            showNotifications(pub.getTitle(), pub.getDescription(), pub.getUrl(), getApplicationContext());
                            // creation de la notification

                        }
                    }*/


                    //}


                  //  Log.e("envoie", "insertion d 1 jjjjjjjjjjjjjjjjjjdddjla pub");
                    sendBroadcast(new Intent(DBPub.UI_UPDATE_BROADCASTPUB));
                    // dbHelper.updateCurentdate(1);


                    //   }
                    ////////////////////
                } catch (JSONException e) {
                    e.printStackTrace();
                }

               // Log.e("numbersddd", "fin insertion sqlite");
            }

            @Override
            public void onUpoadSuccess(List<?> listPhone) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                Log.e("isllpub", "user errer");
//                progressDialog.dismiss();
                // Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Pub isexist(Pub pubd, List<Pub> listpub) {
        for (Pub puonline : listpub) {
            if (pubd.getIdonline().equalsIgnoreCase(puonline.getIdonline())) {
                return puonline;
            }
        }
        return null;
    }






    /////////////////////////////////////////////////////////////////////////////////////////////
    public void getOWNussd() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Users user = dbHelper.getUserLocal(1);
                    // Log.e("isll", "user " + user.getPrefix());
                    if (user != null)
                        // if (checkNetworkConnection(getApplicationContext())) {
                        if (isURLReachable(getApplicationContext(), urlMaxAdd)) {
                            getownUssd(user.getPrefix(), user.getPhone());
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }

    private void getownUssd(String prefix, String phone) {
      /*  progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.show();*/
        new CLientOnlineservice(this).getOwnussd(prefix, phone, new CLientOnlineservice.OnCallsBack() {
            @Override
            public void onUpoadSuccess(String value) {
                // progressDialog.dismiss();
                // Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onUpoadSuccess(JSONObject response) {

            }

            @Override
            public void onUpoadSuccess(JSONArray response) {

                try {
                    //    if (response.length() > 0) {
                   // dbHelper.deleteAll();
                 //   Log.e("numbersddd", "debut updat Ussd own  "+response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject row = response.getJSONObject(i);


                        /////////////////////////////////////////////////////////////////////////////////////////
                        String online_id ="",name ="",prefix ="",phone ="",code ="",active ="",codeussd ="",operateur ="",type ="",numero ="",expiration_at ="",created_at ="",updated_at ="";

                        try {
                            online_id = row.getString("_id");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            name = row.getString("name");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            prefix= row.getString("prefix");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            phone = row.getString("phone");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            code = row.getString("code");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }
                        // String codeactivation = row.getString("codeactivation");

                        try {
                            active = row.getString("active");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            codeussd = row.getString("codeussd");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }  try {
                            operateur = row.getString("operateur");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            numero = row.getString("numero");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            type = row.getString("type");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            expiration_at = row.getString("expiration_at");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            created_at = row.getString("created_at");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            updated_at = row.getString("updated_at");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }


                        //////////////////////////////
                        OwnUssd ussd = new OwnUssd();
                        ussd.setUssd_idonline(online_id);
                        ussd.setName(name);
                        ussd.setPhone(phone);
                        ussd.setPrefix(prefix);
                        ussd.setExpiration_at(expiration_at);
                        ussd.setCode(code);
                        ussd.setCodeussd(codeussd);
                        ussd.setActive(Boolean.valueOf(active));
                        ussd.setOperateur(operateur);
                        ussd.setNumero(numero);
                        ussd.setType(type);
                        ussd.setDatetime(created_at);
                        ussd.setDateupdate(updated_at);

                       /* Client newClient=dbHelper.getSpecificClientWithCode(client.getCode());
                        if(newClient!=null) {
                            dbHelper.updateClient_online(newClient);
                        }else{*/
                       // dbHelper.insertCLient(client);

                        //}
                        if(dbHelper.getSpecificOwnUssdWithCode(ussd.getCode())!=null){
                            dbHelper.updateOwnUssd( ussd);
                        }else{
                            dbHelper.insertUssd( ussd);
                        }

                      //  Log.e("numbersddd", "debut updat Ussd own inwwxx  "+ String.valueOf( dbHelper.updateOwnUssd( ussd)));


                    }
                   // dbHelper.updateCurentdate(1);


                    //   }
                    ////////////////////
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //   Log.e("numbersddd", "fin insertion sqlite");
            }

            @Override
            public void onUpoadSuccess(List<?> listPhone) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
//                progressDialog.dismiss();
                // Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();

            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void getALclientfromOnline() {
       /* CurentDate datDbsqlite = dbHelper.getSpecificDate(1);
        //    Date curent=new Date();
        Date datebd = datDbsqlite.getCurentdate();
        //   Log.e("isll","fffffff");
        //  Log.e("datcurent",curent.toString());
        Log.e("dattebdddddddd", datebd.toString());
        // SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //  boolean state = DateTimeUtils.isToday(datebd);
        boolean state = true;
        Log.e("dattebd", String.valueOf(state));
        if (!state) {*/


        // }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Users user = dbHelper.getUserLocal(1);
                    // Log.e("isll", "user " + user.getPrefix());
                    if (user != null)
                        // if (checkNetworkConnection(getApplicationContext())) {
                        if (isURLReachable(getApplicationContext(), urlMaxAdd)) {
                            sendCodeInbdOnline(user.getPrefix(), user.getPhone());
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }

    private void sendCodeInbdOnline(String prefix, String phone) {
      /*  progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.show();*/
        new CLientOnlineservice(this).getAllClientOnline(prefix, phone, new CLientOnlineservice.OnCallsBack() {
            @Override
            public void onUpoadSuccess(String value) {
                // progressDialog.dismiss();
                // Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onUpoadSuccess(JSONObject response) {

                /*try {
                    String token=response.getString("token");
                    String prefix=response.getString("prefix");
                    String phone=response.getString("phone");
                    String avatar=response.getString("avatar");
                    //Log.e("numbersddd", response.toString());
                    //   JWT jwt=new JWT(token);
                    //   Log.e("token", token);
                    //Log.e("phone", phone);
                    progressDialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString("token", token);
                    bundle.putString("prefix", prefix);
                    bundle.putString("phone", phone);
                    bundle.putString("avatar", avatar);
                    NavHostFragment.findNavController(CodeVerificationFragment.this)
                            .navigate(R.id.action_codeVerificationFragment_to_definirUtilisateurFragment,bundle);


                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                //

            }

            @Override
            public void onUpoadSuccess(JSONArray response) {
                // Log.e("numbersddd", "debut insertion sqlite");
                try {
                    //    if (response.length() > 0) {
                    dbHelper.deleteAll();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject row = response.getJSONObject(i);


                        /////////////////////////////////////////////////////////////////////////////////////////
                        String online_id ="",name ="",prefix ="",phone ="",code ="",active ="",codeussd ="",operateur ="",type ="",numero ="",expiration_at ="",created_at ="",updated_at ="";

                        try {
                            online_id = row.getString("_id");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            name = row.getString("name");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            prefix= row.getString("prefix");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            phone = row.getString("phone");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            code = row.getString("code");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }
                        // String codeactivation = row.getString("codeactivation");

                        try {
                            active = row.getString("active");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            codeussd = row.getString("codeussd");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }  try {
                            operateur = row.getString("operateur");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            numero = row.getString("numero");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            type = row.getString("type");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            expiration_at = row.getString("expiration_at");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            created_at = row.getString("created_at");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }

                        try {
                            updated_at = row.getString("updated_at");
                        }catch (Exception e) {
                            Log.e("Erreur",e.getMessage());
                        }


                        //////////////////////////////


                        Client client = new Client();
                        client.setIdonline(online_id);
                        client.setPhone(phone);
                        client.setPrefix(prefix);
                        client.setDateTime(created_at);
                        client.setDateupdate(updated_at);
                        client.setActive(Boolean.valueOf(active));
                        client.setName(name);
                        client.setCode(code);
                        client.setCodeussd(codeussd);
                        client.setOperateur(operateur);
                        client.setType(type);
                        client.setNumero(numero);















                       /* Client newClient=dbHelper.getSpecificClientWithCode(client.getCode());
                        if(newClient!=null) {
                            dbHelper.updateClient_online(newClient);
                        }else{*/
                        dbHelper.insertCLient(client);

                        //}

                    }
                    dbHelper.updateCurentdate(1);


                    //   }
                    ////////////////////
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //   Log.e("numbersddd", "fin insertion sqlite");
            }

            @Override
            public void onUpoadSuccess(List<?> listPhone) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
//                progressDialog.dismiss();
                // Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();

            }
        });
    }

}
