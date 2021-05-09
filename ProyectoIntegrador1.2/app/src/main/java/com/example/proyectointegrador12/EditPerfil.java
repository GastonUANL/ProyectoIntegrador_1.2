package com.example.proyectointegrador12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.proyectointegrador12.adapters.Adapter_Admins;
import com.example.proyectointegrador12.cultura.Cultura;
import com.example.proyectointegrador12.deportes.Deportes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPerfil extends AppCompatActivity {

    public static final String USUARIO = "com.example.proyectointegrador12.EditPerfil.USUARIO";

    String idUsr = "";
    int activityOr = 0;
    EditText usr, correo, contra, verifcontra;
    ImageView backbtn;
    DatabaseReference DBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        //Login
        activityOr = getActivity();
        idUsr = getUsr(activityOr);

        //Database
        DBRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(idUsr);

        //UI
        usr = findViewById(R.id.nombreinput);
        correo = findViewById(R.id.emailinput);
        contra = findViewById(R.id.contrasenainput);
        verifcontra = findViewById(R.id.contrasena2input);
        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        //Hints
        DBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usr.setHint(dataSnapshot.child("nombreUsuario").getValue().toString());
                correo.setHint(dataSnapshot.child("correo").getValue().toString());
                contra.setHint("*****");
                verifcontra.setHint("*****");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public int getActivity() {
        int activity = 0;
        /*  0: MiPerfil
            1: Admins */
        Intent i = getIntent();
        if(i.hasExtra(MiPerfil.USUARIO)){
            activity = 0;
        } else if(i.hasExtra(Adapter_Admins.USUARIO)) {
            activity = 1;
        }
        return activity;
    }

    public String getUsr(int actOr){
        String usr = "";
        Intent i = getIntent();
        if(actOr == 0){
            usr = i.getStringExtra(MiPerfil.USUARIO);
        } else if(actOr == 1){
            usr = i.getStringExtra(Adapter_Admins.USUARIO);
        }
        return usr;
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MiPerfil.class);
        i.putExtra(USUARIO, idUsr);
        startActivity(i);
        this.finish();
    }

    public void back() {
        this.finish();
    }

    public void save(View view){

    }
}