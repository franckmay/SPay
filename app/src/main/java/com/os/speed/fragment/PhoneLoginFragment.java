package com.os.speed.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.hbb20.CountryCodePicker;
import com.os.speed.R;

import java.util.ArrayList;
import java.util.List;

import static com.os.speed.StiticSimple.REQUEST_SMS_PERMISION;


public class PhoneLoginFragment extends Fragment {

    private Button btn;
    private CountryCodePicker ccp;
    private EditText edit_text;
    public String  operateur;
    Spinner spinner;
    String[] oper = new String[]{"ORANGE", "MTN"};
    public PhoneLoginFragment() {
    }

    public static PhoneLoginFragment newInstance(String param1, String param2) {
        PhoneLoginFragment fragment = new PhoneLoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_phone_login, container, false);

        if(!checkPermissions()){
            requesPermission();
        }

        init(view);
        return view;
    }
    private void init(View view){
        ccp= view.findViewById(R.id.ccp);
        edit_text=view.findViewById(R.id.edit_text);
        btn =  view.findViewById(R.id.btn_login_next);
        spinner = view.findViewById(R.id.spinner_langue);
      /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, oper);
        spinner.setAdapter(adapter);*/
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getNumber(view);
            /*    if ()
                    NavHostFragment.findNavController(PhoneLoginFragment.this)
                            .navigate(R.id.action_phoneLoginFragment_to_codeVerificationFragment);
           */ }
        });
       /* spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                operateur = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    private void getNumber(View view) {
        String numerology=edit_text.getText().toString();

          //  prefix=String.valueOf(ccp.getFullNumber());
        String prefix=String.valueOf(ccp.getSelectedCountryCode());

       if (!numerology.isEmpty()){

// send sms
        //    byuenBroadcast( view);
// a supprimer plustar
              operateur="inconnu";
            Bundle bundle = new Bundle();
            bundle.putString("prefix", prefix);
            bundle.putString("phone", numerology);
            bundle.putString("operateur", operateur);
            Navigation.findNavController(view).navigate(R.id.action_phoneLoginFragment_to_codeVerificationFragment, bundle);

          /*  NavHostFragment.findNavController(PhoneLoginFragment.this)
                    .navigate(R.id.action_phoneLoginFragment_to_codeVerificationFragment);*/
        }else{
            Toast.makeText(getContext(),R.string.numecanbeempty,Toast.LENGTH_SHORT).show();
        }
    }
   /* public void byuenBroadcast(View view){
        Intent intent=new Intent();
        intent.setAction(senBroadcastExepmple);
        intent.putExtra("msg","hello from activyty");

        getContext().sendBroadcast(intent);
        Toast.makeText(getContext(),"actio realiser",Toast.LENGTH_SHORT).show();

    }*/
    String[] permissions = new String[]{
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS
    };
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_SMS_PERMISION);
            return false;
        }
        return true;
    }
    public  void requesPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS
        }, REQUEST_SMS_PERMISION);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SMS_PERMISION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // permissions granted.
                    //       getCallDetails(); // Now you call here what ever you want :)
                   // creatAlfolder(this);
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
