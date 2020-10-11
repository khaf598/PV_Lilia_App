package com.example.pv_lilia_app.ui.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pv_lilia_app.Escanear;
import com.example.pv_lilia_app.R;
import com.google.zxing.Result;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import android.util.Base64.*;


import static android.app.Activity.RESULT_OK;


public class GalleryFragment extends Fragment{


    private GalleryViewModel galleryViewModel;
    EditText cod,nom,piez,pre_e,pre_s;
    Activity activity;
    ImageView mPhotoImageView;
    public static final int REQUEST_CODE_TAKE_PHOTO = 0 /*1*/;
    private String mCurrentPhotoPath;
    private Uri photoURI;
    ImageView imageView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        activity = this.getActivity();
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        final Button but = root.findViewById(R.id.btnGuardar);
        final Button scan = root.findViewById(R.id.btnEscan);

        cod = (EditText)  root.findViewById(R.id.txtCodigo);
        nom = (EditText)  root.findViewById(R.id.txtNombre);
        piez = (EditText)  root.findViewById(R.id.txtPiez);
        pre_e = (EditText)  root.findViewById(R.id.txtPre_Ent);
        pre_s = (EditText)  root.findViewById(R.id.txtPre_Sal);
        imageView = (ImageView) root.findViewById(R.id.imvFoto);


        // Views
        mPhotoImageView = (ImageView) root.findViewById(R.id.imvFoto);
        // Listeners
        mPhotoImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (v == mPhotoImageView) {

                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    225);
                        }
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.CAMERA)) {

                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    226);
                        }
                    } else {
                        dispatchTakePictureIntent();
                    }
                }
            }
        });
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                but.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        if(cod.getText().toString()!=""&&nom.getText().toString()!=""&&piez.getText().toString()!=""&&
                                pre_e.getText().toString()!=""&&pre_s.getText().toString()!="") {
                            try {

                                Ins_Producto(cod.getText().toString(), nom.getText().toString(), Integer.parseInt(piez.getText().toString()),
                                        Double.parseDouble(pre_e.getText().toString()), Double.parseDouble(pre_s.getText().toString()));

                                Bitmap bm=((BitmapDrawable)imageView.getDrawable()).getBitmap();

                                guardarImagen(1,bm);
                                cod.setText("");
                                nom.setText("");
                                piez.setText("");
                                pre_e.setText("");
                                pre_s.setText("");
                                cod.requestFocus();
                            }catch (Exception ex){
                                Toast.makeText(getActivity(),ex.getLocalizedMessage(),Toast.LENGTH_SHORT).show();}
                        }else{
                            Toast.makeText(getActivity(),"Ingresar todos los datos",Toast.LENGTH_SHORT).show();}
                    }
                });
            }
        });
        return root;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                photoURI = getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                //Uri photoURI = FileProvider.getUriForFile(AddActivity.this, "com.example.android.fileprovider", photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + "Hola" + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoURI);
                mPhotoImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public Connection conexionBD(){
        Connection cnn= null;
        try
        {
            StrictMode.ThreadPolicy politice = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politice);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://SQL5080.site4now.net;databaseName=DB_A55496_Lilia;user=DB_A55496_Lilia_admin;password=Resident123;");
        }catch (Exception e){
            Toast.makeText(getContext().getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
        return cnn;
    }

    public void Ins_Producto(String codi,String nomb,Integer piezas,Double Pre_ent, Double Pre_Sal) {
        try {
            Statement stn = conexionBD().createStatement();
            stn.executeQuery("Insert into Tbl_Productos Values('"+codi+"','"+nomb+"',"+piezas+","+Pre_ent+","+Pre_Sal+")");

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Producto registrado", Toast.LENGTH_SHORT).show();
        }
    }
    public void Ins_ImgProducto(int Pr_Id,byte[] imagen) {
        try {
            Statement stn = conexionBD().createStatement();
            stn.executeQuery("Insert into Tbl_Imagen Values("+Pr_Id+","+imagen+")");

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Imagen registrado", Toast.LENGTH_SHORT).show();
        }
    }
    public void guardarImagen(int id, Bitmap bitmap) throws IOException {
        URL url = new URL("http://sree.cc/wp-content/uploads/schogini_team.png");
        URLConnection ucon = url.openConnection();
        InputStream is = ucon.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is,128);
        ByteArrayBuffer barb= new ByteArrayBuffer(128);
        //stream.toByteArray();
        // aqui tenemos el byte[] con el imagen comprimido, ahora lo guardemos en SQLite
        try {
            Statement stn = conexionBD().createStatement();
            stn.executeQuery("Insert into Tbl_Imagen Values("+id+","+byteArray+")");

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}