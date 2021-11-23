package com.os.speed.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.os.speed.R;


public class AccepterConditionsFragment extends Fragment {

    private Button btn;

    public AccepterConditionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accepter_conditions, container, false);

        TextView textView = view.findViewById(R.id.tv_accepterConditions);
        btn = (Button) view.findViewById(R.id.btn_accept);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(AccepterConditionsFragment.this)
                        .navigate(R.id.action_accepterConditionsFragment_to_fragmentFonctionnaliteune);
                      //  .navigate(R.id.action_accepterConditionsFragment_to_phoneLoginFragment);
            }
        });

//        String text = getString(R.string.strAccepterConditions);
        String text = textView.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.GREEN);
            }
        };
        spannableString.setSpan(clickableSpan,59,117, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }
}
