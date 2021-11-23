package com.os.speed.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.os.speed.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragmentfonctionnalitetrois#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragmentfonctionnalitetrois extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LinearLayout suivant;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragmentfonctionnalitetrois() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragmentfonctionnalitetrois.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragmentfonctionnalitetrois newInstance(String param1, String param2) {
        Fragmentfonctionnalitetrois fragment = new Fragmentfonctionnalitetrois();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragmentfonctionnalitetrois, container, false);

        suivant = view.findViewById(R.id.suivant);
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(Fragmentfonctionnalitetrois.this)
                        .navigate(R.id.action_fragmentfonctionnalitetrois_to_fragmentfonctionnalitequatre);
                //  .navigate(R.id.action_accepterConditionsFragment_to_phoneLoginFragment);
            }
        });
        return view;
    }
}