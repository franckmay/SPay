package com.os.speed.Recycler;


import android.widget.Filter;

import com.os.speed.modele.OwnUssd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadjang on 30/09/2016.
 */
public class CustumFilterClientaxi extends Filter {

    MyOwnAdapter adapter;
    List<OwnUssd> filterList;


    public CustumFilterClientaxi(List<OwnUssd> filterList, MyOwnAdapter adapter ) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        if(constraint!=null&& constraint.length()>0){
            constraint=constraint.toString().toUpperCase();
            ArrayList<OwnUssd> filterdObjet=new ArrayList<>();
            for(int i=0;i<filterList.size();i++){
                // choix de la selection
                if(filterList.get(i).getOperateur().toUpperCase().contains(constraint)){
                   // add Objet to filterObjet
                    filterdObjet.add(filterList.get(i));
                }

            }
            results.count=filterdObjet.size();
            results.values=filterdObjet;


        }else{
            results.count=filterList.size();
            results.values=filterList;

        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
         adapter.listUssd= (ArrayList<OwnUssd>) results.values;
         // refrech adapter
        adapter.notifyDataSetChanged();

    }


}
