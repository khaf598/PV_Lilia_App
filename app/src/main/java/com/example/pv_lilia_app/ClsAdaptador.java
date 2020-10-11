package com.example.pv_lilia_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ClsAdaptador extends BaseAdapter {
    private Context context;
    private ArrayList<clsProducto> LstProd;

    public ClsAdaptador(Context context, ArrayList<clsProducto> lstProd) {
        this.context = context;
        this.LstProd = lstProd;
    }

    @Override
    public int getCount() {
        return LstProd.size();
    }

    @Override
    public Object getItem(int position) {
        return LstProd.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        clsProducto ObjNprod = (clsProducto)getItem(position);

        convertView= LayoutInflater.from(context).inflate(R.layout.item2, null);
        TextView txtTitulo= (TextView)convertView.findViewById(R.id.tvTitulo);
        TextView txtContenido= (TextView)convertView.findViewById(R.id.tvContenido);
        TextView txtPiez= (TextView)convertView.findViewById(R.id.tvPiezas);
        TextView txtPrecio_1= (TextView)convertView.findViewById(R.id.tvPrecios);
        TextView txtPrecio_2= (TextView)convertView.findViewById(R.id.tvPrecio_2);

        txtTitulo.setText(ObjNprod.getProdNom());
        txtContenido.setText(ObjNprod.getProdCod());
        txtPiez.setText(String.valueOf(ObjNprod.getProdPie()));
        txtPrecio_1.setText("Entrada: $"+String.valueOf(ObjNprod.get_ProdPrecio_Ent()));
        txtPrecio_2.setText("Salida: $"+String.valueOf(ObjNprod.get_ProdPrecio_Sal()));

        return convertView;
    }
}
