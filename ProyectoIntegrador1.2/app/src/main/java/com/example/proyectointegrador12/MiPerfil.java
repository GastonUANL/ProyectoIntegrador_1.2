package com.example.proyectointegrador12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MiPerfil extends AppCompatActivity {

    DatabaseReference DBRef;

    public static final String USUARIO = "com.example.proyectointegrador12.MiPerfil.USUARIO";
    String idUsr = "";
    ImageView backbtn, img;
    TextView usr, correo, activo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        //Login
        Intent i = getIntent();
        idUsr = i.getStringExtra(MenuDinamico.USUARIO);

        //Database
        DBRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(idUsr);

        //SetupUI
        usr = findViewById(R.id.txt_NombreUsr);
        correo = findViewById(R.id.txt_Correo);
        activo = findViewById(R.id.txt_Activo);
        img = findViewById(R.id.img_Usr_MiPerfil);

        DBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usr.setText(dataSnapshot.child("nombreUsuario").getValue().toString());
                correo.setText(dataSnapshot.child("correo").getValue().toString());
                activo.setText(dataSnapshot.child("activo").getValue().toString());
                if(!dataSnapshot.child("imagen").getValue().toString().equals("N/A")){
                    Picasso.get().load(dataSnapshot.child("imagen").getValue().toString()).into(img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void editarPerfil(View view) {
        Intent i = new Intent(getApplicationContext(), EditPerfil.class);
        i.putExtra(USUARIO, idUsr);
        startActivity(i);
    }

    public void back() {
        this.finish();
    }
}