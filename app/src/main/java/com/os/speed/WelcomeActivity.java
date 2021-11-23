package com.os.speed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.os.speed.StiticSimple.MULTIPLE_PERMISSIONS;

public class WelcomeActivity extends AppCompatActivity {

    String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE
    }; // Here i used multiple permission check


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // permission
        Boolean permission=checkPermissions();

        if (restorePref()) {
            Log.e("permissions",String.valueOf("n eddddt pas"));
            Intent intent =new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        Log.e("permissions",String.valueOf("nddddddddd"));
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();

    }



    /*
     *pour ne plus afficher les vues de configuration apres la première ouverture dde l'appliaction
     *à voir dans FragmentInitialisation (btn.setOnclickListener)
     */
    private  boolean restorePref(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("mespreferences",MODE_PRIVATE);
        Boolean siWelcomeActivityOuvert = preferences.getBoolean("siBienvenueOuvert",false);
        return siWelcomeActivityOuvert;

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

}