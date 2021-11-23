package com.os.speed;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.os.speed.modele.OwnUssd;
import com.os.speed.modele.Users;
import com.os.speed.service.CLientOnlineservice;
import com.os.speed.sqlite.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.os.speed.StiticSimple.LAUNCH_SECOND_ACTIVITY;
import static com.os.speed.StiticSimple.MONTANTSCAN;
import static com.os.speed.StiticSimple.NUMEROACHILLE;
import static com.os.speed.StiticSimple.NUMEROACHILLEMTN;
import static com.os.speed.StiticSimple.REQUEST_EXCECUTE_USSD;
import static com.os.speed.StiticSimple.REQUEST_TAKE_PHOTO;
import static com.os.speed.StiticSimple.urlMaxAdd;
import static com.os.speed.staticclass.isURLReachable;
import static com.os.speed.staticclass.jours;

public class GenCodeBar extends AppCompatActivity {
    LinearLayout btpdf;
    Button bt_textx;
    ImageView ivOutput, btnBack,share;
    Bitmap bitmap;
    int pageWidth = 1200;
    String racine = "Gencode";
    String code = "";
    String codeussd = "";
    String name = "";
    String numero = "";
    String operateur = "";
    TextView txt_indcation;
    //Couleru et police
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 36,
            Font.NORMAL, BaseColor.RED);
    private static Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 36,
            Font.NORMAL, BaseColor.BLUE);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    //////////////////////////////

    /////////////////////////////
    Dialog dialog1;
    Dialog dialog2;
    String codedepayement;
    String codeussdi;
    Users userocal;
    DBHelper dbHelper;
    ////////////////
    OwnUssd ussd;
    String nomfichier="code";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_code_bar);
        LinearLayout layout =(LinearLayout)findViewById(R.id.backgroundgencode);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        //Log.e("sdkligne",String.valueOf(sdk));
        if(sdk <= Build.VERSION_CODES.N) {
            //layout.setBackground(ContextCompat.getDrawable(this, R.drawable.ready));
            layout.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.speed2));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.prim));
        }
        dbHelper = new DBHelper(GenCodeBar.this);
        userocal = dbHelper.getUserLocal(1);
        btpdf = findViewById(R.id.bt_generatePdf);
        share = findViewById(R.id.share);
        bt_textx = findViewById(R.id.bt_textx);
        ivOutput = findViewById(R.id.iv_output);
        txt_indcation = findViewById(R.id.txt_indcation);
        btnBack = findViewById(R.id.btn_back);
        Intent intent = this.getIntent();
        code = intent.getStringExtra("code");
        codeussd = intent.getStringExtra("codeussd");  //code,codeussd,name,numero,operateur
        name = intent.getStringExtra("name");
        numero = intent.getStringExtra("numero");
        operateur = intent.getStringExtra("operateur");
        String type = intent.getStringExtra("type");
        int ussd_id = intent.getIntExtra("ussd_id", 0);
        String ussd_idonline = intent.getStringExtra("ussd_idonline");
        // Log.e("id_ussd_online",ussd_idonline);
        String datetime = intent.getStringExtra("datetime");
        String dateupdate = intent.getStringExtra("dateupdate");
        String expiration_at = intent.getStringExtra("expiration_at");


        ussd = new OwnUssd();
        ussd.setUssd_idonline(ussd_idonline);
        ussd.setUssd_id(ussd_id);
        ussd.setExpiration_at(expiration_at);
        ussd.setCode(code);


        boolean active = Boolean.parseBoolean(intent.getStringExtra("active"));
        Log.e("viill", "588555 " + String.valueOf(active));
        if (active) {
           // outPath = Environment.getExternalStorageDirectory() + "/wintime";
            creatFolder(this, racine, "Backup");
            generateCodeBar();
            btpdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creatVoid();
                    //  generatePDF();
                     nomfichier = numero + new Date();
                    // File file=new File(outPath+"/"+nomfichier+".pdf");
                    // File file = new File(getExternalFilesDir(racine).getAbsolutePath() + "/" +nomfichier+".pdf");
                    File file = new File(getExternalFilesDir(racine).getAbsolutePath() + "/" + "Backup/" + nomfichier + ".pdf");
                    Log.e("nomfichier", file.getPath());
                    //createPDF(nomfichier,1);
                    generatePDF(nomfichier);
                    afficher(file);
                }
            });
            bt_textx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creatVoid();
                    //  generatePDF();

                    nomfichier = numero + new Date();
                    // File file=new File(outPath+"/"+nomfichier+".pdf");
                    // File file = new File(getExternalFilesDir(racine).getAbsolutePath() + "/" +nomfichier+".pdf");
                    File file = new File(getExternalFilesDir(racine).getAbsolutePath() + "/" + "Backup/" + nomfichier + ".pdf");
                    Log.e("nomfichier", file.getPath());
                    //createPDF(nomfichier,1);
                    generatePDF(nomfichier);
                    afficher(file);
                }
            });
            txt_indcation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creatVoid();
                    //  generatePDF();

                    nomfichier = numero + new Date();
                    // File file=new File(outPath+"/"+nomfichier+".pdf");
                    // File file = new File(getExternalFilesDir(racine).getAbsolutePath() + "/" +nomfichier+".pdf");
                    File file = new File(getExternalFilesDir(racine).getAbsolutePath() + "/" + "Backup/" + nomfichier + ".pdf");
                    Log.e("nomfichier", file.getPath());
                    //createPDF(nomfichier,1);
                    generatePDF(nomfichier);
                    afficher(file);
                }
            });
        } else {

            //////////////////// procecus de paiement /////////////////////////////////////////////////////
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.waiting));
            dialog1 = new Dialog(this);
            dialog1.setContentView(R.layout.custom_dialog_insert);
            dialog2 = new Dialog(this);
            dialog2.setContentView(R.layout.custom_dialog_user);

            Button getcode = dialog1.findViewById(R.id.getcode);
           // TextView numeroachille = dialog2.findViewById(R.id.numeroachille);
           // numeroachille.setText(NUMEROACHILLEMTN);
            EditText code = dialog1.findViewById(R.id.code);
            ImageView sumitcode = dialog1.findViewById(R.id.sumitcode);

            //Log.e("isll", "user " + user.getPrefix());


            getcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1.dismiss();
                    dialog2.show();
                }
            });


            sumitcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Execution du code de payement
                    codedepayement = code.getText().toString();
                    // code.setText("");
                    dialog1.dismiss();
                    //progressDialog.show();

                    ////////////////////////////

                    progressDialog.show();
                    ///   //////////////////////////////////////////////////////////////////////////////////////////////////////////
                    actiOwnUSSDD();
                    /////////////////////////////////

                }
            });
            LinearLayout paieorange = dialog2.findViewById(R.id.paieorange);
            paieorange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Toast.makeText(getApplicationContext(), getString(R.string.nombreussmax), Toast.LENGTH_LONG).show();
                    //String ussd = client.getCodeussd() + "*" + client.getNumero() + "*" + montant + "#";
                    dialog2.dismiss();
                    codeussdi = "#100*1*1*" + NUMEROACHILLE + "*" + MONTANTSCAN + "#";
                    initCall();
                }
            });
         /*   LinearLayout paiemtn = dialog2.findViewById(R.id.paiemtn);
            paiemtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog2.dismiss();
                    codeussdi = "*126#";
                    initCall();
                }
            });
*/

            /////////////////////////////////////fin procecus de paiement


            ivOutput.setImageResource(R.drawable.speed);
            ivOutput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // payerment de frais d activation
                    dialog1.show();
                }
            });
        }


/////////////////////////////////////////////////////


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareFile();
            }
        });
        ////////////////////////////////
    }

    public  void createPdf(){
        nomfichier = numero + new Date();
        File file = new File(getExternalFilesDir(racine).getAbsolutePath() + "/" + "Backup/" + nomfichier + ".pdf");
        Log.e("nomfichier", file.getPath());
        generatePDF(nomfichier);
    }

    public void sharef( File file){

            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("application/pdf");
            // intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI);
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File from Webkul...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File from Webkul to purchase items...");
            startActivity(Intent.createChooser(intentShareFile, "Share File Webkul"));
    }
    public void shareFile(){
        File file = new File(getExternalFilesDir(racine).getAbsolutePath() + "/" + "Backup/" + nomfichier + ".pdf");
        if(file.exists()) {
            sharef( file);
        }else{
            createPdf();
            sharef( file);
        }

    }






    public void actiOwnUSSDD() {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isURLReachable(getApplicationContext(), urlMaxAdd)) {
                        activeOwnUssd(code, codeussd, name, numero, operateur, userocal.getPrefix(), userocal.getPhone());
                    } else {
                        progressDialog.dismiss();
                        showToast(getString(R.string.erreur_connextion));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    private void activeOwnUssd(String code, String codeussd, String name, String numero, String operateur, String prefix, String phone) {

        new CLientOnlineservice(this).activeOwnUssd(codedepayement, code, codeussd, name, numero, operateur, prefix, phone, new CLientOnlineservice.OnCallsBack() {

            @Override
            public void onUpoadSuccess(String code) {

            }

            @Override
            public void onUpoadSuccess(JSONObject response) {
                try {
                    String value = response.getString("value");
                    if (Boolean.parseBoolean(value)) {
                        String expiration = response.getString("expiration");
                        ussd.setActive(true);
                        ussd.setExpiration_at(expiration);
                        boolean v = dbHelper.updateOwnUssd(ussd);
                        Log.e("atoubar", "DDD " + String.valueOf(v));
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        finish();
                    } else {
                        showToast(getString(R.string.codenonreconu));
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onUpoadSuccess(JSONArray response) {

            }

            @Override
            public void onUpoadSuccess(List<?> listPhone) {

            }

            @Override
            public void onUpoadFailed(Exception e) {

            }
        });
    }


    public ProgressDialog progressDialog;

    public void showToast(final String toast) {
        runOnUiThread(() -> Toast.makeText(GenCodeBar.this, toast, Toast.LENGTH_SHORT).show());
    }

    public void initCall() {


        Intent callInn = new Intent(Intent.ACTION_CALL, ussdTocallableUri(codeussdi));
        //     startActivity(callInn);
        startActivityForResult(callInn, REQUEST_EXCECUTE_USSD);
    }

    public Uri ussdTocallableUri(String ussd) {
        String uriString = "";
        if (!ussd.startsWith("tel:"))
            uriString += "tel:";
        for (char c : ussd.toCharArray()) {
            if (c == '#')
                uriString += Uri.encode("#");
            else
                uriString += c;
        }
        return Uri.parse(uriString);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

 //   String outPath;

    /*public void createPDF(String nomfichier, int action) {
        Document doc = new Document();
        Image image1 = null;
        Drawable d;
        /////////////////////////////////////////
        /// parametrage du pdf
        Paragraph paynow = new Paragraph();
        paynow.add(new Chunk("SPE", blueFont));
        paynow.add(new Chunk("AD", redFont));
        paynow.setAlignment(Element.ALIGN_CENTER);
        Paragraph titre = new Paragraph(getString(R.string.codegenere), catFont);
        titre.setAlignment(Element.ALIGN_CENTER);
        Paragraph datejours;
        if (jours() != null) {
            datejours = new Paragraph("*" + jours() + "*", smallBold);
        } else {
            datejours = new Paragraph("*" + new Date() + "*", smallBold);
        }

        datejours.setAlignment(Element.ALIGN_CENTER);
        doc.addTitle(getString(R.string.codegenere));
        doc.addAuthor("ARINTECH");
        doc.addCreator("achille");
        //////////////////////////////////////
/////////////////////////////////////
        ///tableau
        PdfPTable tableau = new PdfPTable(4);
        try {
            tableau.setWidths(new float[]{1, 3, 1, 1});
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell = new PdfPCell(new Paragraph("getString(R.string.entresortie)"));
        //cell.setBackgroundColor(BaseColor.BLACK);
        PdfPCell cell1 = new PdfPCell(new Paragraph("getString(R.string.identif)"));
        // cell.setBackgroundColor(BaseColor.BLACK);
        PdfPCell cell2 = new PdfPCell(new Paragraph("getString(R.string.montant)"));
        // cell.setBackgroundColor(BaseColor.BLACK);
        PdfPCell cell3 = new PdfPCell(new Paragraph("getString(R.string.heure)"));
        // cell.setBackgroundColor(BaseColor.BLACK);
        tableau.addCell(cell);
        tableau.addCell(cell1);
        tableau.addCell(cell2);
        tableau.addCell(cell3);
        //////////////////////////////
        ///

        /*for(RapportJour rapportJour : listopejour) {
            InputStream ims = null;
            try {
                if(rapportJour.getTypeentre()==1) {
                    d = getResources().getDrawable(R.drawable.ic_action_sortie);
                }else{
                    d = getResources().getDrawable(R.drawable.ic_action_entre);
                }
                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapData = stream.toByteArray();
                image1 = Image.getInstance(bitmapData) ;
                image1.setWidthPercentage(1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BadElementException e) {
                e.printStackTrace();
            }
            PdfPCell cel = new PdfPCell();
            cel.setBackgroundColor(new BaseColor(255,255,255));
            cel.setFixedHeight(10);
            cel.setImage(image1);
            tableau.addCell(cel);
            tableau.addCell(rapportJour.getIdentifiant());
            tableau.addCell(String.valueOf(rapportJour.getMontant()));
            tableau.addCell(rapportJour.getHeure());

        }*/

       /* try {
            PdfWriter.getInstance(doc, new FileOutputStream(outPath + "/" + nomfichier + ".pdf"));
            doc.open();
            addEmptyLine(doc, new Paragraph(), 10);
            doc.add(paynow);
            addEmptyLine(doc, new Paragraph(), 5);
            doc.add(titre);
            addEmptyLine(doc, new Paragraph(), 2);
            doc.add(datejours);
            addEmptyLine(doc, new Paragraph(), 5);
            doc.add(tableau);
            doc.close();
            if (action == 1) {
                Toast.makeText(this, "save", Toast.LENGTH_LONG).show();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
*/
  /*  public void viewPDF(String nomfichier) {
        File file = new File(outPath + "/" + nomfichier + ".pdf");
        file.delete();
        createPDF(nomfichier, 2);
        afficher(file);


    }*/


    private void addEmptyLine(Document doc, Paragraph paragraph, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            doc.add(new Paragraph(" "));
        }
    }

    public void afficher(File file) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri pdf = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                intent.setDataAndType(pdf, "application/pdf");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");//com.example.sadjang.mobilpay
            }





  /*  Intent target=new Intent(Intent.ACTION_VIEW);
    target.setDataAndType(Uri.fromFile(file),"application/pdf");
    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    Intent intent=Intent.createChooser(target,"Open File");*/
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ///
            Toast.makeText(this, "getString(R.string.actinofoun)", Toast.LENGTH_LONG).show();

        }

    }


    public void generateCodeBar() {
        //Get input value for edit text
        //String sText = etInput.getText().toString().trim();
        String sText = code;
        bt_textx.setText(operateur + " : " + numero + " : " + name);
        // initialize multi format writer

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            // initialize bit matrix
            BitMatrix matrix = writer.encode(sText, BarcodeFormat.QR_CODE,
                    350, 350);
            //INITIALIZE BARCODE ENCODE
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(matrix);
            ivOutput.setImageBitmap(bitmap);
            InputMethodManager manager = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE
            );
            // HIDE SOFT KEYBOARD
            // manager.hideSoftInputFromWindow(etInput.getApplicationWindowToken(), 0);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
/*
    public void creatVoid() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            PdfDocument myPdfdocument = new PdfDocument();
            Paint myPaint = new Paint();
            Paint titlePaint = new Paint();
            PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200, 2012, 1).create();
            PdfDocument.Page myPage1 = myPdfdocument.startPage(myPageInfo1);
            Canvas canvas = myPage1.getCanvas();
            titlePaint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            titlePaint.setTextSize(70);

            canvas.drawText("OS( Operating System )",pageWidth/2,270,titlePaint);

            myPaint.setColor(Color.rgb(0, 113, 188));
            myPaint.setTextSize(300);
            canvas.drawLine(10, 10, 10, 10, myPaint);
            canvas.drawBitmap(bitmap, 100, 100, myPaint);


            myPdfdocument.finishPage(myPage1);
            // File file = new File(Environment.getExternalStorageDirectory(), "/Hello.pdf");
            File file=new File(getExternalFilesDir(racine).getAbsolutePath()+"/"+"Backup/llo.pdf");
            try {
                Log.e("path", file.getPath());
                myPdfdocument.writeTo(new FileOutputStream(file));
                Log.e("path", file.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myPdfdocument.close();

        }


    }

*/


    int pageHeight = 1120;
    int pagewidth = 992;

    private void generatePDF(String nomfichier) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {


            // creating an object variable
            // for our PDF document.
            PdfDocument pdfDocument = new PdfDocument();

            // two variables for paint "paint" is used
            // for drawing shapes and we will use "title"
            // for adding text in our PDF file.
            Paint paint = new Paint();
            Paint title = new Paint();

            // we are adding page info to our PDF file
            // in which we will be passing our pageWidth,
            // pageHeight and number of pages and after that
            // we are calling it to create our PDF.
            PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

            // below line is used for setting
            // start page for our PDF file.
            PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

            // creating a variable for canvas
            // from our page of PDF.
            Canvas canvas = myPage.getCanvas();

            // below line is used to draw our image on our PDF file.
            // the first parameter of our drawbitmap method is
            // our bitmap
            // second parameter is position from left
            // third parameter is position from top and last
            // one is our variable for paint.
            canvas.drawBitmap(bitmap, 60, 100, paint);

            // below line is used for adding typeface for
            // our text which we will be adding in our PDF file.
            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

            // below line is used for setting text size
            // which we will be displaying in our PDF file.
            title.setTextSize(60);

            // below line is sued for setting color
            // of our text inside our PDF file.
            title.setColor(ContextCompat.getColor(this, R.color.prim));

            // below line is used to draw text in our PDF file.
            // the first parameter is our text, second parameter
            // is position from start, third parameter is position from top
            // and then we are passing our variable of paint which is title.
            canvas.drawText("speed", 150, 280, title);
            // canvas.drawText("Make live easy.", 100, 200, title);
            // similarly we are creating another text and in this
            // we are aligning this text to center of our PDF file.
            //  title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            //  title.setColor(ContextCompat.getColor(this, R.color.purple_200));
            //  title.setTextSize(15);

            // below line is used for setting
            // our text to center of PDF.
            //title.setTextAlign(Paint.Align.CENTER);
            //  canvas.drawText("This is sample document which we have created.", 100, 560, title);

            // after adding all attributes to our
            // PDF file we will be finishing our page.
            pdfDocument.finishPage(myPage);

            // below line is used to set the name of
            // our PDF file and its path.
            // File file = new File(Environment.getExternalStorageDirectory(), "GFG.pdf");
            File file = new File(getExternalFilesDir(racine).getAbsolutePath() + "/" + "Backup/" + nomfichier + ".pdf");

            try {

                // after creating a file name we will
                // write our PDF file to that location.
                pdfDocument.writeTo(new FileOutputStream(file));
                // Log.e("path", file.getPath());
                // below line is to print toast message
                // on completion of PDF generation.
                // Toast.makeText(GenCodeBar.this, "PDF file generated succesfully.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                // below line is used
                // to handle error
                e.printStackTrace();
            }
            // after storing our pdf to that
            // location we are closing our PDF file.
            pdfDocument.close();
        }
    }


    public static void creatFolder(Context context, String racine, String foldername) {
        File file = new File(context.getExternalFilesDir(racine).getAbsolutePath() + "/" + foldername);
        if (!file.exists()) {
            file.mkdir();
            //   Log.e("volid", "PATH " + file.getPath());
            //   Toast.makeText(context, " Succesful", Toast.LENGTH_SHORT).show();
        } else {
            //  Toast.makeText(context, " folder already exist", Toast.LENGTH_SHORT).show();
            //Log.e("volid", "PATH " + file.getPath());

        }
    }


    /*
    Service key VHZvKTp3DOVlc00O64847UBDvzdYpUUc

Service secret Aof94roM62g0sg82UmfDFbZXHGqnPC4Tm7iKntHefSEYM1hqOAHmJQNMEMskPlVr
     */

}