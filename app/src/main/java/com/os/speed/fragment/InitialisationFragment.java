package com.os.speed.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.os.speed.MainActivity;
import com.os.speed.R;


public class InitialisationFragment extends Fragment {

    Button terminer;
    public InitialisationFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_initialisation, container, false);
         terminer = view.findViewById(R.id.btn_initialisation_end);

        terminer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void SauvegardePref (){

        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("mespreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putBoolean("siBienvenueOuvert",true);
        editor.commit();
    }
}
