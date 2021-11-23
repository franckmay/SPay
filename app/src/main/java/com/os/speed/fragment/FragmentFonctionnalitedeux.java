package com.os.speed.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.os.speed.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFonctionnalitedeux#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFonctionnalitedeux extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LinearLayout suivant;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  TextView position;
    public FragmentFonctionnalitedeux() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFonctionnalitedeux.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFonctionnalitedeux newInstance(String param1, String param2) {
        FragmentFonctionnalitedeux fragment = new FragmentFonctionnalitedeux();
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

        View view = inflater.inflate(R.layout.fragment_fonctionnalitedeux, container, false);

        suivant = view.findViewById(R.id.suivant);
         position = view.findViewById(R.id.position);
       /* LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(position.getLayoutParams());
        LinearLayout pere=view.findViewById(R.id.pere);
        pere.setLayoutParams(position.getLayoutParams());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Call some material design APIs here
            lp.setMargins(0, 4000, 0, 0);
        } else {
            // Implement this feature without material design
            lp.setMargins(0, 100, 0, 0);
        }*/
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(FragmentFonctionnalitedeux.this)
                        .navigate(R.id.action_fragmentFonctionnalitedeux_to_fragmentfonctionnalitetrois);
                //  .navigate(R.id.action_accepterConditionsFragment_to_phoneLoginFragment);
            }
        });
        return view;
    }
}