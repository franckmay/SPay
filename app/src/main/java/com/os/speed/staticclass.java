package com.os.speed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.os.speed.broadreceiver.NotificationReceiver;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class staticclass extends Application
{
    public static final String CHANNEL_ID = "exampleChannel";
    public static final String NOTIFICATION_CHANNEL_ID = "com.os.speed.service.text";
    static public void changeLangage(Context context,String language){
        Locale locale=new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        context.getResources().updateConfiguration(configuration,context.getResources().getDisplayMetrics());
    }

    static public boolean isURLReachable(Context context, String urld) {

        URL url = null;
        try {
            url = new URL(urld);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);          // 10 s.
            connection.connect();
            int code = connection.getResponseCode();
            if (code == 200) {
                Log.e("isddpubdddd", "vrais");
                return true;
            } else {
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("isddpubdddd", "faux");
        return false;

    }

    public static String jours() {
        Date dt = new Date();
        int jour = dt.getDay();
        int mont = dt.getMonth();
        int year = dt.getYear();
        String jours = jour + "/" + mont + "/" + year;
        return jours;
    }
    public  static boolean isNumeric(String str){
        try{
            Double.parseDouble(str);
            return true;

        }catch (NumberFormatException e){
            return false;
        }
    }
    public static boolean checkNetworkConnection(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission")
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null&&networkInfo.isConnected());

    }
    public static String getCurrentDate(){
        Calendar curentDateTime =Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
        String today=format.format(curentDateTime.getTime());
        String currentTime=format.format(curentDateTime.getTime());
        return today+currentTime;

    }
    public static String getCurrentDay(){
        Calendar curentDateTime =Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
        String today=format.format(curentDateTime.getTime());
      //  String currentTime=format.format(curentDateTime.getTime());
        return today;

    }
    public static boolean checkPermissionFromDevice(Context context){
        int write_external_strorage_result= ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result= ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO);
        return  write_external_strorage_result==PackageManager.PERMISSION_DENIED||record_audio_result==PackageManager.PERMISSION_DENIED;
    }
    public static boolean checkPermissionSmS(Context context){
        int write_external_strorage_result= ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);
        int record_audio_result= ContextCompat.checkSelfPermission(context,Manifest.permission.RECEIVE_SMS);
        return  write_external_strorage_result==PackageManager.PERMISSION_DENIED||record_audio_result==PackageManager.PERMISSION_DENIED;
    }

    public static void creatAlfolder(Context context){
        String racine ="MaxAds";
        creatFolder(context,racine, "Backup");
        creatFolder(context,racine, "Backup/Wallpapers");

        creatFolder(context,racine, "Databases");
        creatFolder(context,racine, "Media");
        creatFolder(context,racine, "Media/Wallpapers");
        creatFolder(context,racine, "Media/Animated Gifs");
        creatFolder(context,racine, "Media/Audio");
        creatFolder(context,racine, "Media/documents");
        creatFolder(context,racine, "Media/documents/send");
        creatFolder(context,racine, "Media/documents/received");
        creatFolder(context,racine, "Media/images");
        creatFolder(context,racine, "Media/images/profil");
        creatFolder(context,racine, "Media/images/send");
        creatFolder(context,racine, "Media/images/received");
        creatFolder(context,racine, "Media/profils photos");
        creatFolder(context,racine, "Media/stickers");
        creatFolder(context,racine, "Media/video");
        creatFolder(context,racine, "Media/voice Notes");
        creatFolder(context,racine, "Media/voice Notes/send");
        creatFolder(context,racine, "Media/voice Notes/received");

    }
    public static void creatFolder(Context context,String racine,String foldername) {
        File file=new File(context.getExternalFilesDir(racine).getAbsolutePath()+"/"+foldername);
        if (!file.exists()) {
            file.mkdir();
            Log.e("volid", "PATH "+file.getPath());
            Toast.makeText(context, " Succesful", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, " folder already exist", Toast.LENGTH_SHORT).show();
            Log.e("volid", "PATH "+file.getPath());

        }
    }

    public static  String pathRacine(Context context,String name,String extension) {
        String racine ="MaxAds";
        String MediaVoice="Media/documents";
       // String pa=MediaVoice+ "/" +getCurrentDay();
      //  Log.e("curendate",getCurrentDay());
        creatFolder(context,racine, MediaVoice);
        String path_save = context.getApplicationContext().getExternalFilesDir(racine).getAbsolutePath()+ File.separator +MediaVoice+ File.separator + UUID.randomUUID().toString() + name+"."+extension;
        return  path_save;
    }

    public static File createFile(Context context,byte[] fileData,String name,String extension) {
        try {
            //Create directory..
           /* File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/YOUR_FOLDER_NAME");
            File dir = new File(root + File.separator);
            if (!dir.exists()) dir.mkdir();*/
            //Create file..
            File file = new File(pathRacine( context, name, extension));
            file.createNewFile();

            FileOutputStream out = new FileOutputStream(file);
            out.write(fileData);
            out.close();
          return file;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    public static long getTime() throws Exception {
        String url = "https://time.is/Unix_time_now";
        Document doc=null;
        try{
          //   doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);

            doc = Jsoup.connect(url).get();


        }catch (Exception e) {
            e.printStackTrace();
            Log.e("datess","erree "+e.getMessage()) ;
        }

        if(doc!=null)
             Log.e("datess","la la la  ") ;
        else
            Log.e("datess","je suis null ") ;
        String[] tags = new String[] {
                "div[id=time_section]",
                "div[id=clock0_bg]"
        };
        Elements elements= doc.select(tags[0]);
        for (int i = 0; i <tags.length; i++) {
            elements = elements.select(tags[i]);
        }
        return Long.parseLong(elements.text() + "000");
    }
  /*  public static long getTime() throws Exception {
        String url = "https://time.is/Unix_time_now";
        Log.e("datess","la la la  ") ;
        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
        Log.e("datess","la la cccccccccccccccccccla  ") ;
        String[] tags = new String[] {
                "div[id=time_section]",
                "div[id=clock0_bg]"
        };
        Elements elements= doc.select(tags[0]);
        for (int i = 0; i <tags.length; i++) {
            elements = elements.select(tags[i]);
        }
        return Long.parseLong(elements.text());
    }*/
    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
       // SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(cal.getTime());
    }
    public static void getDatInternet(){
        try {
            Log.e("datess",String.valueOf(getTime())) ;
           // Log.e("datess",getDate(getTime())) ;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("datess","getDate(getTime()) "+e.getMessage()) ;
        }
    }

    public static String getFileName(Uri uri,Context context){
        String result=null;
        if(uri.getScheme().equals("content")){
            Cursor cursor=context.getContentResolver().query(uri,null,null,null,null);
            try{
                if(cursor!=null && cursor.moveToFirst()){
                    result=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }

            }finally {
                cursor.close();
            }
        }
        if(result==null){
            result=uri.getPath();
            int cut=result.lastIndexOf('/');
            if(cut!=-1){
                result=result.substring(cut+1);
            }
        }
        return result;
    }

    public static String getFileNameOnli(Uri uri,Context context) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        if (result.indexOf(".") > 0)
            result = result.substring(0, result.lastIndexOf("."));
        return result;
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;
        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }
        return extension;
    }

    public static Bitmap pdfToBitmap(File pdfFile) {
        Bitmap bitmap = null;
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));
            final int pageCount = renderer.getPageCount();
            if(pageCount>0){
                PdfRenderer.Page page = renderer.openPage(0);
                int width = (int) (page.getWidth());
                int height = (int) (page.getHeight());
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                renderer.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }


   // public static ArrayList<Bitmap> pdfToBitmaps(Context context,File pdfFile) {
    public static Bitmap pdfToBitmaps(Context context,File pdfFile) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        Bitmap bitmapd = null;
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));

            Bitmap bitmap;
          //  final int pageCount = renderer.getPageCount();
            final int pageCount = 1;
            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = renderer.openPage(i);

                int width = context.getResources().getDisplayMetrics().densityDpi / 72 * page.getWidth();
                int height =  context.getResources().getDisplayMetrics().densityDpi / 72 * page.getHeight();
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

              //  bitmaps.add(bitmap);
                bitmapd=bitmap;
                // close the page
                page.close();

            }

            // close the renderer
            renderer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //return bitmaps;
        return bitmapd;

    }



    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("TAG", "getRealPathFromURI Exception de ddddddddddddd: " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getPDFPath(Context context,Uri uri){

        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static  String encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[]bytesofimage=byteArrayOutputStream.toByteArray();
        String temp= Base64.encodeToString(bytesofimage, Base64.DEFAULT);
        return temp;
    }
    public static  Bitmap StringToBitmap(String encodedString) {
        try{
            byte[]encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);
            return bitmap;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

public static  byte[]getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,outputStream);
        return outputStream.toByteArray();
}

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
/*
public void postData(Context context){
        final ProgressDialog loading=new ProgressDialog(context);
        loading.setMessage(getString(R.string.strPatienter));
        loading.show();
        loading.setCanceledOnTouchOutside(false);
        JSONObject object =new JSONObject();
        try{
            object.put("user",numero.getText());
        }catch (JSONException e){
            e.printStackTrace();

        }
//    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,urlPhoneNUmber,,object,new Res)









        //json response code
         StringRequest stringRequest=new StringRequest(Request.Method.GET, urlPhoneNUmber,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         try {
                             Log.e("JSON", response);
                             loading.dismiss();
                             //Internet_Check.setVisibility(View.GONE);
                             JSONObject jsonObject = new JSONObject(response);
                             String error_status = jsonObject.getString("httpStatus");
                             if (error_status.equals("")) {
                                 String error_msg = jsonObject.getString("message");
                                 ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.Theme_AppCompat);
                                 final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                 alertDialogBuilder.setTitle("Message");
                                 alertDialogBuilder.setCancelable(false);
                                 alertDialogBuilder.setMessage(error_msg);
                                 alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {

                                     }
                                 });
                                 alertDialogBuilder.show();

                             } else if (error_status.equals("OK")) {
                                 JSONArray jArray = jsonObject.getJSONArray("body");
                                 if (jArray.length() == 0) {
                                     ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.Theme_AppCompat);
                                     final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                     alertDialogBuilder.setTitle("Message");
                                     alertDialogBuilder.setCancelable(false);
                                     alertDialogBuilder.setMessage("No any Article data found Try later");
                                     alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {

                                         }
                                     });
                                     alertDialogBuilder.show();
                                 } else {
                                     //onPostExecute(jArray);
                                 }
                             }
                         } catch (JSONException e) {
                             e.printStackTrace();
                             Log.d("Tag", e.getMessage());
                         }

                     }
                 },
                 new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                       loading.dismiss();
                       Internet_Check.setVisibility(View.VISIBLE);
                       if(error instanceof TimeoutError||error instanceof NoConnectionError){
                           Internet_Msg.setText("No connection\n Connection time out error please try again");

                       }else if(error instanceof AuthFailureError){
                           Internet_Msg.setText("Connection Error \n Authentication failure connection error please try later");

                       }else if (error instanceof ServerError){
                           Internet_Msg.setText("Connection Error \n AServer Connectin error please try again");
                       }else if (error instanceof NetworkError){
                           Internet_Msg.setText("Connection Error \n Netework Connectin error please try again");
                       }else if (error instanceof ParseError){
                           Internet_Msg.setText("Connection Error \n Netework Connectin error please try again");
                       }
                     }
                 }){
             @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                 HashMap<String, String>headers=new HashMap<String, String>();
                 headers.put("Autorization",valeur1);
                 return headers;
             }
             protected void onPostExecute(JSONArray result){
                 List <DataObject>data=new ArrayList<Object>();
                 data.equals(null);
                 try{
                     for(int i=0;i<result.length();i++){
                         JSONObject json_data=json_data.getJSONObject("category");
                         DataObject report=new DataObject();
                         report.mText1=json_data.getString("id");
                         report.mText1=json_data.getString("title");
                         report.mText1=json_data.getString("shortDescription");
                         report.mText1=json_data.getString("resourceLink");
                         report.mText1=json_data.getString("createAt");
                         report.mText1=json_data.getString("name");
                         report.mText1=json_data.getString("thumbnailIMageUrl");
                         data.add(report);

                     }

                 }catch (JSONException e){
                     Toast.makeText(Articles_Screen.this,e.toString(),Toast.LENGTH_SHORT).show();
                 }


             }
         };
}
*/

    public static String getFileExtention(Context context,Uri uri){
        ContentResolver contentResolver=context.getApplicationContext().getContentResolver();
                MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public  static String recursekeys(JSONObject jObj,String findkey)throws JSONException{
        String finalVlue="";
        if(jObj==null){
            return "";
        }
        Iterator<String> keyItr=jObj.keys();
        Map<String,String>map=new HashMap<>();
        while (keyItr.hasNext()){
            String key=keyItr.next();
            map.put(key,jObj.getString(key));
        }
        for(Map.Entry<String,String>e:(map).entrySet()){
            String key=e.getKey();
            if(key.equalsIgnoreCase(findkey)){
                return  jObj.getString(key);
            }
            // read value
            Object value=jObj.get(key);
            if(value instanceof JSONObject){
                finalVlue=recursekeys((JSONObject)value,findkey);

            }
        }
        // key is not found
        return finalVlue;
    }

    // String to date

      public static Date StringTodate(String dtStart){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date =null;
        try {
            date = format.parse(dtStart);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String DateToString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        // Date date = new Date();
        String dateTime = dateFormat.format(date);
        System.out.println("Current Date Time : " + dateTime);
        return  dateTime;
    }

    public static void showNotifications(String title, String body,String url,Context context ) {
        int value=new Random().nextInt();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // plus petit
        RemoteViews collapsedView = new RemoteViews(context.getPackageName(),
                R.layout.notification_collapssed);

        collapsedView.setTextViewText(R.id.text_new_message, title);
        collapsedView.setTextViewText(R.id.text_view_colla, body);

        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            //   imageView.setImageBitmap(resource);
                            collapsedView.setImageViewBitmap(R.id.image_view_expand, resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });


        }else{
            collapsedView.setImageViewResource(R.id.image_view_expand, R.drawable.speed);
        }







// grand
        RemoteViews expandedView = new RemoteViews(context.getPackageName(),
                R.layout.notification_expended);
        expandedView.setTextViewText(R.id.text_titre, title);
        expandedView.setTextViewText(R.id.text_body, body);
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                         //   imageView.setImageBitmap(resource);
                            expandedView.setImageViewBitmap(R.id.image_view_expanded,  resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });






        }else{
            expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.speed);
        }



        Intent clickIntent = new Intent(context, NotificationReceiver.class);
        clickIntent.putExtra("vaulue",value);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context,
                value, clickIntent, 0);









        //




        //collapsedView.setBoolean(R.id.sw);


        // expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.speed);

        // gestion des click
        collapsedView.setOnClickPendingIntent(R.id.liner_collaps, clickPendingIntent);
        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_MIN);
            notificationChannel.setDescription("code sphere");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);

            notificationManager.createNotificationChannel(notificationChannel);
        }



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                // .setDefaults(Notification.DEFAULT_ALL)
                //.setWhen(System.currentTimeMillis())

                // .setContentTitle(title)
                //.setContentText(body)
                //.setSmallIcon(R.drawable.ic_launcher_background)

                .setSmallIcon(R.drawable.speed)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentInfo("info");

        //  notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
        notificationManager.notify(value, notificationBuilder.build());





    }

}