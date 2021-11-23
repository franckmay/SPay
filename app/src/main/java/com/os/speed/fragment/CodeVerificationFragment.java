package com.os.speed.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.gne.www.lib.PinView;
import com.os.speed.R;
import com.os.speed.service.getCodeOnlineservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.os.speed.StiticSimple.NOMCOMPAGNY;
import static com.os.speed.StiticSimple.SMS;
import static com.os.speed.staticclass.recursekeys;


public class CodeVerificationFragment extends Fragment {
    public CodeVerificationFragment() {}
    public ProgressDialog progressDialog;
    public PinView et_number1,et_number2,et_number3,et_number4;
    String phone;
    String prefix;
    String operateur;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_code_verification, container, false);
        et_number1=view.findViewById(R.id.pinview);
     /*   et_number2=view.findViewById(R.id.et_number2);
        et_number3=view.findViewById(R.id.et_number3);
        et_number4=view.findViewById(R.id.et_number4);
        */

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.show();

         phone=getArguments().getString("phone");
         prefix=getArguments().getString("prefix");
        operateur=getArguments().getString("operateur");


        sendphoneInbdOnline(prefix,phone,operateur);






        Button btn = (Button) view.findViewById(R.id.btn_codeV_next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if(et_number1.getText().toString().isEmpty()||et_number2.getText().toString().isEmpty()||et_number3.getText().toString().isEmpty()||et_number4.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), R.string.pleaseentervalid, Toast.LENGTH_SHORT).show();
                    return;
                }
                String code= et_number1.getText().toString()+et_number2.getText().toString()+et_number3.getText().toString()+et_number4.getText().toString();
*/
                String code= et_number1.getText();

                if(!code.isEmpty()){


                    /*NavHostFragment.findNavController(CodeVerificationFragment.this)
                            .navigate(R.id.action_codeVerificationFragment_to_definirUtilisateurFragment);*/
                    sendCodeInbdOnline( prefix,  phone, code);

                }else{
                    Toast.makeText(getContext(), R.string.isempty, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void sendphoneInbdOnline(String prefix, String phone,String operateur) {
        new getCodeOnlineservice(getContext()).sendPrefixAndNumber(prefix, phone, operateur,new getCodeOnlineservice.OnCallsBack() {
            @Override
            public void onUpoadSuccess(String values) {

               // et_number1.setText(String.valueOf(code));
               /* et_number2.setText(String.valueOf(code.charAt(1)));
                et_number3.setText(String.valueOf(code.charAt(2)));
                et_number4.setText(String.valueOf(code.charAt(3)));*/
                /// CODE INTEGREE EN ATTENDANS LA VERIFICATION PAR SMS
                try {
                    JSONObject row=new JSONObject(values);
                    String value = recursekeys(row, "value");
                    boolean valeur=Boolean.valueOf(value);
                   // Log.e("valeur_usse","ffffffffffffffff");
                //    Log.e("valeur",String.valueOf(valeur));
                    if(!valeur) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(CodeVerificationFragment.this)
                                .navigate(R.id.action_codeVerificationFragment_to_phoneLoginFragment);
                    }else{
                        String sms = recursekeys(row, "sms");
                        String s = sms;
                        s = s.substring(s.indexOf("(") + 1);
                        s = s.substring(0, s.indexOf(")"));

                        et_number1.setText(s);
                        String code= et_number1.getText();
                        if(!code.isEmpty()){
                           /* NavHostFragment.findNavController(CodeVerificationFragment.this)
                                    .navigate(R.id.action_codeVerificationFragment_to_definirUtilisateurFragment);*/
                            sendCodeInbdOnline( prefix,  phone, code);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }

            @Override
            public void onUpoadSuccess(JSONObject response) {

            }

            @Override
            public void onUpoadSuccess(List<?> listPhone) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
//                Log.e("valeur",e.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(CodeVerificationFragment.this)
                        .navigate(R.id.action_codeVerificationFragment_to_phoneLoginFragment);


            }
        });
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void sendCodeInbdOnline(String prefix, String phone,String code) {
       /* progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.show();*/
        new getCodeOnlineservice(getContext()).sendCode(prefix,phone,code ,new getCodeOnlineservice.OnCallsBack() {
            @Override
            public void onUpoadSuccess(String value) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(CodeVerificationFragment.this)
                        .navigate(R.id.action_codeVerificationFragment_to_phoneLoginFragment);
                progressDialog.dismiss();
                // et_number1.setText(String.valueOf(code));
               /* et_number2.setText(String.valueOf(code.charAt(1)));
                et_number3.setText(String.valueOf(code.charAt(2)));
                et_number4.setText(String.valueOf(code.charAt(3)));*/
           /*     boolean valeur=Boolean.valueOf(value);
                if(!valeur) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(CodeVerificationFragment.this)
                            .navigate(R.id.action_codeVerificationFragment_to_phoneLoginFragment);
                }
                Log.e("valeur",String.valueOf(valeur));
*/
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {

                try {
                    progressDialog.dismiss();
                    String token=response.getString("token");
                    String prefix=response.getString("prefix");
                    String phone=response.getString("phone");
                  //  String avatar=response.getString("avatar");
                    Log.e("numbersddd", "response.toString()");
                 //   JWT jwt=new JWT(token);
                 //   Log.e("token", token);
                    //Log.e("phone", phone);
                    //progressDialog.dismiss();
                    Bundle bundle = new Bundle();
               //     bundle.putString("token", token);
                    bundle.putString("prefix", prefix);
                    bundle.putString("phone", phone);
                 //   bundle.putString("avatar", avatar);
                    NavHostFragment.findNavController(CodeVerificationFragment.this)
                            .navigate(R.id.action_codeVerificationFragment_to_definirUtilisateurFragment,bundle);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //

            }

            @Override
            public void onUpoadSuccess(List<?> listPhone) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();

            }
        });
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter=new IntentFilter(SMS);
        getContext().registerReceiver(SmSreceiveCode,filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(SmSreceiveCode);
    }

    private final BroadcastReceiver SmSreceiveCode =new  BroadcastReceiver (){

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
                    Log.e("from",from);
                    if(from.equalsIgnoreCase(NOMCOMPAGNY)){
                        String s = strMessage;
                        s = s.substring(s.indexOf("(") + 1);
                        s = s.substring(0, s.indexOf(")"));

                        et_number1.setText(s);
                        String code= et_number1.getText();
                        if(!code.isEmpty()){
                           /* NavHostFragment.findNavController(CodeVerificationFragment.this)
                                    .navigate(R.id.action_codeVerificationFragment_to_definirUtilisateurFragment);*/
                            sendCodeInbdOnline( prefix,  phone, code);
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










}
