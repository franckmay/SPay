package com.os.speed.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.os.speed.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFonctionnaliteune#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFonctionnaliteune extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout suivant;
    private ImageView images;
    public FragmentFonctionnaliteune() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFonctionnaliteune.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFonctionnaliteune newInstance(String param1, String param2) {
        FragmentFonctionnaliteune fragment = new FragmentFonctionnaliteune();
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

        View view = inflater.inflate(R.layout.fragment_fonctionnaliteune, container, false);

        suivant = view.findViewById(R.id.suivant);
        images = view.findViewById(R.id.images);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(images.getLayoutParams());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Call some material design APIs here
            lp.setMargins(0, 350, 0, 0);
        } else {
            // Implement this feature without material design
            lp.setMargins(0, 100, 0, 0);
        }



        images.setLayoutParams(lp);
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(FragmentFonctionnaliteune.this)
                        .navigate(R.id.action_fragmentFonctionnaliteune_to_fragmentFonctionnalitedeux);
                //  .navigate(R.id.action_accepterConditionsFragment_to_phoneLoginFragment);
            }
        });
        return view;
    }
}