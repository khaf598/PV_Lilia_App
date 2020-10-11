package com.example.pv_lilia_app.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pv_lilia_app.ClsAdaptador;
import com.example.pv_lilia_app.R;
import com.example.pv_lilia_app.clsProducto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ListView listaF;
    int Id_Editar=0;
    boolean Editar=false;
    clsProducto ObjProd;
    private ClsAdaptador Adaptador;
    TextView txtNoVta;
    ImageView imagen;
    ArrayList<clsProducto> LstProd = new ArrayList<clsProducto>();
    int a;
    View v;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        v = inflater.inflate(R.layout.dialogo2, null);
        try {
            Statement stn = conexionBD().createStatement();
            ResultSet rs = stn.executeQuery("Select * From Tbl_Productos");
            while (rs.next()) {
                ObjProd = new clsProducto();
                ObjProd.Prod_Codigo=rs.getString(2);
                ObjProd.Prod_Nombre=rs.getString(3);
                ObjProd.Prod_Piezas=rs.getInt(4);
                ObjProd.Prod_Precio_Ent=rs.getDouble(5);
                ObjProd.Prod_Precio_Sal=rs.getDouble(6);
                ObjProd.Id_Prod=rs.getInt(1);

                LstProd.add(ObjProd);//esta es la chida el for no
            }
        } catch (Exception e) {
            Toast.makeText(getView().getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        //txtNoVta = (TextView) getActivity().findViewById(R.id.txtcount);
        //txtNoVta.setText(""+LstVenta.size());
        listaF =(ListView) root.findViewById(R.id.LstProductos);

        Adaptador= new ClsAdaptador(getContext(),LstProd);//ADAPTAR PRODUCTOS
        listaF.setAdapter(Adaptador);                     //MOSTRAR A LISTVIEW
        listaF.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Escojer(LstProd.get(pos).Id_Prod);
            }
        });
        return root;
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
    private void Escojer(final int pos)
    {

        //-------------------------------------------------------

        LayoutInflater inflater = getLayoutInflater();
        View dial = inflater.inflate( R.layout.dialogo2, null);
        imagen = (ImageView) dial.findViewById(R.id.imgvi);

        try {
            Statement stn = conexionBD().createStatement();
            ResultSet rs = stn.executeQuery("Select Pr_Image From Tbl_Productos Where Pr_Id = "+pos);
            if (rs.next()) {
                byte[] photo = rs.getBytes(1);
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

                Toast.makeText(getActivity(), String.valueOf(photo), Toast.LENGTH_SHORT).show();

                imagen.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        AlertDialog.Builder build= new AlertDialog.Builder(getContext()).setView(dial);
        TextView title = new TextView(getContext());

        title.setBackgroundColor(Color.GRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(30);
        build.setCustomTitle(title);
        build.show();
    }
}