package com.os.speed;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.os.speed.broadreceiver.Networkreceiver;
import com.os.speed.modele.Client;
import com.os.speed.modele.CurentDate;
import com.os.speed.modele.Users;
import com.os.speed.service.CLientOnlineservice;
import com.os.speed.sqlite.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.os.speed.StiticSimple.urlMaxAdd;
import static com.os.speed.sqlite.DBClient.UI_UPDATE_BROADCAST;
import static com.os.speed.staticclass.checkNetworkConnection;
import static com.os.speed.staticclass.isURLReachable;

public class ScanQrs extends AppCompatActivity {
    private TextView textView;
    //   public Button button;
    public ImageView image;
    String value;
    private static final int PERMISSION_CODE = 1000;
    private static final int REQUEST_TAKE_PHOTO = 1001;
    DBHelper dbHelper;
    BroadcastReceiver broadcastReceiver;
    Networkreceiver networkreceiver;


    PendingIntent myPendingIntent;
    AlarmManager alarmManager;
    BroadcastReceiver myBroadcastReceiver;
    Calendar firingCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrs);

        // create Dbhelper;
        dbHelper = new DBHelper(com.os.speed.ScanQrs.this);


  /*      Client client = new Client();
        client.setCodeussd("#150*47*2#");
        client.setCode("code2");
        client.setActive(true);
        client.setDateTime("12/24/1222");
        client.setIdonline("ee1245");


       Log.e("code inserier",String.valueOf(dbHelper.insertCLient(client))) ;*/

      /*  Handler handler =new Handler();
        final Runnable r=new Runnable() {
            @Override
            public void run() {

            }
        };*/
        // handler.postDelayed(r,1000);
        Thread thread=new Thread() {
            @Override
            public void run() {
                super.run();
                getALclientfromOnline();
            }
        };
        thread.start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_DENIED) {
                //permission non autorisée, faire une demande
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE};

                //afficher un popup pour le resultat de la demande de permission
                requestPermissions(permission, PERMISSION_CODE);


            } else
                ScanButton();
        }


        // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);

        image = findViewById(R.id.imageView);
        //button = findViewById(R.id.button2);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.CALL_PHONE)
                                    == PackageManager.PERMISSION_DENIED) {
                        //permission non autorisée, faire une demande
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE};

                        //afficher un popup pour le resultat de la demande de permission
                        requestPermissions(permission, PERMISSION_CODE);


                    } else
                        ScanButton();
                }
            }
        });
// 148672 Messa medongo
     /*   networkreceiver = new Networkreceiver(ScanQrs.this);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getALclientfromOnline();
            }
        };*/

/*
        firingCal= Calendar.getInstance();
        firingCal.set(Calendar.HOUR, 11); // At the hour you want to fire the alarm
        firingCal.set(Calendar.MINUTE, 10); // alarm minute
        firingCal.set(Calendar.SECOND, 0); // and alarm second
        long intendedTime = firingCal.getTimeInMillis();

        registerMyAlarmBroadcast();

       // alarmManager.set( AlarmManager.RTC_WAKEUP, intendedTime , AlarmManager.INTERVAL_DAY , myPendingIntent );
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, myPendingIntent);
       // alarmManager.set(AlarmManager.RTC_WAKEUP,intendedTime,myPendingIntent);
*/

    }



/*
    private static final String TAG ="SCAN";

    private void registerMyAlarmBroadcast()
    {
        Log.i(TAG, "Going to register Intent.RegisterAlramBroadcast");

        //This is the call back function(BroadcastReceiver) which will be call when your
        //alarm time will reached.
        myBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.i(TAG,"BroadcastReceiver::OnReceive()");
                Toast.makeText(context, "Your Alarm is there", Toast.LENGTH_LONG).show();
            }
        };

        registerReceiver(myBroadcastReceiver, new IntentFilter("com.alarm.example") );
        myPendingIntent = PendingIntent.getBroadcast( this, 0, new Intent("com.alarm.example"),0 );
        alarmManager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
    }
    private void UnregisterAlarmBroadcast()
    {
        alarmManager.cancel(myPendingIntent);
        getBaseContext().unregisterReceiver(myBroadcastReceiver);
    }

*/


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkreceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkreceiver != null) {
            // unregister receiver
            unregisterReceiver(networkreceiver);
        }
        //  unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(UI_UPDATE_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    public void ScanButton() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(true);
        // intentIntegrator.setTorchEnabled(true);
        intentIntegrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            progressDialog.dismiss();
        } else {
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (intentResult != null) {
                if (intentResult.getContents() == null) {
                    //  textView.setText("Cancelled");
                } else {
                    //textView.setText(intentResult.getContents());
                    value = intentResult.getContents();
                    initCall(value);

                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public ProgressDialog progressDialog;

    public void initCall(String value) {


        Log.e("value", value);
        Client client = dbHelper.getSpecificClientWithCode(value);
        if (client != null && client.isActive()) {
            //
            Intent callInn = new Intent(Intent.ACTION_CALL, ussdTocallableUri(client.getCodeussd()));
            //     startActivity(callInn);
            startActivityForResult(callInn, REQUEST_TAKE_PHOTO);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.waiting));
            progressDialog.show();


            // finish();
            // progressDialog.dismiss();
        /*Toast.makeText(this, " Succesful", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+Uri.encode("#150#")));
        startActivity(intent);*/

            //Intent call=new Intent(Intent.ACTION_DIAL);
        } else {
            //  Toast.makeText(this, " code non reconue", Toast.LENGTH_SHORT).show();
            custumToast(" code non reconue");

        }

    }

    public Uri ussdTocallableUri(String ussd) {
        String uriString = "";
        if (!ussd.startsWith("tel:"))
            uriString += "tel:";
        for (char c : ussd.toCharArray()) {
            if (c == '#')
                uriString += Uri.encode("#");
            else
                uriString += c;
        }
        return Uri.parse(uriString);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //methode appelée lors du choix d'accepter ou refus des permissions par l'utilisateur
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //la permission a été accordée
                    ScanButton();
                } else {
                    //permission refusée
                    Toast.makeText(this, "permission refusée", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    public void custumToast(String text) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toastlayout));
        ImageView imageView = (ImageView) view.findViewById(R.id.imagetoast);
        TextView textView = (TextView) view.findViewById(R.id.testtoast);
        textView.setText(text);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void saveClientToLocal(Client client) {
        //if (checkNetworkConnection(getApplicationContext())) {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isURLReachable(getApplicationContext(), urlMaxAdd)){
                        sendCodeInbdOnline("237", "672778594");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }

    public void getALclientfromOnline() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CurentDate datDbsqlite = dbHelper.getSpecificDate(1);
                    //    Date curent=new Date();
                    Date datebd = datDbsqlite.getCurentdate();
                    //   Log.e("isll","fffffff");
                    //  Log.e("datcurent",curent.toString());
                    Log.e("dattebd", datebd.toString());
                    // SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    //  boolean state = DateTimeUtils.isToday(datebd);
                    boolean state =true;
                    Log.e("dattebd", String.valueOf(state));
                    if (!state) {
                        Users user = dbHelper.getUserLocal(1);
                        Log.e("isll", "user " + user.getPrefix());
                        if (user != null)
                            // if (checkNetworkConnection(getApplicationContext())) {
                            if (isURLReachable(getApplicationContext(), urlMaxAdd)){
                                sendCodeInbdOnline(user.getPrefix(), user.getPhone());
                            }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
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
                Log.e("numbersddd", "debut insertion sqlite");
                try {
                //    if (response.length() > 0) {
                        dbHelper.deleteAll();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject row = response.getJSONObject(i);
                            Client client = new Client();
                            client.setIdonline(row.getString("_id"));
                            client.setDateTime(row.getString("created_at"));
                            client.setDateupdate(row.getString("updated_at"));
                            client.setActive(Boolean.valueOf(row.getString("active")));
                            client.setName(row.getString("name"));
                            client.setCode(row.getString("code"));
                            client.setCodeussd(row.getString("codeussd"));
                       /* Client newClient=dbHelper.getSpecificClientWithCode(client.getCode());
                        if(newClient!=null) {
                            dbHelper.updateClient_online(newClient);
                        }else{*/
                            dbHelper.insertCLient(client);

                            //}

                        }
                     dbHelper. updateCurentdate(1);


                 //   }
                    ////////////////////
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("numbersddd", "fin insertion sqlite");
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