package com.os.speed;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.os.speed.Recycler.PubAdapter;
import com.os.speed.Recycler.ScrollingLinearLayoutManager;
import com.os.speed.broadreceiver.Networkreceiver;
import com.os.speed.modele.Client;
import com.os.speed.modele.CurentDate;
import com.os.speed.modele.OwnUssd;
import com.os.speed.modele.Pub;
import com.os.speed.modele.Users;
import com.os.speed.modele.Ussd;
import com.os.speed.service.BackgroundService;
import com.os.speed.service.CLientOnlineservice;
import com.os.speed.sqlite.DBHelper;
import com.os.speed.sqlite.DBPub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.os.speed.StiticSimple.CODE_RECONNAISSANCE;
import static com.os.speed.StiticSimple.LENTUSERCODE;
import static com.os.speed.StiticSimple.MONTANT;
import static com.os.speed.StiticSimple.MONTANTSCAN;
import static com.os.speed.StiticSimple.MULTIPLE_PERMISSIONS;
import static com.os.speed.StiticSimple.NOMCOMPAGNY;
import static com.os.speed.StiticSimple.NUMEROACHILLE;
import static com.os.speed.StiticSimple.NUMEROACHILLEMTN;
import static com.os.speed.StiticSimple.REQUEST_EXCECUTE_USSD;
import static com.os.speed.StiticSimple.SMS;
import static com.os.speed.StiticSimple.urlMaxAdd;
import static com.os.speed.staticclass.checkNetworkConnection;
import static com.os.speed.staticclass.isNumeric;
import static com.os.speed.staticclass.isURLReachable;
import static com.os.speed.staticclass.recursekeys;

public class Home extends AppCompatActivity {
    private static final long DEFAULT_SLIDE_INTERVAL = 1;
    BroadcastReceiver broadcastReceivers;


    String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    }; // Here i used multiple permission check
    public Button bt_send;
    public Button bt_receive;
    private static final int PERMISSION_CODE = 1000;
    private static final int REQUEST_TAKE_PHOTO = 1001;
    DBHelper dbHelper;
    Networkreceiver networkreceiver;
    BroadcastReceiver broadcastReceiver;
    BackgroundService backgroundService;
    RecyclerView rv;
    PubAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    ScrollingLinearLayoutManager scrollManager;


    Timer timer;
    int cursor = 0;
    String TAG = "application";
    Context contex;

    class AdvertisementTimerTask extends TimerTask {
        private int count;

        public AdvertisementTimerTask(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            if (cursor < count) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //linearLayoutManager.scrollToPosition(cursor);
                        Log.e("scroller", "scrollss" + String.valueOf(cursor));
                        rv.scrollToPosition(cursor);
                        cursor++;
                    }
                });

            } else {
                cursor = 0;
            }
        }
    }

    /////////////////////////////
    Dialog dialog1;
    Dialog dialog2;
    String codedepayement;
    String codeussdi;
    Users userocal;

    ////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.prim));
        }


        LinearLayout layout =(LinearLayout)findViewById(R.id.background);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        //Log.e("sdkligne",String.valueOf(sdk));
        if(sdk <= Build.VERSION_CODES.N) {
            //layout.setBackground(ContextCompat.getDrawable(this, R.drawable.ready));
            layout.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.speed2));
        }

        //////////////////// procecus de paiement /////////////////////////////////////////////////////
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.waiting));
        dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.custom_dialog_insert);
        dialog2 = new Dialog(this);
        dialog2.setContentView(R.layout.custom_dialog_user);

        Button getcode = dialog1.findViewById(R.id.getcode);
       // TextView numeroachille = dialog2.findViewById(R.id.numeroachille);
      //  numeroachille.setText(NUMEROACHILLEMTN);
        EditText code = dialog1.findViewById(R.id.code);
        ImageView sumitcode = dialog1.findViewById(R.id.sumitcode);
        //Log.e("isll", "user " + user.getPrefix());


        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                dialog2.show();
            }
        });


        sumitcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Execution du code de payement
                codedepayement = code.getText().toString();
                // code.setText("");
                dialog1.dismiss();
                //progressDialog.show();

                ////////////////////////////

                progressDialog.show();
                ///   //////////////////////////////////////////////////////////////////////////////////////////////////////////
                sendtoActiveuser(userocal.getIdonline(), userocal.getPhone(), userocal.getPrefix(), codedepayement);
                /////////////////////////////////

            }
        });
        LinearLayout paieorange = dialog2.findViewById(R.id.paieorange);
        paieorange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), getString(R.string.nombreussmax), Toast.LENGTH_LONG).show();
                //String ussd = client.getCodeussd() + "*" + client.getNumero() + "*" + montant + "#";
                dialog2.dismiss();
                codeussdi = "#150*1*1*" +NUMEROACHILLE  + "*" + MONTANTSCAN + "#";
                initCall();
            }
        });
      /*  LinearLayout paiemtn = dialog2.findViewById(R.id.paiemtn);
        paiemtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                codeussdi = "*126#";
                initCall();
            }
        });*/


        /////////////////////////////////////fin procecus de paiement
        contex = this;
        bt_send = findViewById(R.id.send);
        bt_receive = findViewById(R.id.receive);
        rv = (RecyclerView) findViewById(R.id.pub);
        Boolean permission = checkPermissions();
        // create Dbhelper;
        dbHelper = new DBHelper(Home.this);
        userocal = dbHelper.getUserLocal(1);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanButton();

            }
        });
        bt_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.os.speed.Reception.class));
            }
        });
        //   networkreceiver = new Networkreceiver(Home.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Home.this.startForegroundService(new Intent(this, BackgroundService.class));
                Home.this.startService(new Intent(this, BackgroundService.class));
            }catch (Exception e){
                Log.e("erreurs",e.getMessage());

            }


        } else {
            Home.this.startService(new Intent(this, BackgroundService.class));
        }

        rv.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    cursor = linearLayoutManager.findLastVisibleItemPosition();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.scrollToPosition(Integer.MAX_VALUE);
        //  displayPeers(tabownUsse);
        adapter = new PubAdapter(this);
        rv.setAdapter(adapter);
        ///////////////////
       /* scrollManager = new ScrollingLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false, 100);
        rv.setLayoutManager(scrollManager);
        rv.setAdapter(adapter);*/
        /////////////////////////


        //rv.smoothScrollBy(5,0);


        getAllPub();
        broadcastReceivers = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("RECEPTIONDELA", "RECEPTION DE LA PUB");
                getAllPub();
            }
        };
       /* Pub pub1=new Pub();
        pub1.setId(1);
        pub1.setIdonline(1);
        pub1.setImage(1);
        pub1.setMessage("ddd");
        addOwn(pub1);
        addOwn(pub1);
        addOwn(pub1);
        addOwn(pub1);
*/
     /*   FirebaseMessaging.getInstance().subscribeToTopic("publicite")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                      //  Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });*/


    }

    private void sendtoActiveuser(String idonline, String phone, String prefix, String codedepayement) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isURLReachable(getApplicationContext(), urlMaxAdd)) {
                        activeuserlocal(idonline, phone, prefix, codedepayement);


                    } else {
                        /// Toast.makeText(Reception.this, "getString(R.string.erreur_connextion)", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        showToast(getString(R.string.erreur_connextion));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }

    private void activeuserlocal(String idonline, String phone, String prefix, String codedepayement) {

        new CLientOnlineservice(this).activeuserlocal(idonline, phone, prefix, codedepayement, new CLientOnlineservice.OnCallsBack() {
            @Override
            public void onUpoadSuccess(String value) {
                // progressDialog.dismiss();
                // Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {
                try {
                    String value = response.getString("value");
                    //Log.e("jesss",value);
                    dbHelper.updateUserLocal(1, "1");
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //

            }

            @Override
            public void onUpoadSuccess(JSONArray response) {


            }

            @Override
            public void onUpoadSuccess(List<?> listPhone) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.erreur_connextion), Toast.LENGTH_SHORT).show();


            }
        });
    }


    /////////////////////////////
    public void initCall() {


        Intent callInn = new Intent(Intent.ACTION_CALL, ussdTocallableUri(codeussdi));
        //     startActivity(callInn);
        startActivityForResult(callInn, REQUEST_EXCECUTE_USSD);


        // finish();
        // progressDialog.dismiss();
        /*Toast.makeText(this, " Succesful", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+Uri.encode("#150#")));
        startActivity(intent);*/

        //Intent call=new Intent(Intent.ACTION_DIAL);


    }

    public void showToast(final String toast) {
        runOnUiThread(() -> Toast.makeText(Home.this, toast, Toast.LENGTH_SHORT).show());
    }

    public void appenUrl(int position) {
        Log.e("position", String.valueOf(position));
        Pub pub = adapter.getPub(position);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pub.getUrl_destination()));
        try {
            startActivity(browserIntent);
        } catch (Exception e) {

        }
    }

    public class SwipeTask extends TimerTask {
        public void run() {
            rv.post(() -> {
                int nextPage = (scrollManager.findFirstVisibleItemPosition() + 1) % adapter.getItemCount();
                rv.smoothScrollToPosition(nextPage);
            });
        }

    }

    private void stopScrollTimer() {
        if (null != swipeTimer) {
            swipeTimer.cancel();
        }
        if (null != swipeTask) {
            swipeTask.cancel();
        }
    }

    private Timer swipeTimer;
    private SwipeTask swipeTask;

    private void resetScrollTimer() {
        stopScrollTimer();
        swipeTask = new SwipeTask();
        swipeTimer = new Timer();
    }

    private void playCarousel() {
        resetScrollTimer();
        swipeTimer.schedule(swipeTask, 0, DEFAULT_SLIDE_INTERVAL);
    }


    public void getAllPub() {
        List<Pub> lisussd = dbHelper.getAllPub();
        displayPeers(lisussd);
    }

    public void displayPeers(List<Pub> tabOwnUssd) {
        adapter.addAll(tabOwnUssd);
    }


    public void addOwn(Pub ownUssd) {
        adapter.add(ownUssd);
    }

    public void getALclientfromOnline() {
        CurentDate datDbsqlite = dbHelper.getSpecificDate(1);
        //    Date curent=new Date();
     /*   Date datebd = datDbsqlite.getCurentdate();
        //   Log.e("isll","fffffff");
        //  Log.e("datcurent",curent.toString());
        Log.e("dattebd", datebd.toString());
        // SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //  boolean state = DateTimeUtils.isToday(datebd);
        boolean state = true;
        Log.e("dattebd", String.valueOf(state));
        if (!state) {*/


        //   }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Users user = dbHelper.getUserLocal(1);
                    Log.e("isll", "user " + user.getPrefix());
                    if (user != null)
                        //  if (checkNetworkConnection(getApplicationContext())) {
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

    public void getALpubOnline() {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Users user = dbHelper.getUserLocal(1);
                    Log.e("isll", "user " + user.getPrefix());
                    if (user != null)
                        //  if (checkNetworkConnection(getApplicationContext())) {
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

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // permissions granted.
                    //       getCallDetails(); // Now you call here what ever you want :)
                    //    creatAlfolder(this);
                    // initialie();
                } else {
                    String perStr = "";
                    for (String per : permissions) {
                        perStr += "\n" + per;
                    }   // permissions list of don't granted permission
                }
                return;
            }
        }
    }

    ////////////////////////////////////
    public void ScanButton() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(com.os.speed.Home.this);
        intentIntegrator.setBeepEnabled(true);
        // intentIntegrator.setTorchEnabled(true);
        intentIntegrator.setPrompt(getString(R.string.forflash));
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();


    }

    String value;


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
                    Users user = dbHelper.getUserLocal(1);
                    if (user.isActive() == 1) {// si  l utilisateur est actif
                        //textView.setText(intentResult.getContents());
                        value = intentResult.getContents();
                        /// recuperation de l id dans la bd distante

                        Client client = dbHelper.getSpecificClientWithCode(value);
                        if (client != null && client.isActive()) {
                            withEditText(client);
                        } else {
                            custumToast(getString(R.string.codenonreconu));

                        }




                    } else {
                        // declancher le mode de paiement dd
                        dialog1.show();
                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    }


                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    //////////////////////////////////////
    public ProgressDialog progressDialog;

    public void initCall(String ussd) {
        Intent callInn = new Intent(Intent.ACTION_CALL, ussdTocallableUri(ussd));
        //     startActivity(callInn);
        startActivityForResult(callInn, REQUEST_TAKE_PHOTO);

        progressDialog.dismiss();

        // finish();
        // progressDialog.dismiss();
        /*Toast.makeText(this, " Succesful", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+Uri.encode("#150#")));
        startActivity(intent);*/

        //Intent call=new Intent(Intent.ACTION_DIAL);


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
//////////////////////////////////////////////////////////////////

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
                Log.e("numbersddd", "debut insertion sqlite");
                try {
                    //    if (response.length() > 0) {
                    dbHelper.deleteAll();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject row = response.getJSONObject(i);
                        Pub pub = new Pub();
                        pub.setIdonline(row.getString("_id"));
                        pub.setTitle(row.getString("title"));
                        pub.setDescription(row.getString("description"));
                        pub.setUrl_destination(row.getString("image"));
                        pub.setUrl_image(row.getString("url"));
                        pub.setDate_publication(String.valueOf(row.getString("date_publication")));
                        pub.setDuration(row.getString("duration"));
                        pub.setPeople_count(Integer.parseInt(row.getString("people_count")));
                        pub.setViews_count(Integer.parseInt(row.getString("views_count")));
                        pub.setStatus(row.getString("status"));
                        pub.setAmount(Integer.parseInt(row.getString("amount")));
                        pub.setGender(row.getString("gender"));
                        String minage = recursekeys(row, "minage");
                        String maxage = recursekeys(row, "maxage");
                        pub.setMinage(Integer.parseInt(minage));
                        pub.setMaxage(Integer.parseInt(maxage));
                        pub.setCity(row.getString("city"));
                        pub.setCreated_at(row.getString("created_at"));
                        pub.setUpdated_at(row.getString("updated_at"));
                       /* Client newClient=dbHelper.getSpecificClientWithCode(client.getCode());
                        if(newClient!=null) {
                            dbHelper.updateClient_online(newClient);
                        }else{*/
                        dbHelper.insertPub(pub);

                        //}

                    }
                    // dbHelper.updateCurentdate(1);


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
                    dbHelper.updateCurentdate(1);


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

    public void withEditText(Client client) {
        // Log.e("ussd", client.getCodeussd());
        //  Log.e("ussd", client.getNumero());


        // AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
        builder.setTitle(getString(R.string.entre_le_montant));
        builder.setIcon(R.drawable.speed);
        builder.setMessage(getString(R.string.payto) + " " + client.getName());
        final EditText input = new EditText(Home.this);
        // final TextView title = new TextView(Home.this);
        //  title.setText(client.getName());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        // builder.setView(title);
        builder.setView(input);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isNumeric(input.getText().toString())) {
                    String montant = input.getText().toString();
                    String ussd = client.getCodeussd() + "*" + client.getNumero() + "*" + montant + "#";
                    //  Log.e("ussdd",ussd);
                    progressDialog = new ProgressDialog(contex);
                    progressDialog.setMessage(getString(R.string.waiting));
                    progressDialog.show();
                    initCall(ussd);
                    // initCall(client,value, input.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "this value " + input.getText().toString() + " is not numerique", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();


    }

////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////// open insert code//////////////////////////////

    ///////////////////////////////////////
    private final BroadcastReceiver SmSreceiveCode = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

       /*  Bundle b = intent.getExtras();
       Toast.makeText(context,"jjjjjj",Toast.LENGTH_SHORT).show();
        if(intent.getAction().equalsIgnoreCase(senBroadcastExepmple)){
            Log.e("SMS", "ACTION EXEMPLE");
             String msg=b.getString("msg");
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();

            Log.e("SMS", "ACTION EXEMPLE");
        }*/
            if (intent.getAction().equalsIgnoreCase(SMS)) {
                //sms recived
                Bundle myBundle = intent.getExtras();
                SmsMessage[] messages = null;
                String from = "";
                String strMessage = "";
                if (myBundle != null) {
                    Object[] pdus = (Object[]) myBundle.get("pdus");

                    messages = new SmsMessage[pdus.length];

                    for (int i = 0; i < pdus.length; i++) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            String format = myBundle.getString("format");
                            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                        } else {
                            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        }


                        from += messages[i].getOriginatingAddress();

                        strMessage += messages[i].getMessageBody();
                        strMessage += "\n";

                    }
                    Log.e("from", from);
                    if (from.equalsIgnoreCase(NOMCOMPAGNY)) {
                        String s = strMessage;


                        s = s.substring(s.indexOf("(") + 1);
                        s = s.substring(0, s.indexOf(")"));
                        codedepayement = s;
                        if (!codedepayement.isEmpty()) {
                            //////////////////////////////////////////////////
                            if (codedepayement.length()==LENTUSERCODE) {

                               /* dialog1.dismiss();
                                progressDialog.show();*/
                                ////////////////////////////

                                sendtoActiveuser(userocal.getIdonline(), userocal.getPhone(), userocal.getPrefix(), codedepayement);

                                /////////////////////////////////
                            }
                        }


                    }

                    ///////////////////////////////////

                    Toast.makeText(context, "From Message", Toast.LENGTH_LONG).show();
                    Toast.makeText(context, from, Toast.LENGTH_LONG).show();
                    Toast.makeText(context, "containt message", Toast.LENGTH_LONG).show();
                    Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                    ////////////////////////////


                }

            }
        }
    };

    /////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStart() {
        super.onStart();
        int itemCountOfAdvertisements = rv.getAdapter().getItemCount();
        registerReceiver(broadcastReceivers, new IntentFilter(DBPub.UI_UPDATE_BROADCASTPUB));
        // playCarousel();
        timer = new Timer();
        timer.schedule(new AdvertisementTimerTask(itemCountOfAdvertisements), 0, 2 * 1000);
        IntentFilter filter = new IntentFilter(SMS);
        registerReceiver(SmSreceiveCode, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (broadcastReceivers != null) {
            // unregister receiver
            unregisterReceiver(broadcastReceivers);
        }
        if(SmSreceiveCode!=null){
            unregisterReceiver(SmSreceiveCode);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkreceiver, intentFilter);
        registerReceiver(broadcastReceivers, new IntentFilter(DBPub.UI_UPDATE_BROADCASTPUB));
        IntentFilter filter = new IntentFilter(SMS);
        registerReceiver(SmSreceiveCode, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkreceiver != null) {
            // unregister receiver
            unregisterReceiver(networkreceiver);
        }
        if (broadcastReceivers != null) {
            // unregister receiver
            try {
                unregisterReceiver(broadcastReceivers);
            } catch (Exception e) {

            }

        }
        //  unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.action_language:
                Toast.makeText(Home.this,"Action clicked",Toast.LENGTH_LONG).show();break;

        }
        return super.onOptionsItemSelected(item);
    }
*/
}