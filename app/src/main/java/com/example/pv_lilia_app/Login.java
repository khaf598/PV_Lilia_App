package com.example.pv_lilia_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {
    EditText Usuario;
    EditText Contrasena;
    Button Consultar;
    public String cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Usuario = (EditText) findViewById(R.id.txtUsuario);
        Contrasena = (EditText) findViewById(R.id.txtContrasena);
        Consultar = (Button) findViewById(R.id.btnEntrar);

        Consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarusuario();
            }
        });
    }

    public Connection conexionBD() {
        Connection cnn = null;
        try {
            StrictMode.ThreadPolicy politice = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politice);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://SQL5080.site4now.net;databaseName=DB_A55496_Lilia;user=DB_A55496_Lilia_admin;password=Resident123;");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        return cnn;
    }

    public void consultarusuario() {
        try {
            Statement stn = conexionBD().createStatement();
            ResultSet rs = stn.executeQuery("Select * From Tbl_Usuarios Where Us_Usuario = '" + Usuario.getText().toString() + "' And Us_Contrasena = '" + Contrasena.getText().toString() + "'");
            if (rs.next()) {
                cliente = (rs.getString(1));
                Intent i = new Intent(Login.this, MainActivity.class);
                i.putExtra("Id", cliente.toString());
                Usuario.setText("");
                Contrasena.setText("");
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "El usuario no existe o la contrasena no es correcta", Toast.LENGTH_SHORT).show();
                Usuario.setText("");
                Contrasena.setText("");
                Usuario.requestFocus();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
