package com.os.speed.service;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.os.speed.modele.OwnUssd;
import com.os.speed.sqlonline.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class OnlineService {
    private Context context;

    public OnlineService(Context context) {
        this.context = context;
    }

    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    public void deleteUssd( OwnUssd ownUssd,String prefix, String phone,String url, OnCallBack onCallBack) {//  get post from online sql
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("type", ownUssd.getType());
            jsonBodyObj.put("ussd_id_online", ownUssd.getUssd_idonline());
            jsonBodyObj.put("code", ownUssd.getCode());
            jsonBodyObj.put("numero",  ownUssd.getNumero());
            jsonBodyObj.put("codeussd", ownUssd.getCodeussd());
            jsonBodyObj.put("operateur", ownUssd.getOperateur());
            jsonBodyObj.put("prefix", prefix);
            jsonBodyObj.put("phone",phone);

        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();
        //JsonObjectRequest
        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
            //    Log.e("ddddddddddddddddd", response.toString());
                onCallBack.onUpoadSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            //    Log.e("hhhhhh","lsjdfqlfmq");
               // Log.e("hhhhhh", error.getMessage());
                onCallBack.onUpoadFailed(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };
        MySingleton.getInstance(context).addToRequestQueue(JOPR);
    }



    public void createUssd(String phonenumber, String choiceoperateur,String  prefix, String phone,int type,String code,String copagnie_name,String radicale,String url, OnCallBack onCallBack) {//  get post from online sql
       /* Log.e("listCLients", "ACTION PUB");
        Log.e("copagnie_name","envoie "+copagnie_name );
        Log.e("copagnie_name","phonenumber "+phonenumber );
        Log.e("copagnie_name","radicale "+radicale );*/
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("type", type);
            jsonBodyObj.put("prefix", prefix);
            jsonBodyObj.put("code", code);
            jsonBodyObj.put("phone", phone);
            jsonBodyObj.put("numero", phonenumber);
            jsonBodyObj.put("operateur", choiceoperateur);
            jsonBodyObj.put("copagnie_name", copagnie_name);
            jsonBodyObj.put("radicale", radicale);

        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();
        //JsonObjectRequest
        JsonArrayRequest JOPR = new JsonArrayRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
              //  Log.e("hhhhhfffffffffffffffffffh","receptions");
              //  Log.e("hhhhhfffffffffffffffffffh", response.toString());
                onCallBack.onUpoadSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e("numbersddd", error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };



        MySingleton.getInstance(context).addToRequestQueue(JOPR);

        /////////////////////////////

    }




    public void activeuserlocal(String idonline,String phone,String prefix,String codedepayement,String url, OnCallBack onCallBack) {//  get post from online sql
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("idonline", idonline);
            jsonBodyObj.put("phone", phone);
            jsonBodyObj.put("prefix", prefix);
            jsonBodyObj.put("codedepayement", codedepayement);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();
        //JsonObjectRequest
        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
           //     Log.e("ddddddddddddddddd", response.toString());
                onCallBack.onUpoadSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e("hhhhhh","lsjdfqlfmq");
                // Log.e("hhhhhh", error.getMessage());
                onCallBack.onUpoadFailed(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };
        MySingleton.getInstance(context).addToRequestQueue(JOPR);
        /////////////////////////////

    }








/*

    public void createUssd(String phonenumber, String choiceoperateur,String  prefix, String phone,String url, OnCallBack onCallBack) {//  get post from online sql
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("type", 1);
            jsonBodyObj.put("prefix", prefix);
            jsonBodyObj.put("phone", phone);
            jsonBodyObj.put("numero", phonenumber);
            jsonBodyObj.put("operateur", choiceoperateur);

        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();
        //JsonObjectRequest
        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                Log.e("ddddddddddddddddd", response.toString());
                onCallBack.onUpoadSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Log.e("numbersddd", error.getMessage());
                onCallBack.onUpoadFailed(error);

            }
        }){*/
       /*     @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };



        MySingleton.getInstance(context).addToRequestQueue(JOPR);

        /////////////////////////////

    }*/
///////////////////////////////////////////////////////////////////////

    public void getPubOnline(String prefix,String phone,String url, OnCallBack onCallBack) {//  get post from online sql
       // Log.e("listCLients", "ACTION PUB");
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("prefix", prefix);
            jsonBodyObj.put("phone", phone);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();
        //JsonObjectRequest
        JsonArrayRequest JOPR = new JsonArrayRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
               /* Log.e("listCLients", "ACTION PUB dddddddddddddd");
                Log.e("listCLients", response.toString());*/
                onCallBack.onUpoadSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e("numbersddd", error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };



        MySingleton.getInstance(context).addToRequestQueue(JOPR);

        /////////////////////////////

    }

/////////////////////////////////////////











    ///////////////////////////////////////////////////////////////////////

    public void getAllModificationUser(String prefix,String phone,String url, OnCallBack onCallBack) {//  get post from online sql
        //Log.e("listCLients", "ACTION PUB");
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("prefix", prefix);
            jsonBodyObj.put("phone", phone);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();
        //JsonObjectRequest
        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
               // Log.e("ddddddddddddddddd", response.toString());
                onCallBack.onUpoadSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e("hhhhhh","lsjdfqlfmq");
                // Log.e("hhhhhh", error.getMessage());
                onCallBack.onUpoadFailed(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };
        MySingleton.getInstance(context).addToRequestQueue(JOPR);
    }

/////////////////////////////////////////





    //////////////////////////////////////////
    public void getownussd(String prefix,String phone,String url, OnCallBack onCallBack) {//  get post from online sql
        // Log.e("listCLients", "ACTION DDDJJDKDKKDK");
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("prefix", prefix);
            jsonBodyObj.put("phone", phone);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();
        //JsonObjectRequest
        JsonArrayRequest JOPR = new JsonArrayRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //  Log.e("listCLients", response.toString());
                onCallBack.onUpoadSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e("numbersddd", error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };



        MySingleton.getInstance(context).addToRequestQueue(JOPR);

        /////////////////////////////

    }









    ///////////////////////////////////////////////
    //////////////////////////////////////////
    public void getCientOnline(String prefix,String phone,String url, OnCallBack onCallBack) {//  get post from online sql
       // Log.e("listCLients", "ACTION DDDJJDKDKKDK");
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("prefix", prefix);
            jsonBodyObj.put("phone", phone);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();
        //JsonObjectRequest
        JsonArrayRequest JOPR = new JsonArrayRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
              //  Log.e("listCLients", response.toString());
                onCallBack.onUpoadSuccess(response);
             }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e("numbersddd", error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };



        MySingleton.getInstance(context).addToRequestQueue(JOPR);

        /////////////////////////////

    }

/////////////////////////////////////////
public void sendOnline(String prefix, String phone,String operateur, String url, OnCallBack onCallBack) {//  get post from online sql

    JSONObject jsonBodyObj = new JSONObject();
    try{
        jsonBodyObj.put("prefix", prefix);
        jsonBodyObj.put("phone", phone);
        jsonBodyObj.put("operateur", operateur);
    }catch (JSONException e){
        e.printStackTrace();
    }
    final String requestBody = jsonBodyObj.toString();

    JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
            url, null, new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            //  VolleyLog.v("Response:%n %s", response.toString(4));
            //  populateLessonDetails(myActiveLessonURLFiltered);
        //    Log.e("numbers", response.toString());
            try {
                //String value=response.getString("value");
                String value=response.getString("value");
                onCallBack.onUpoadSuccess(response.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //  VolleyLog.e("Error: ", error.getMessage());
            //    Log.e("numbersddd", error.getMessage());
            onCallBack.onUpoadFailed(error);
        }
    }){
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json");
            return headers;
        }


        @Override
        public byte[] getBody() {
            try {
                return requestBody == null ? null : requestBody.getBytes("utf-8");
            } catch (UnsupportedEncodingException uee) {
                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                        requestBody, "utf-8");
                return null;
            }
        }


    };

    MySingleton.getInstance(context).addToRequestQueue(JOPR);

    /////////////////////////////

}
    public void sendCodeOnline(String prefix, String phone, String code,String url, OnCallBack onCallBack){//  get post from online sql

        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("prefix", prefix);
            jsonBodyObj.put("phone", phone);
            jsonBodyObj.put("code", code);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                //  VolleyLog.v("Response:%n %s", response.toString(4));
                //  populateLessonDetails(myActiveLessonURLFiltered);
                try {
                    String value=response.getString("value");
                //    Log.e("numbers____value", value);
                    if(Boolean.parseBoolean(value)){// si c est true
                      //  Log.e("numbers la", response.toString());
                        onCallBack.onUpoadSuccess(response);

                    }else{// si c est true
                     //   Log.e("numbers", response.toString());
                        onCallBack.onUpoadSuccess(value);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  VolleyLog.e("Error: ", error.getMessage());
                // Log.e("numbersddd", error.getMessage());
                onCallBack.onUpoadFailed(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };

        MySingleton.getInstance(context).addToRequestQueue(JOPR);

        /////////////////////////////

    }

    public void sendImageAndNameOnline(String prefix, String phone, String image,String extension,String name,String url, OnCallBack onCallBack){//  get post from online sql
     //   Log.e("images____value", "value");
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("prefix", prefix);
            jsonBodyObj.put("phone", phone);
            jsonBodyObj.put("image", image);
            jsonBodyObj.put("extension",extension);
            jsonBodyObj.put("name", name);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                //  VolleyLog.v("Response:%n %s", response.toString(4));
                //  populateLessonDetails(myActiveLessonURLFiltered);
                try {
                    String value=response.getString("value");
                //    Log.e("images____value", value);
                    if(Boolean.parseBoolean(value)){// si c est true
                    //    Log.e("numbers la", response.toString());
                        onCallBack.onUpoadSuccess(response);

                    }else{// si c est false
                    //    Log.e("numbers", response.toString());
                        onCallBack.onUpoadSuccess(value);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  VolleyLog.e("Error: ", error.getMessage());
                // Log.e("numbersddd", error.getMessage());
                onCallBack.onUpoadFailed(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };

        MySingleton.getInstance(context).addToRequestQueue(JOPR);

        /////////////////////////////

    }

    public void sendLanguage(String prefix, String phone, String language,String tokenfirebase,String url, OnCallBack onCallBack){//  get post from online sql
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("prefix", prefix);
            jsonBodyObj.put("phone", phone);
            jsonBodyObj.put("language", language);
            jsonBodyObj.put("tokenfirebase", tokenfirebase);

        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                //  VolleyLog.v("Response:%n %s", response.toString(4));
                //  populateLessonDetails(myActiveLessonURLFiltered);
                try {
                    String value=response.getString("value");
                    if(Boolean.parseBoolean(value)){// si c est true
                        onCallBack.onUpoadSuccess(response);

                    }else{// si c est false
                      //  Log.e("language", response.toString());
                        onCallBack.onUpoadSuccess(value);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  VolleyLog.e("Error: ", error.getMessage());
                // Log.e("numbersddd", error.getMessage());
                onCallBack.onUpoadFailed(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };

        MySingleton.getInstance(context).addToRequestQueue(JOPR);

        /////////////////////////////

    }



    //////////////////////////////////////////////
    public void actiOwnussd(String codedepayement,String code, String codeussd,String  name, String numero,String operateur,String prefix,String phone,String url, OnCallBack onCallBack) {//  get post from online sql
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("codedepayement", codedepayement);
            jsonBodyObj.put("code", code);
            jsonBodyObj.put("codeussd", codeussd);
            jsonBodyObj.put("name", name);
            jsonBodyObj.put("numero", numero);
            jsonBodyObj.put("operateur", operateur);
            jsonBodyObj.put("prefix", prefix);
            jsonBodyObj.put("phone", phone);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();
        //JsonObjectRequest
        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
            //    Log.e("atoubar", response.toString());
                onCallBack.onUpoadSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
           //     Log.e("hhhhhh","lsjdfqlfmq");
                // Log.e("hhhhhh", error.getMessage());
                onCallBack.onUpoadFailed(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }


        };
        MySingleton.getInstance(context).addToRequestQueue(JOPR);
        /////////////////////////////

    }



    public interface OnCallBack {
        void onUpoadSuccess(String imagUrl);
        void onUpoadSuccess(JSONObject response);
        void onUpoadSuccess(JSONArray response);

        void onUpoadFailed(Exception e);
    }
}
