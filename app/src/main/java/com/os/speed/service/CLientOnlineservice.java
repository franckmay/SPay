package com.os.speed.service;

import android.content.Context;
import android.util.Log;

import com.os.speed.modele.OwnUssd;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static com.os.speed.StiticSimple.urlactiveuserlocal;
import static com.os.speed.StiticSimple.urlactiveussd;
import static com.os.speed.StiticSimple.urlallpub;
import static com.os.speed.StiticSimple.urlcreateussd;
import static com.os.speed.StiticSimple.urldeleteussd;
import static com.os.speed.StiticSimple.urllistuser;
import static com.os.speed.StiticSimple.urllistussdown;
import static com.os.speed.StiticSimple.urluserdata;

public class CLientOnlineservice {
    private Context context;

    public CLientOnlineservice(Context context) {
        this.context = context;
    }


    ////////////////////////////////////////////
    public void  getAllPubOnline(String prefix,String phone,final OnCallsBack OnCallssBack){
        //  recuperation du message et formation de l object message
        //  String url="http://192.168.43.67:8888/registrer/check-registration";
        new  OnlineService(context).getPubOnline(prefix,phone, urlallpub, new OnlineService.OnCallBack() {
            @Override
            public void onUpoadSuccess(String code) {
                OnCallssBack.onUpoadSuccess(code);
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                OnCallssBack.onUpoadFailed(e);
            }

            @Override
            public void onUpoadSuccess(JSONArray response) {
                OnCallssBack.onUpoadSuccess(response);

            }
        });


    }

    //////////////////////////////////////

    public void  getAllClientOnline(String prefix,String phone,final OnCallsBack OnCallssBack){
        //  recuperation du message et formation de l object message
      //  String url="http://192.168.43.67:8888/registrer/check-registration";
        new  OnlineService(context).getCientOnline(prefix,phone, urllistuser, new OnlineService.OnCallBack() {
            @Override
            public void onUpoadSuccess(String code) {
                OnCallssBack.onUpoadSuccess(code);
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                OnCallssBack.onUpoadFailed(e);
            }

            @Override
            public void onUpoadSuccess(JSONArray response) {
                OnCallssBack.onUpoadSuccess(response);

            }
        });


    }
    public void  getOwnussd(String prefix,String phone,final OnCallsBack OnCallssBack){
        //  recuperation du message et formation de l object message
        //  String url="http://192.168.43.67:8888/registrer/check-registration";
        new  OnlineService(context).getownussd(prefix,phone, urllistussdown, new OnlineService.OnCallBack() {
            @Override
            public void onUpoadSuccess(String code) {
                OnCallssBack.onUpoadSuccess(code);
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                OnCallssBack.onUpoadFailed(e);
            }

            @Override
            public void onUpoadSuccess(JSONArray response) {
                OnCallssBack.onUpoadSuccess(response);

            }
        });


    }
    public void  creatUssdOnline(String phonenumber, String choiceoperateur,String  prefix, String phone,int type,String code,String copagnie_name,String radicale,final OnCallsBack OnCallssBack){
        //  recuperation du message et formation de l object message
        //  String url="http://192.168.43.67:8888/registrer/check-registration";
        new  OnlineService(context).createUssd( phonenumber,  choiceoperateur,  prefix,  phone,type, code, copagnie_name,radicale,urlcreateussd, new OnlineService.OnCallBack() {
            @Override
            public void onUpoadSuccess(String code) {
                OnCallssBack.onUpoadSuccess(code);
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {
                OnCallssBack.onUpoadSuccess(response);
            }

            @Override
            public void onUpoadFailed(Exception e) {
                OnCallssBack.onUpoadFailed(e);
            }

            @Override
            public void onUpoadSuccess(JSONArray response) {
                OnCallssBack.onUpoadSuccess(response);

            }
        });


    }


    public void  activeuserlocal(String idonline,String phone,String prefix,String codedepayement,final OnCallsBack OnCallssBack){
        //  recuperation du message et formation de l object message
        //  String url="http://192.168.43.67:8888/registrer/check-registration";
        new  OnlineService(context).activeuserlocal( idonline,  phone,  prefix,  codedepayement,urlactiveuserlocal, new OnlineService.OnCallBack() {
            @Override
            public void onUpoadSuccess(String code) {
                OnCallssBack.onUpoadSuccess(code);
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {
                OnCallssBack.onUpoadSuccess(response);
            }

            @Override
            public void onUpoadFailed(Exception e) {
                OnCallssBack.onUpoadFailed(e);
            }

            @Override
            public void onUpoadSuccess(JSONArray response) {
                OnCallssBack.onUpoadSuccess(response);

            }
        });


    }
    public void  deleteUssdOnline(OwnUssd ownUssd,String  prefix,String  phone, final OnCallsBack OnCallssBack){
        //  recuperation du message et formation de l object message
        //  String url="http://192.168.43.67:8888/registrer/check-registration";
        new  OnlineService(context).deleteUssd( ownUssd, prefix, phone, urldeleteussd, new OnlineService.OnCallBack() {
            @Override
            public void onUpoadSuccess(String code) {
                OnCallssBack.onUpoadSuccess(code);
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {
                OnCallssBack.onUpoadSuccess(response);
            }

            @Override
            public void onUpoadFailed(Exception e) {
                OnCallssBack.onUpoadFailed(e);
            }

            @Override
            public void onUpoadSuccess(JSONArray response) {
                OnCallssBack.onUpoadSuccess(response);

            }
        });


    }



    ////////////////////////////////////////////
    public void  getAllModificationUsers(String prefix,String phone,final OnCallsBack OnCallssBack){
        //  recuperation du message et formation de l object message
        //  String url="http://192.168.43.67:8888/registrer/check-registration";
        new  OnlineService(context).getAllModificationUser(prefix,phone, urluserdata, new OnlineService.OnCallBack() {
            @Override
            public void onUpoadSuccess(String code) {
                OnCallssBack.onUpoadSuccess(code);
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {
                 OnCallssBack.onUpoadSuccess(response);
            }

            @Override
            public void onUpoadFailed(Exception e) {
                OnCallssBack.onUpoadFailed(e);
            }

            @Override
            public void onUpoadSuccess(JSONArray response) {
                OnCallssBack.onUpoadSuccess(response);

            }
        });


    }

    public void  activeOwnUssd(String codedepayement,String code,String codeussd,String name,String numero,String operateur,String prefix,String phone,final OnCallsBack OnCallssBack){
        new  OnlineService(context).actiOwnussd( codedepayement,code,  codeussd,  name,  numero,operateur,prefix,phone,urlactiveussd, new OnlineService.OnCallBack() {
            @Override
            public void onUpoadSuccess(String code) {
                OnCallssBack.onUpoadSuccess(code);
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {
                OnCallssBack.onUpoadSuccess(response);
            }

            @Override
            public void onUpoadFailed(Exception e) {
                OnCallssBack.onUpoadFailed(e);
            }

            @Override
            public void onUpoadSuccess(JSONArray response) {
                OnCallssBack.onUpoadSuccess(response);

            }
        });


    }



    public interface OnCallsBack{
        void onUpoadSuccess(String code);
        void onUpoadSuccess(JSONObject response);
        void onUpoadSuccess(JSONArray response);
        void onUpoadSuccess(List<?> listPhone);
        void onUpoadFailed(Exception e);
    }
}
