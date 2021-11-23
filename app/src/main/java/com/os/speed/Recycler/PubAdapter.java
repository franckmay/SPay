package com.os.speed.Recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.os.speed.Home;
import com.os.speed.ItemeClickListener;
import com.os.speed.R;
import com.os.speed.modele.Pub;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadjang on 28/09/2016.
 */
public class PubAdapter extends RecyclerView.Adapter<PuHolder>  {
    Context c;
    public List<Pub> listUssd = new ArrayList<>();
    CustumFilterClientaxi filter;
    int previousPosition = 0;

    //DatabaseHelper myDb;


    public PubAdapter(Context c) {
        this.c = c;
        //this.myDb = new DatabaseHelper(c);
    }

    @Override
    public PuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pub, parent, false);
        final PuHolder holder = new PuHolder(v);
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nomclient=(TextView)v.findViewById(R.id.nom);
                String nomclients=nomclient.getText().toString();
                TextView codeclient=(TextView)v.findViewById(R.id.code);
                String codeclients=codeclient.getText().toString();
                ListClientPrix.nomclient=nomclients;
                ListClientPrix.code=codeclients;

            }
        });*/
       /* holder.prix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        return holder;
    }

    @Override
    public void onBindViewHolder(final PuHolder holder, int position) {
        //holder.nom.setText(litclient.get(position).getNom());
        holder.message.setText(listUssd.get(position).getTitle());



        if (listUssd.get(position).getUrl_image() != null && !listUssd.get(position).getUrl_image() .isEmpty()) {
            Glide.with(c)
                    .asBitmap()
                    .load(listUssd.get(position).getUrl_image() )
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            //   imageView.setImageBitmap(resource);
                          //  collapsedView.setImageViewBitmap(R.id.image_view_expand, resource);
                            holder.image.setImageBitmap(resource);

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });


        }else{
            holder.image.setImageResource(R.drawable.speed);
        }



        // holder.code.setText(litclient.get(position).getIndentifiantTelephone());
       /* if (position > previousPosition) { // we are scrolling down
            AnimationUtil.animate(holder, true);
        } else { // we are scrolling up
            AnimationUtil.animate(holder, false);
        }
        previousPosition = position;*/

        holder.setItemeClickListener(new ItemeClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //CheckBox chk=(CheckBox)v;
                Home activity = (Home) c;
                try {
                    activity.appenUrl(position);
                } catch (Exception e) {
                    Log.e("erreur", e.getMessage());
                }

            }
        });

    }

    @Override
    public int getItemCount() {

        return listUssd.size();
        //return listUssd==null?0:Integer.MAX_VALUE;
    }
    // Return filter Objet


    public List<Pub> getListDemandeur() {
        return listUssd;
    }

    public void addAll(List<Pub> listabl){
        listUssd.clear();
        listUssd.addAll(listabl);
        //filterList.addAll(listabl);
        notifyDataSetChanged();
    }

    public void add(Pub ownUssd) {
        listUssd.add(ownUssd);
       // filterList.add(ownUssd);
        notifyDataSetChanged();
    }

    public Pub getPub(int position) {
        return listUssd.get(position);
    }

    public void remove(Pub ownUssd) {
        listUssd.remove(ownUssd);
       // filterList.remove(ownUssd);
        notifyDataSetChanged();
    }
}

