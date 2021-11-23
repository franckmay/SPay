package com.os.speed.service;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static com.os.speed.StiticSimple.urlgetCode;
import static com.os.speed.StiticSimple.urlimageAndName;
import static com.os.speed.StiticSimple.urllanguage;
import static com.os.speed.StiticSimple.urlvalidecode;


public class getCodeOnlineservice {
    private Context context;

    public getCodeOnlineservice(Context context) {
        this.context = context;
    }

    public void  sendPrefixAndNumber(String prefix, String phone,String operateur,final OnCallsBack OnCallssBack){
        //  recuperation du message et formation de l object message

      //  String url="http://192.168.43.67:8888/registrer/check-registration";
        new  OnlineService(context).sendOnline(prefix,phone,operateur, urlgetCode, new OnlineService.OnCallBack() {
            @Override
            public void onUpoadSuccess(String code) {
                OnCallssBack.onUpoadSuccess(code);
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {

            }

            @Override
            public void onUpoadSuccess(JSONArray response) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                OnCallssBack.onUpoadFailed(e);
            }
        });


    }
    public void  sendCode(String prefix, String phone,String code,final OnCallsBack OnCallssBack){
        //  recuperation du message et formation de l object message

        //String url="http://192.168.43.67:8888/registrer/check-registration";
        new  OnlineService(context).sendCodeOnline(prefix,phone,code,urlvalidecode, new OnlineService.OnCallBack() {
            @Override
            public void onUpoadSuccess(String code) {
                OnCallssBack.onUpoadSuccess(code);
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {
                OnCallssBack.onUpoadSuccess(response);
            }

            @Override
            public void onUpoadSuccess(JSONArray response) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                OnCallssBack.onUpoadFailed(e);
            }
        });


    }



    public void  sendImageAndName(String prefix, String phone,String Image,String extension,String name,final OnCallsBack OnCallssBack){
        //  recuperation du message et formation de l object message


        new  OnlineService(context).sendImageAndNameOnline(prefix,phone,Image, extension,name,urlimageAndName, new OnlineService.OnCallBack() {
            @Override
            public void onUpoadSuccess(String code) {
                OnCallssBack.onUpoadSuccess(code);
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {
                OnCallssBack.onUpoadSuccess(response);
            }

            @Override
            public void onUpoadSuccess(JSONArray response) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                OnCallssBack.onUpoadFailed(e);
            }
        });


    }

    public void  sendLanguage(String prefix, String phone,String language,String tokenfirebase,final OnCallsBack OnCallssBack){
        //  recuperation du message et formation de l object message


        new  OnlineService(context).sendLanguage(prefix,phone,language,tokenfirebase,urllanguage, new OnlineService.OnCallBack() {
            @Override
            public void onUpoadSuccess(String code) {
                OnCallssBack.onUpoadSuccess(code);
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {
                OnCallssBack.onUpoadSuccess(response);
            }

            @Override
            public void onUpoadSuccess(JSONArray response) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                OnCallssBack.onUpoadFailed(e);
            }
        });


    }


    public interface OnCallsBack{
        void onUpoadSuccess(String code);
        void onUpoadSuccess(JSONObject response);
        void onUpoadSuccess(List<?> listPhone);
        void onUpoadFailed(Exception e);
    }
}
