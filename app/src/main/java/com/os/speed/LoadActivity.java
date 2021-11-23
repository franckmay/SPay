package com.os.speed;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.os.speed.sqlite.DBHelper;

import java.util.ArrayList;
import java.util.List;

import static com.os.speed.StiticSimple.MULTIPLE_PERMISSIONS;
import static com.os.speed.StiticSimple.REQUEST_CORD_PERMISSION;

public class LoadActivity extends AppCompatActivity {
    private static int TIME_OUT = 2000;
    Animation topAnim, bottomAnimation,bottom_animation_point;
    ImageView image;
    ImageView pointille;
    TextView logo, slogan;
    static int SPLASH_SCREEN = 5000;
    //DBHelper dbHelper;
    String iduser=null;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        getSupportActionBar().hide();
/// get preference
        preferences=getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
      //  SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        iduser=preferences.getString("iduser",null);
        //iduser=getPreferences(MODE_PRIVATE).getString("iduser",null);
//        Log.e("preference",iduser);
        initialie();
    }

    private void initialie() {
        // create Dbhelper;
      //  dbHelper = new DBHelper(LoadActivity.this);
        // Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        bottom_animation_point = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        // hooks
        image = findViewById(R.id.image);
        pointille = findViewById(R.id.pointille);
        logo = findViewById(R.id.logo);
        slogan = findViewById(R.id.slogan);
        // set Animation
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnimation);
        slogan.setAnimation(bottomAnimation);
        pointille.setAnimation(bottom_animation_point);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                //  choix de  la page suivante
              //  if (dbHelper.getAllUserslocal().size() > 0) {
                if (iduser != null) {
                    Log.e("permissions",String.valueOf("exist"));
                    // si l'untilisateur est  inscript l'orienter vers le processus d'inscription
                    Intent loadintent = new Intent(LoadActivity.this, Home.class);
                    startActivity(loadintent);
                    overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);

                } else {
                    Log.e("permissions",String.valueOf("n exist pas"));
                    // si l'untilisateur n'est pas encore inscript l'orienter vers le processus d'inscription
                    Intent loadintent = new Intent(LoadActivity.this, WelcomeActivity.class);
                    startActivity(loadintent);
                    overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
                }

                finish();
            }
        }, TIME_OUT);

    }

    // permission
    String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    }; // Here i used multiple permission check

    public void requesPermission() {

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CORD_PERMISSION);

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
                    initialie();
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






}