package com.os.speed;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.os.speed.Recycler.MyOwnAdapter;
import com.os.speed.Recycler.PubAdapter1;
import com.os.speed.Recycler.ScrollingLinearLayoutManager;
import com.os.speed.broadreceiver.NotificationReceiver;
import com.os.speed.modele.Client;
import com.os.speed.modele.OwnUssd;
import com.os.speed.modele.Pub;
import com.os.speed.modele.Users;
import com.os.speed.modele.Ussd;
import com.os.speed.service.CLientOnlineservice;
import com.os.speed.sqlite.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.os.speed.StiticSimple.CODE_RECONNAISSANCE;
import static com.os.speed.StiticSimple.LAUNCH_SECOND_ACTIVITY;
import static com.os.speed.StiticSimple.LENGHT_CAMEROON;
import static com.os.speed.StiticSimple.LENGHT_CODEENTREPRISE;
import static com.os.speed.StiticSimple.LENTUSERCODE;
import static com.os.speed.StiticSimple.MONTANT;
import static com.os.speed.StiticSimple.NOMCOMPAGNY;
import static com.os.speed.StiticSimple.NUMEROACHILLE;
import static com.os.speed.StiticSimple.NUMEROACHILLEMTN;
import static com.os.speed.StiticSimple.REQUEST_EXCECUTE_USSD;
import static com.os.speed.StiticSimple.SMS;
import static com.os.speed.StiticSimple.urlMaxAdd;
import static com.os.speed.staticclass.CHANNEL_ID;
import static com.os.speed.staticclass.NOTIFICATION_CHANNEL_ID;
import static com.os.speed.staticclass.getBitmapFromURL;
import static com.os.speed.staticclass.isURLReachable;

public class Reception extends AppCompatActivity {

    List<OwnUssd> tabownUssd = new ArrayList<>();
    RecyclerView rv;
    MyOwnAdapter adapter;
    ImageView bt_add, btnBack;
    Button submit;
    EditText phonenumber, copagnie_name,shortnumber;
    // String[] operateur = new String[]{"Orange", "Mtn", "Nextel", "Camtel", "yoome", "Express Union"};
    String[] operateur = new String[]{"Orange"};
    String[] entreprise = new String[]{"client", "entreprise"};
    Spinner spinner;
    String choiceoperateur, prefix, phone, choiceentreprise;
    SharedPreferences preferences;
    AlertDialog dialog;
    DBHelper dbHelper;


    RecyclerView rvd;
    PubAdapter1 adapter1;
    LinearLayoutManager linearLayoutManager;
    ScrollingLinearLayoutManager scrollManager;

    Timer timer;
    int cursor = 0;
    BroadcastReceiver broadcastReceivers;
    //  private NotificationManagerCompat notificationManager;
    private Ussd ussd;
    String codeussdi;

    class AdvertisementTimerTasks extends TimerTask {
        private int count;

        public AdvertisementTimerTasks(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            if (cursor < count) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rvd.scrollToPosition(cursor);
                        cursor++;
                    }
                });

            } else {
                cursor = 0;
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        int itemCountOfAdvertisements = rvd.getAdapter().getItemCount();
        timer = new Timer();
        timer.schedule(new AdvertisementTimerTasks(itemCountOfAdvertisements), 0, 2 * 1000);
      /*  IntentFilter filter = new IntentFilter(SMS);
        registerReceiver(SmSreceiveCode, filter);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
      //  unregisterReceiver(SmSreceiveCode);
    }

   // Dialog dialog1;
    Dialog dialog2;
    Dialog dialog3;// suppression
    Dialog dialogchoix;// suppression
    String codedepayement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.prim));
        }
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/
        setContentView(R.layout.activity_reception);
        LinearLayout layout =(LinearLayout)findViewById(R.id.backgroundreception);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk <= Build.VERSION_CODES.N) {
            //layout.setBackground(ContextCompat.getDrawable(this, R.drawable.ready));
            layout.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.speed2));
        }

       // dialog1 = new Dialog(this);
        ussd = new Ussd();
       // dialog1.setContentView(R.layout.custom_dialog_insert);
        dialog2 = new Dialog(this);
        dialogchoix = new Dialog(this);
        dialog2.setContentView(R.layout.custom_dialog);
        dialogchoix.setContentView(R.layout.custom_dialog_choix);

        LinearLayout entreprise = dialogchoix.findViewById(R.id.entreprise);
        LinearLayout personnel = dialogchoix.findViewById(R.id.personnel);
        personnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ussd.setType("client");
                dialogchoix.dismiss();

                showDialogPesonnel();


            }
        });
        entreprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  if (ownUssddelete != null) {
                Log.e("jepassd", "ACTION PUB  lala je passe");
                ussd.setType("entreprise");
                dialogchoix.dismiss();
                showDialog();

                //    }

            }
        });

         /*  if (adapter.getItemCount() < 7) {
                    showDialog();

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.nombreussmax), Toast.LENGTH_LONG).show();
                }*/


        ////
        dialog3 = new Dialog(this);
        dialog3.setContentView(R.layout.dialog_delete_ussd);
        Button bt_ok = dialog3.findViewById(R.id.bt_ok);
        TextView numerotxt = findViewById(R.id.numero);
        TextView codeussd = findViewById(R.id.codeussd);
        TextView operateur = findViewById(R.id.operateur);
        Log.e("buill", String.valueOf(Build.VERSION.SDK_INT));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Call some material design APIs here
            numerotxt.setText(getString(R.string.numero_telephone));
            codeussd.setText(getString(R.string.codeussd));
            operateur.setText(getString(R.string.operateur));
        } else {
            // Implement this feature without material design
            numerotxt.setText(getString(R.string.numero));
            codeussd.setText(getString(R.string.codeuss));
            operateur.setText(getString(R.string.operateu));
        }


        Button bt_cancel = dialog3.findViewById(R.id.bt_cancel);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ownUssddelete != null) {
                    dialog3.dismiss();
                    deleteUssd(ownUssddelete);
                }

            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ownUssddelete != null) {
                    dialog3.dismiss();
                    ownUssddelete = null;
                }

            }
        });
        ///////////////////////////////////


     //   Button getcode = dialog1.findViewById(R.id.getcode);
        //TextView numeroachille = dialog2.findViewById(R.id.numeroachille);
        // numeroachille.setText(NUMEROACHILLEMTN);
    //    EditText code = dialog1.findViewById(R.id.code);
     //   ImageView sumitcode = dialog1.findViewById(R.id.sumitcode);
      /*  getcode.setOnClickListener(new View.OnClickListener() {
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
                code.setText("");
                ussd.setCode(codedepayement);
                dialog1.dismiss();
                progressDialog.show();

                ////////////////////////////

                sendtocreateussd();

                /////////////////////////////////

            }
        });*/
        LinearLayout paieorange = dialog2.findViewById(R.id.paieorange);
        paieorange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), getString(R.string.nombreussmax), Toast.LENGTH_LONG).show();
                //String ussd = client.getCodeussd() + "*" + client.getNumero() + "*" + montant + "#";
                dialog2.dismiss();
                codeussdi = "#150*1*1*" + NUMEROACHILLE + "*" + MONTANT + "#";
                initCall();
            }
        });
        /*LinearLayout paiemtn = dialog2.findViewById(R.id.paiemtn);
        paiemtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                codeussdi = "*126#";
                initCall();
            }
        });*/

/////////////////////////////////////////////////////////////////////////////////////////////
        //  notificationManager = NotificationManagerCompat.from(this);
        rv = (RecyclerView) findViewById(R.id.rvownussd);
        rvd = (RecyclerView) findViewById(R.id.pub);
        bt_add = findViewById(R.id.bt_add);
        btnBack = findViewById(R.id.btn_back);
        dbHelper = new DBHelper(com.os.speed.Reception.this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.waiting));
        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        //  SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        phone = preferences.getString("phone", null);
        prefix = preferences.getString("prefix", null);


        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());


        //  displayPeers(tabownUsse);
        adapter = new MyOwnAdapter(this, tabownUssd);
        rv.setAdapter(adapter);
        getAllUssd();
        //initCall(getApplicationContext());
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creatVoid();

                dialogchoix.show();
              /*  if (adapter.getItemCount() < 7) {
                    showDialog();

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.nombreussmax), Toast.LENGTH_LONG).show();
                }*/

            }
        });
        // LinearLayout entreprise = findViewById(R.id.entreprise);
        //  LinearLayout personnel

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //////////////////////////////////////////////
        rvd.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        rvd.setLayoutManager(linearLayoutManager);
        rvd.setItemAnimator(new DefaultItemAnimator());
        rvd.scrollToPosition(Integer.MAX_VALUE);

        //  displayPeers(tabownUsse);

        adapter1 = new PubAdapter1(this);
        rvd.setAdapter(adapter1);
        ///////////////////////////////////
        getAllPub();
        broadcastReceivers = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                getAllPub();
            }
        };

    }

    public void sendtocreateussd() {

        progressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Users user = dbHelper.getUserLocal(1);
                    if (user != null) {
                        // if (checkNetworkConnection(getApplicationContext())) {78/

                        if (isURLReachable(getApplicationContext(), urlMaxAdd)) {
                           /* Log.e("jepassd", "ACTION PUB  lala je passe");
                            Log.e("jepassd", ussd.getType());
                            Log.e("jepassd", "rrii");*/
                            if (ussd.getType().equals("client")) {
                              //  Log.e("jepassd", "ACTION PUB  lala je passe");
                                createUssd(ussd.getPhonenumber(), ussd.getChoiceoperateur(), ussd.getPrefix(), ussd.getPhone(), 1, ussd.getCode(), "rine","rien");
                            } else if (ussd.getType().equals("entreprise")) {
                                if (!ussd.getCopagnie_name().isEmpty())
                                    createUssd(ussd.getPhonenumber(), ussd.getChoiceoperateur(), ussd.getPrefix(), ussd.getPhone(), 2, ussd.getCode(), ussd.getCopagnie_name(),ussd.getRadicale());
                                else
                                    Toast.makeText(Reception.this, getString(R.string.canbeempty), Toast.LENGTH_LONG).show();
                            }

                            progressDialog.dismiss();
                        } else {
                            /// Toast.makeText(Reception.this, "getString(R.string.erreur_connextion)", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();
                            showToast(getString(R.string.erreur_connextion));

                        }
                    } else {
                        // Toast.makeText(getApplicationContext(), getString(R.string.erreur_bd), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        showToast(getString(R.string.erreur_bd));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    /////////////////////////////

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


    public void initCall() {


        Intent callInn = new Intent(Intent.ACTION_CALL, ussdTocallableUri(codeussdi));
        //     startActivity(callInn);


        startActivityForResult(callInn, REQUEST_EXCECUTE_USSD);

        progressDialog.dismiss();

        // finish();
        // progressDialog.dismiss();
        /*Toast.makeText(this, " Succesful", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+Uri.encode("#150#")));
        startActivity(intent);*/

        //Intent call=new Intent(Intent.ACTION_DIAL);


    }

    ///////////////////////


    public void getAllPub() {
        List<Pub> lisussd = dbHelper.getAllPub();

        displayPeersPub(lisussd);
    }

    public void displayPeersPub(List<Pub> tabOwnUssd) {
     //   Log.e("RECEPTIONDELA", "RECEPTION DE LA PUB allllll  " + tabOwnUssd.size());
        adapter1.addAll(tabOwnUssd);
    }

    public void showToast(final String toast) {
        runOnUiThread(() -> Toast.makeText(Reception.this, toast, Toast.LENGTH_SHORT).show());
    }

    private void showDialog() {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_ussd, null);
        phonenumber = view.findViewById(R.id.ed_numero);
        shortnumber = view.findViewById(R.id.shortnumber);
        copagnie_name = view.findViewById(R.id.ed_name);
        submit = view.findViewById(R.id.bt_submit);
        spinner = view.findViewById(R.id.spinner_operateur);


        alert.setView(view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, operateur);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, entreprise);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choiceoperateur = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creatVoid();
                if (!phonenumber.getText().toString().isEmpty()&&!shortnumber.getText().toString().isEmpty()) {
                    //  progressDialog.setMessage(getString(R.string.waiting));
                    //   progressDialog.show();
                    // show second dialog
                    if (phonenumber.getText().toString().length() >= LENGHT_CODEENTREPRISE) {

                        ussd.setRadicale(shortnumber.getText().toString());
                        ussd.setPhone(phone);
                        ussd.setPrefix(prefix);
                        ussd.setChoiceoperateur(choiceoperateur);
                        ussd.setPhonenumber(phonenumber.getText().toString());
                        ussd.setCopagnie_name(copagnie_name.getText().toString());
                        dialog.dismiss();
                       // dialog1.show();yyyyyyyyyyyyyyyyyy
                        codedepayement = "000000";
                        ussd.setCode(codedepayement);
                      //  progressDialog.show();

                        ////////////////////////////

                        sendtocreateussd();

                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.numero_incorrect), Toast.LENGTH_SHORT).show();


                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.canbeempty), Toast.LENGTH_SHORT).show();

            }
        });

    }


    public ProgressDialog progressDialog;


    private void showDialogPesonnel() {

        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_ussd_personnel, null);
        phonenumber = view.findViewById(R.id.ed_numero);
        submit = view.findViewById(R.id.bt_submit);
        spinner = view.findViewById(R.id.spinner_operateur);


        alert.setView(view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, operateur);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, entreprise);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choiceoperateur = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creatVoid();
                if (!phonenumber.getText().toString().isEmpty()) {
                    //  progressDialog.setMessage(getString(R.string.waiting));
                    //   progressDialog.show();
                    // show second dialog
                    if (phonenumber.getText().toString().length() >= LENGHT_CAMEROON) {
                        ussd.setPhone(phone);
                        ussd.setPrefix(prefix);
                        ussd.setChoiceoperateur(choiceoperateur);
                        ussd.setPhonenumber(phonenumber.getText().toString());
                        ussd.setCopagnie_name("");
                        dialog.dismiss();
                       // dialog1.show();
                        codedepayement = "000000";
                        ussd.setCode(codedepayement);
                      //  Log.e("jepassd", "ACTION PUB  lala je passe");
                       // Toast.makeText(getApplicationContext(), "ACTION PUB  lala je passe", Toast.LENGTH_SHORT).show();
                       // progressDialog.show();ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff
                        sendtocreateussd();
                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.numero_incorrect), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.canbeempty), Toast.LENGTH_SHORT).show();

            }
        });

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void createUssd(String phonenumber, String choiceoperateur, String prefix, String phone, int type, String code, String copagnie_name,String radicale) {

        new CLientOnlineservice(this).creatUssdOnline(phonenumber, choiceoperateur, prefix, phone, type, code, copagnie_name,radicale, new CLientOnlineservice.OnCallsBack() {
            @Override
            public void onUpoadSuccess(String value) {
                // progressDialog.dismiss();
                // Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onUpoadSuccess(JSONObject response) {
                try {

                    String online_id = response.getString("_id");
                    String user_id = response.getString("user_id");
                    String name = response.getString("name");
                    String code = response.getString("code");
                    String codeussd = response.getString("codeussd");
                    String active = response.getString("active");
                    String operateur = response.getString("operateur");
                    String numero = response.getString("numero");
                    String type = response.getString("type");
                    String created_at = response.getString("created_at");
                    String updated_at = response.getString("updated_at");
                    //      Log.e("hhhhhfffffffffffffffffffh","lsjdfqdddddddddddddddddddddddddlfmq");
                    OwnUssd ussd = new OwnUssd();
                    ussd.setUssd_idonline(online_id);
                    ussd.setName(name);
                    ussd.setCode(code);
                    ussd.setCodeussd(codeussd);
                    ussd.setActive(Boolean.valueOf(active));
                    ussd.setOperateur(operateur);
                    ussd.setNumero(numero);
                    ussd.setType(type);
                    ussd.setDatetime(created_at);
                    ussd.setDateupdate(updated_at);
                    //Log.e("hhhhhfffffffffffffffffffh","5555555555555555555dddddddddddddddlfmq");
                    OwnUssd usd = dbHelper.getSpecificOwnUssdWithCode(code);
                    if (usd == null) {
                        if (dbHelper.insertUssd(ussd)) {
                            //   Log.e("hhhhhfffffffffffffffffffh","22222222222222222222222ddddddddddddddlfmq");
                            getAllUssd();
                            // Log.e("hhhhhfffffffffffffffffffh","l222ddddddddddddddlfmq");
                        }
                    } else {
                        Log.e("hhhhhfffffffffffffffffffh", "22222222222222222222222ddddddddddddddlfmq");
                    }

                    progressDialog.dismiss();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //

            }

            @Override
            public void onUpoadSuccess(JSONArray response) {

            //    Log.e("hhhhhfffffffffffffffffffh", "liste sdddesss");
                dbHelper.deleteAllUssd();
            //    Log.e("hhhhhfffffffffffffffffffh", "liste sdddesss");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject row = response.getJSONObject(i);
                        String online_id = "", name = "", prefix = "", phone = "", code = "", active = "", codeussd = "", operateur = "", type = "", numero = "", expiration_at = "", created_at = "", updated_at = "";

                        try {
                            online_id = row.getString("_id");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }

                        try {
                            name = row.getString("name");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }

                        try {
                            prefix = row.getString("prefix");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }

                        try {
                            phone = row.getString("phone");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }

                        try {
                            code = row.getString("code");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }
                        // String codeactivation = row.getString("codeactivation");

                        try {
                            active = row.getString("active");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }

                        try {
                            codeussd = row.getString("codeussd");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }
                        try {
                            operateur = row.getString("operateur");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }

                        try {
                            numero = row.getString("numero");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }

                        try {
                            type = row.getString("type");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }

                        try {
                            expiration_at = row.getString("expiration_at");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }

                        try {
                            created_at = row.getString("created_at");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }

                        try {
                            updated_at = row.getString("updated_at");
                        } catch (Exception e) {
                            Log.e("Erreur", e.getMessage());
                        }

                    //    Log.e("hhhhhfffffffffffffffffffh", "lsjdfqdddddddddddddddddddddddddlfmq");


                          /*
                          {
                          "active":true,
                          "_id":"60eea4354fc28911b0e26292",
                          "prefix":"237",
                          "phone":"657385146",
                          "name":"Achille",
                          "code":"5434106573851465222051",
                          "codeactivation":"06262",
                          "codeussd":"#150*1*1",
                          "operateur":"Orange",
                          "numero":"657385146",
                          "expiration_at":"2021-07-14T08:45:41.278Z",
                          "type":"1",
                          "created_at":"2021-08-14T08:45:41.254Z",
                          "updated_at":"2021-08-14T08:45:41.254Z",
                          "__v":0
                          }
                           */
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
                        //Log.e("hhhhhfffffffffffffffffffh","5555555555555555555dddddddddddddddlfmq");
                        OwnUssd usd = dbHelper.getSpecificOwnUssdWithCode(code);
                        if (usd == null) {
                            if (dbHelper.insertUssd(ussd)) {
                                //   Log.e("hhhhhfffffffffffffffffffh","22222222222222222222222ddddddddddddddlfmq");
                                getAllUssd();
                                // Log.e("hhhhhfffffffffffffffffffh","l222ddddddddddddddlfmq");
                            }
                        } else {
                            Log.e("hhhhhfffffffffffffffffffh", "22222222222222222222222ddddddddddddddlfmq");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //


                }
                progressDialog.dismiss();
                dialog.dismiss();

            }

            @Override
            public void onUpoadSuccess(List<?> listPhone) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.erreur_connextion), Toast.LENGTH_SHORT).show();


            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void getAllUssd() {
        List<OwnUssd> lisussd = dbHelper.getAllUssd();
        displayPeers(lisussd);
    }


    public void connexion(int position) {
        Log.e("position", String.valueOf(position));
        OwnUssd ussd = adapter.getUssd(position);
        Intent intent = new Intent(getApplicationContext(), GenCodeBar.class);
        intent.putExtra("code", ussd.getCode());
        intent.putExtra("codeussd", ussd.getCodeussd());
        intent.putExtra("name", ussd.getName());
        intent.putExtra("numero", ussd.getNumero());
        intent.putExtra("operateur", ussd.getOperateur());
        intent.putExtra("type", ussd.getType());
        intent.putExtra("prefix", ussd.getPrefix());
        intent.putExtra("phone", ussd.getPhone());
        intent.putExtra("ussd_idonline", ussd.getUssd_idonline());
        intent.putExtra("datetime", ussd.getDatetime());
        intent.putExtra("dateupdate", ussd.getDateupdate());
        intent.putExtra("expiration_at", ussd.getExpiration_at());
    //    Log.e("activite", String.valueOf(ussd.isActive()));
        intent.putExtra("active", String.valueOf(ussd.isActive()));
        //startActivityForResult(intent);
        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);

        //////confiintant= adapter.getListDemandeur().get(position);
        // confiintant=tabconfigurations.get(position);
    }

    OwnUssd ownUssddelete = null;

    public void delete(int position) {
        ownUssddelete = adapter.getUssd(position);
        dialog3.show();

    }
    //String url="market://details?id="+appPackageName;
    // String urls="https://play.google.com/store/apps/detail?id"+appPackageName;

    public void appenUrl(int position) {
        Log.e("position", String.valueOf(position));
        Pub pub = adapter1.getPub(position);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pub.getUrl_destination()));
        try {
            startActivity(browserIntent);
        } catch (Exception e) {

        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void deleteUssd(OwnUssd ownUssd) {
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.show();
        new CLientOnlineservice(this).deleteUssdOnline(ownUssd, prefix, phone, new CLientOnlineservice.OnCallsBack() {
            @Override
            public void onUpoadSuccess(String value) {
                // progressDialog.dismiss();
                // Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onUpoadSuccess(JSONObject response) {
                try {
                    String value = response.getString("value");
                    if (Boolean.parseBoolean(value)) {
                        if (dbHelper.deleteUssd(ownUssd)) {
                            getAllUssd();
                        }
                    }

                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ownUssddelete = null;
               /* try {
                    String online_id=response.getString("_id");
                    String user_id=response.getString("user_id");
                    String name=response.getString("name");
                    String code=response.getString("code");
                    String codeussd=response.getString("codeussd");
                    String active=response.getString("active");
                    String operateur=response.getString("operateur");
                    String numero=response.getString("numero");
                    String type=response.getString("type");
                    String created_at=response.getString("created_at");
                    String updated_at=response.getString("updated_at");
                    //      Log.e("hhhhhfffffffffffffffffffh","lsjdfqdddddddddddddddddddddddddlfmq");
                    OwnUssd ussd = new OwnUssd();
                    ussd.setUssd_idonline(online_id);
                    ussd.setName(name);
                    ussd.setCode(code);
                    ussd.setCodeussd(codeussd);
                    ussd.setActive(Boolean.valueOf(active));
                    ussd.setOperateur(operateur);
                    ussd.setNumero(numero);
                    ussd.setType(type);
                    ussd.setDatetime(created_at);
                    ussd.setDateupdate(updated_at);
                    //Log.e("hhhhhfffffffffffffffffffh","5555555555555555555dddddddddddddddlfmq");
                    if(dbHelper.insertUssd(ussd)) {
                        //   Log.e("hhhhhfffffffffffffffffffh","22222222222222222222222ddddddddddddddlfmq");
                        getAllUssd();
                        // Log.e("hhhhhfffffffffffffffffffh","l222ddddddddddddddlfmq");
                    }
                    progressDialog.dismiss();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                //

            }

            @Override
            public void onUpoadSuccess(JSONArray response) {
                /*
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
               /*         dbHelper.insertCLient(client);

                        //}

                    }
                    dbHelper.updateCurentdate(1);


                    //   }
                    ////////////////////
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("numbersddd", "fin insertion sqlite");
        */
            }

            @Override
            public void onUpoadSuccess(List<?> listPhone) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
//                progressDialog.dismiss();
                // Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();
                ownUssddelete = null;
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.erreur_connextion), Toast.LENGTH_SHORT).show();


            }
        });
    }


    public void addOwn(OwnUssd ownUssd) {
        adapter.add(ownUssd);
    }

    public void removeOwn(OwnUssd ownUssd) {
        adapter.remove(ownUssd);
    }

    public void displayPeers(List<OwnUssd> tabOwnUssd) {
        adapter.addAll(tabOwnUssd);
       /* Set<OwnUssd> set=new HashSet<>();

        set.addAll(tabOwnUssd);
        ArrayList<OwnUssd> distincList=new ArrayList(set);
        rv.setAdapter(null);

        //adapter.notify();

        adapter.notifyDataSetChanged();*/
        // rv.setAdapter(adapter);   //  a modifier

    }


   /* private void showNotifi(){
     //    NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.my_notify);
        RemoteViews eremoteViews=new RemoteViews(getPackageName(),R.layout.my_notification);



    /*    Notification notification=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.speed)
                .setCustomBigContentView(eremoteViews)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();
        managerCompat.notify(1,notification);

*/

  /*      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_MIN);
            notificationChannel.setDescription("code sphere");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);

            notificationManager.createNotificationChannel(notificationChannel);
        }



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                // .setDefaults(Notification.DEFAULT_ALL)
                //.setWhen(System.currentTimeMillis())

                // .setContentTitle(title)
                //.setContentText(body)
                //.setSmallIcon(R.drawable.ic_launcher_background)

              /*  .setSmallIcon(R.drawable.ic_add)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())*/


      /*          .setSmallIcon(R.drawable.speed)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCustomContentView(remoteViews)

                .setCustomBigContentView(eremoteViews)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
               // .build();




                .setContentInfo("info");

        //  notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
        notificationManager.notify(1, notificationBuilder.build());










    }
*/

/*
    private void showNotificationsd(String title, String body,String url ) {
        int value=new Random().nextInt();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // plus petit
        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notification_collapssed);

        collapsedView.setTextViewText(R.id.text_new_message, title);
        collapsedView.setTextViewText(R.id.text_view_colla, title);
        collapsedView.setImageViewResource(R.id.image_view_expand, R.drawable.speed);




// grand
        RemoteViews expandedView = new RemoteViews(getPackageName(),
                R.layout.notification_expended);
        expandedView.setTextViewText(R.id.text_titre, title);
        if (url != null && !url.isEmpty()) {
            expandedView.setImageViewBitmap(R.id.image_view_expanded,  getBitmapFromURL(url));
        }else{
            expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.speed);
        }



        Intent clickIntent = new Intent(this, NotificationReceiver.class);
        clickIntent.putExtra("vaulue",value);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
                value, clickIntent, 0);









        //




        //collapsedView.setBoolean(R.id.sw);


        // expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.speed);

        // gestion des click
        collapsedView.setOnClickPendingIntent(R.id.liner_collaps, clickPendingIntent);
        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_MIN);
            notificationChannel.setDescription("code sphere");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);

            notificationManager.createNotificationChannel(notificationChannel);
        }



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                // .setDefaults(Notification.DEFAULT_ALL)
                //.setWhen(System.currentTimeMillis())

                // .setContentTitle(title)
                //.setContentText(body)
                //.setSmallIcon(R.drawable.ic_launcher_background)

                .setSmallIcon(R.drawable.ic_add)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentInfo("info");

        //  notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
        notificationManager.notify(1, notificationBuilder.build());





    }
*/
    ////////////////////////////
   /* public void showNotification() {
        Log.e("ddddd","dsljqflqsjfqs");
        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notification_collapssed);
        RemoteViews expandedView = new RemoteViews(getPackageName(),
                R.layout.notification_expended);
        Intent clickIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
                0, clickIntent, 0);
        collapsedView.setTextViewText(R.id.text_new_message, "Hello World!");
        expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.flag_burundi);
        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();
        notificationManager.notify(1, notification);
    }*/
    //////////////////
   /* private void showNotification(String title, String body,String tag ) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notification_collapssed);
        RemoteViews expandedView = new RemoteViews(getPackageName(),
                R.layout.notification_expended);
        Intent clickIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
                0, clickIntent, 0);
        collapsedView.setTextViewText(R.id.text_new_message, "Hello World!");
        collapsedView.setImageViewResource(R.id.image_view_expanded, R.drawable.flag_burundi);
        //collapsedView.setBoolean(R.id.sw);
        expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.flag_burundi);


        // gestion des click
        collapsedView.setOnClickPendingIntent(R.id.liner_collaps, clickPendingIntent);
        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);


        /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_MIN);
            notificationChannel.setDescription("code sphere");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);


            notificationManager.createNotificationChannel(notificationChannel);
        }


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
               .setPriority(Notification.PRIORITY_HIGH)
               // .setDefaults(Notification.DEFAULT_ALL)
                //.setWhen(System.currentTimeMillis())
                .setSound(alarmSound)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle(title)
                .setContentText(body)
                //.setSmallIcon(R.drawable.ic_launcher_background)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSmallIcon(R.drawable.speed)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();
               // .setContentInfo("info");

      //  notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
        notificationManager.notify(1, notificationBuilder.build());


*/
    /*    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
               .setSmallIcon(R.drawable.speed)
            //    .setCustomContentView(collapsedView)
            //    .setCustomBigContentView(expandedView)

               .setCustomHeadsUpContentView(expandedView)
                .setContent(collapsedView)
                .setCustomContentView(collapsedView)

                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(alarmSound)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();
        notificationManager.notify(1,notification);


    }*/


  /*  private final BroadcastReceiver SmSreceiveCode = new BroadcastReceiver() {

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
      /*      if (intent.getAction().equalsIgnoreCase(SMS)) {
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
                            if (codedepayement.length() == LENTUSERCODE) {
                                ussd.setCode(codedepayement);
                                dialog1.dismiss();
                                progressDialog.show();
                                ////////////////////////////
                                sendtocreateussd();
                            }
                            /////////////////////////////////
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
    };*/

    /////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_EXCECUTE_USSD && resultCode == Activity.RESULT_OK) {
            progressDialog.dismiss();
        } else {

            if (requestCode == LAUNCH_SECOND_ACTIVITY && resultCode == Activity.RESULT_CANCELED) {
                // Toast.makeText(this, "Act Message", Toast.LENGTH_LONG).show();
                getAllUssd();

            } else {

                IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (intentResult != null) {
                    if (intentResult.getContents() == null) {
                        //  textView.setText("Cancelled");
                    } else {

                        initCall();
                    }
                }


            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


}