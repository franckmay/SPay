package com.os.speed.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.installations.FirebaseInstallations;
import com.os.speed.Home;
import com.os.speed.R;
import com.os.speed.modele.CurentDate;
import com.os.speed.modele.Users;
import com.os.speed.service.getCodeOnlineservice;
import com.os.speed.sqlite.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.os.speed.staticclass.DateToString;
import static com.os.speed.staticclass.StringToBitmap;
import static com.os.speed.staticclass.StringTodate;
import static com.os.speed.staticclass.getBitmapAsByteArray;
import static com.os.speed.staticclass.recursekeys;


public class TraductionFragment extends Fragment {

    Spinner spinner;
    String[] langues = new String[]{"Anglais", "Français"};

    public ProgressDialog progressDialog;
    public String prefix, phone, languagage;
    DBHelper dbHelper;

    public TraductionFragment() {
    }

    SharedPreferences preference;
    String tokenfirebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traduction, container, false);
        preference = getContext().getSharedPreferences("SHARED_PREF", MODE_PRIVATE);

        //  SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        tokenfirebase="";
      /*  tokenfirebase = preference.getString("tokenfirebase", null);
        if (tokenfirebase == null || tokenfirebase.isEmpty())
            FirebaseInstallations.getInstance().getId().addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            String token = task.getResult();
                            Log.i("token ---->>", token);
                            tokenfirebase = token;
                            SharedPreferences preference = getContext().getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
                            Log.e("TOkensss ", tokenfirebase);
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putString("tokenfirebase", tokenfirebase).apply();
                            editor.apply();
                        }
                    }
            );*/


        dbHelper = new DBHelper(getContext());
        spinner = view.findViewById(R.id.spinner_langue);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, langues);
        spinner.setAdapter(adapter);
        prefix = getArguments().getString("prefix");
        phone = getArguments().getString("phone");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languagage = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button button = view.findViewById(R.id.btn_suivant_traduction);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLanguageInbdOnline(prefix, phone, languagage, tokenfirebase);
            }
        });


        return view;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void sendLanguageInbdOnline(String prefix, String phone, String language, String tokenfirebase) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.show();
        new getCodeOnlineservice(getContext()).sendLanguage(prefix, phone, language, tokenfirebase, new getCodeOnlineservice.OnCallsBack() {
            @Override
            public void onUpoadSuccess(String value) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {

                try {
                    //  Log.e("response__values", response.toString());
                    String user_id_online = recursekeys(response, "_id");
                    String prefix = recursekeys(response, "prefix");
                    String phone = recursekeys(response, "phone");
                    String username = recursekeys(response, "username");

                    String active = recursekeys(response, "active");

                    String verified = recursekeys(response, "verified");
                    String preferences = recursekeys(response, "preferences");
                    String last_connection = recursekeys(response, "last_connection");
                    String last_verification = recursekeys(response, "last_verification");
                    String created_at = recursekeys(response, "created_at");
                    String updated_at = recursekeys(response, "updated_at");
                    String avatar = recursekeys(response, "avatar");
                   // String imagebase64 = recursekeys(response, "imagebase64");
              //      Bitmap imageprofil = StringToBitmap(imagebase64);
                    Users user = new Users();
                    user.setIdonline(user_id_online);
                    user.setPrefix(prefix);
                    user.setPhone(phone);
                    Log.e("valueactive", username);
                    user.setUserName(username);
                    if (Boolean.valueOf(active))
                        user.setActive(1);
                    else
                        user.setActive(0);
                    user.setVerified(Boolean.valueOf(verified));
                    user.setPreferences(preferences);
                    user.setLast_connection(last_connection);
                    user.setLast_verification(last_verification);
                    user.setCreated_at(created_at);
                    user.setUpdated_at(updated_at);
                 /*   if (imageprofil != null)
                        user.setImage(getBitmapAsByteArray(imageprofil));*/
                    if (dbHelper.insertUsers(user)) {
                        CurentDate date = new CurentDate();
                        Date nodate = new Date();
                        String stringdate = DateToString(nodate);
                        Date dates = StringTodate(stringdate);
                        date.setCurentdate(dates);
                        date.setDatetime(dates);
                        dbHelper.insertDate(date);

                        progressDialog.dismiss();
                        Intent intent = new Intent(getContext(), Home.class);
                        //  intent.putExtra("all",  response.toString());
                        intent.putExtra("prefix", prefix);
                        intent.putExtra("user_id_online", user_id_online);
                        intent.putExtra("username", username);
                        intent.putExtra("phone", phone);
                        intent.putExtra("avatar", avatar);

                        SharedPreferences.Editor editor = preference.edit();
                        editor.putString("iduser", user_id_online).apply();
                        editor.putString("phone", phone).apply();
                        editor.putString("prefix", prefix).apply();
                        editor.apply();

                        startActivity(intent);
                        getActivity().finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onUpoadSuccess(List<?> listPhone) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////


}
