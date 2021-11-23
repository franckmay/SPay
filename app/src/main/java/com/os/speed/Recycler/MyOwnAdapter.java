package com.os.speed.Recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.os.speed.ItemeClickListener;
import com.os.speed.R;
import com.os.speed.Reception;
import com.os.speed.modele.OwnUssd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadjang on 28/09/2016.
 */
public class MyOwnAdapter extends RecyclerView.Adapter<MyOwnHolderUssd> implements Filterable {

    Context c;
    List<OwnUssd> listUssd, filterList;
    public List<OwnUssd> chekedclient = new ArrayList<>();
    CustumFilterClientaxi filter;
    int previousPosition = 0;

    //DatabaseHelper myDb;


    public MyOwnAdapter(Context c, List<OwnUssd> listUss) {
        this.c = c;
        this.listUssd = listUss;
        this.filterList =  this.listUssd;
        //this.myDb = new DatabaseHelper(c);
    }

    @Override
    public MyOwnHolderUssd onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ussd_own, parent, false);
        final MyOwnHolderUssd holder = new MyOwnHolderUssd(v);
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
    public void onBindViewHolder(final MyOwnHolderUssd holder, int position) {
        //holder.nom.setText(litclient.get(position).getNom());
        holder.operateur.setText(listUssd.get(position).getOperateur());
        holder.numero.setText(listUssd.get(position).getNumero());
        holder.codeussd.setText(listUssd.get(position).getCodeussd());
        // holder.code.setText(litclient.get(position).getIndentifiantTelephone());
       /* if (position > previousPosition) { // we are scrolling down
            AnimationUtil.animate(holder, true);
        } else { // we are scrolling up
            AnimationUtil.animate(holder, false);
        }
        previousPosition = position;*/
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reception activity = (Reception) c;
                try {
                   activity.delete(position);

                } catch (Exception e) {
                    Log.e("erreur", e.getMessage());
                }
            }
        });
        holder.setItemeClickListener(new ItemeClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //CheckBox chk=(CheckBox)v;
                Reception activity = (Reception) c;
                try {
                    activity.connexion(position);
                } catch (Exception e) {
                    Log.e("erreur", e.getMessage());
                }

            }
        });

    }

    @Override
    public int getItemCount() {

        return listUssd.size();
    }
    // Return filter Objet

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustumFilterClientaxi(filterList, this);
        }

        return filter;
    }

    public List<OwnUssd> getListDemandeur() {
        return listUssd;
    }
    public void addAll(List<OwnUssd> listabl){
        listUssd.clear();
        listUssd.addAll(listabl);
        //filterList.addAll(listabl);
        notifyDataSetChanged();
    }

    public void add(OwnUssd ownUssd) {
        listUssd.add(ownUssd);
       // filterList.add(ownUssd);
        notifyDataSetChanged();
    }
    public OwnUssd getUssd(int position) {
        return listUssd.get(position);
    }


    public void remove(OwnUssd ownUssd) {
        listUssd.remove(ownUssd);
       // filterList.remove(ownUssd);
        notifyDataSetChanged();
    }
}

