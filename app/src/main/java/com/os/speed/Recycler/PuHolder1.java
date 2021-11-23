package com.os.speed.Recycler;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.os.speed.ItemeClickListener;
import com.os.speed.R;


/**
 * Created by sadjang on 28/09/2016.
 */
public class PuHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView message;
    ImageView image;
    //TextView code;
    CheckBox checkBox;
    Context c;
    ItemeClickListener itemeClickListener;
    public PuHolder1(View itemView) {
        super(itemView);
        c=itemView.getContext();
        message=(TextView) itemView.findViewById(R.id.message);
        image=(ImageView) itemView.findViewById(R.id.image);

        //code=(TextView) itemView.findViewById(R.id.code);
       // checkBox=(CheckBox) itemView.findViewById(R.id.checkBox);
       // checkBox.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

 public void setItemeClickListener(ItemeClickListener ic){
    this.itemeClickListener=ic;
 }

    @Override
    public void onClick(View v) {
       this.itemeClickListener.onItemClick(v,getLayoutPosition());
    }
}
