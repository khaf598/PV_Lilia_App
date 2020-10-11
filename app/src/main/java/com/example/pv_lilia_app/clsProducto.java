package com.example.pv_lilia_app;
public class clsProducto {

    public int Id_Prod;
    public String Prod_Codigo;
    public String Prod_Nombre;
    public int Prod_Piezas;
    public double Prod_Precio_Ent;
    public double Prod_Precio_Sal;


    public int get_IdProd(){
        return Id_Prod;
    }
    public void set_IdProd(int IdProd){
        Id_Prod=IdProd;
    }
    public String getProdCod(){
        return Prod_Codigo;
    }
    public String getProdNom(){
        return Prod_Nombre;
    }
    public int getProdPie(){ return Prod_Piezas; }
    public void set_ProdNom(String ProdNom){
        Prod_Nombre=ProdNom;
    }
    public double get_ProdPrecio_Ent() {return Prod_Precio_Ent; }
    public void set_ProdPrecio_Ent(float ProdPrecio_Ent){Prod_Precio_Ent = ProdPrecio_Ent;}
    public double get_ProdPrecio_Sal() {return Prod_Precio_Sal; }
    public void set_ProdPrecio_Sal(float ProdPrecio_Ent){Prod_Precio_Sal = ProdPrecio_Ent;}
    public clsProducto(){}
    public clsProducto(int IdProd, String Prod_Cod, String ProdNom, float
            ProdPrecio_Ent,float ProdPrecio_Sal, int Prod_Piez){
        Id_Prod=IdProd;
        Prod_Nombre=ProdNom;
        Prod_Codigo=Prod_Cod;
        Prod_Precio_Ent=ProdPrecio_Ent;
        Prod_Precio_Sal=ProdPrecio_Sal;
        Prod_Piezas = Prod_Piez;
    }
}