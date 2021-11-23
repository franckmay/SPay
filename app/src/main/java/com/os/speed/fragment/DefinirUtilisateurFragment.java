package com.os.speed.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.os.speed.BuildConfig;
import com.os.speed.R;
import com.os.speed.service.getCodeOnlineservice;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.os.speed.StiticSimple.PERMISSION_CAMERA;
import static com.os.speed.StiticSimple.PERMISSION_EXTERNAL_STORAGE;
import static com.os.speed.StiticSimple.REQUEST_TAKE_PHOTO;
import static com.os.speed.staticclass.encodeBitmap;
import static com.os.speed.staticclass.getFileExtention;

public class DefinirUtilisateurFragment extends Fragment {
    private String encodeImageString="";
    private Button button;
    private CircularImageView circle_image_profile;
    private EditText et_user_name;
    public ProgressDialog progressDialog;
    public String token,prefix,phone,avatar,name;
    String extension =null;
    public DefinirUtilisateurFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_definir_utilisateur, container, false);
         button = (Button) view.findViewById(R.id.btn_set_profil_next);
         circle_image_profile =  view.findViewById(R.id.circle_image_profile);
         et_user_name =  view.findViewById(R.id.et_user_name);

         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 token=getArguments().getString("token");
                 prefix=getArguments().getString("prefix");
                 phone=getArguments().getString("phone");
                 avatar=getArguments().getString("avatar");
                 name=et_user_name.getText().toString();

               /*  NavHostFragment.findNavController(DefinirUtilisateurFragment.this)
                         .navigate(R.id.action_definirUtilisateurFragment_to_traductionFragment);*/
                 if(imageUri!=null){
                     extension =getFileExtention(getContext(),imageUri);
                 }
                 sendImageAndNameInbdOnline( prefix,  phone, encodeImageString,extension, name);
             }
         });
     /*   circle_image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });*/
        return view;
    }





    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void sendImageAndNameInbdOnline(String prefix, String phone,String imageFileName,String extension,String name) {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.show();
        new getCodeOnlineservice(getContext()).sendImageAndName(prefix,phone,imageFileName, extension,name ,new getCodeOnlineservice.OnCallsBack() {
            @Override
            public void onUpoadSuccess(String value) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();
              /*  NavHostFragment.findNavController(DefinirUtilisateurFragment.this)
                        .navigate(R.id.action_definirUtilisateurFragment_to_traductionFragment);*/

                // et_number1.setText(String.valueOf(code));
               /* et_number2.setText(String.valueOf(code.charAt(1)));
                et_number3.setText(String.valueOf(code.charAt(2)));
                et_number4.setText(String.valueOf(code.charAt(3)));*/
           /*     boolean valeur=Boolean.valueOf(value);
                if(!valeur) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), R.string.try_againe, Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(CodeVerificationFragment.this)
                            .navigate(R.id.action_codeVerificationFragment_to_phoneLoginFragment);
                }
                Log.e("valeur",String.valueOf(valeur));
*/
            }

            @Override
            public void onUpoadSuccess(JSONObject response) {

             //   try {
                  /*  String token=response.getString("token");
                    String prefix=response.getString("prefix");
                    String phone=response.getString("phone");
                    String avatar=response.getString("avatar");*/
                    //Log.e("numbersddd", response.toString());
                    //   JWT jwt=new JWT(token);
                    //   Log.e("token", token);
                    //Log.e("phone", phone);
                    progressDialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString("token", token);
                    bundle.putString("prefix", prefix);
                    bundle.putString("phone", phone);
                    bundle.putString("avatar", avatar);
                    NavHostFragment.findNavController(DefinirUtilisateurFragment.this)
                            .navigate(R.id.action_definirUtilisateurFragment_to_traductionFragment,bundle);


           /*     } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                //

            }

            @Override
            public void onUpoadSuccess(List<?> listPhone) {

            }

            @Override
            public void onUpoadFailed(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////












    private void checkCameraPermission() {

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(),"action 1" ,Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA);
        }else
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(),"action 2",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_EXTERNAL_STORAGE);
        }else{

            openCamera();
        }
    }
    private static String mCurrentPhotoPath;
    private String imageFileName;
    private Uri imageUri;
    @SuppressLint("QueryPermissionsNeeded")
    public void openCamera(){
        Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(!cameraIntent.equals(null)){
            File imageFile=null;
            try{
                imageFile=createImageFile(getContext(),imageFileName);
            }catch (IOException e){
                e.printStackTrace();
            }
            if(imageFile!=null){
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageUri= FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID+".provider",imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                intent.putExtra("listPhotoName",imageFileName);
                startActivityForResult(intent,REQUEST_TAKE_PHOTO);

            }


        }
    }



    public static File createImageFile(Context context, String imageFileName) throws IOException {

        String racine ="MaxAds";
        String MediaVoice="Media/images/profil";
        String pa=MediaVoice;
      //  creatFolder(context,racine, pa);
        /////  String path_save = context.getExternalFilesDir(racine).getAbsolutePath()+"/"+pa;
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "PROFIL";
        File storageDir = context.getExternalFilesDir(pa);
        File image = File.createTempFile(imageFileName,  /* prefix */".jpg",         /* suffix */storageDir      /* directory */);
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode==REQUEST_TAKE_PHOTO&&resultCode==RESULT_OK){
              //  uploadToOnline();
                try{
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imageUri);
                    circle_image_profile.setImageBitmap(bitmap);
                    encodeImageString=encodeBitmap(bitmap);
                  //  progressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }

        }
    }




}
