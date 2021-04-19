package com.example.proyectointegrador12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static final String USUARIO = "com.example.proyectointegrador12.MainActivity.USUARIO";

    DatabaseReference DBReg;
    EditText usr, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Database
        DBReg = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        //Objetos
        usr = (EditText) findViewById(R.id.txtbox_Usuario);
        pass = (EditText) findViewById(R.id.txtbox_Password);
    }

    public void logIn(View v){
        try {
            //validacion de usuario
            String Usuario = usr.getText().toString();
            if (Usuario.equals("")) {
                usr.setError("El Nombre de Usuario no puede estar vacio.");
                return;
            }
            //validacion de contraseña
            String Contraseña = pass.getText().toString();
            if (Contraseña.equals("")) {
                pass.setError("La contraseña no puede estar vacio.");
                return;
            }
            else if(Contraseña.length() <5)
            {
                pass.setError("La contraseña debe contener al menos 5 caracteres.");
                return;
            }
            //validación DB
            DBReg.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String usrA, passA;
                    int ErrCode = 0;
                    String idUsr = "";
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        usrA = ds.child("nombreUsuario").getValue().toString();
                        if(usrA.equals(Usuario))
                        {
                            passA = ds.child("contrasena").getValue().toString();
                             if(passA.equals(Contraseña)){
                                 idUsr = ds.child("id_Usr").getValue().toString();
                                 ErrCode = 1;
                             }
                             else { ErrCode = 2; }
                        }
                        else { ErrCode = 2; }
                    }
                    switch(ErrCode){
                        case 1:{
                            Toast.makeText(MainActivity.this, "Exito " + idUsr, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), MenuDinamico.class);
                            i.putExtra(USUARIO, idUsr);
                            startActivity(i);
                            MainActivity.this.finish();
                            break;
                        }
                        case 2:{
                            Toast.makeText(MainActivity.this, "El Nomrbe de Usuario y la Contraseña no coinciden con los de ningun usuario registrado.", Toast.LENGTH_LONG).show();
                            break;
                        }
                        default:{
                            Toast.makeText(MainActivity.this, "Error no Registrado", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Ocurrio un Error con Firebase.", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e){
            Log.e("Error", "Error--" + e.getMessage().toString());
            Toast.makeText(MainActivity.this, "Ocurrio un Error --->  " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}