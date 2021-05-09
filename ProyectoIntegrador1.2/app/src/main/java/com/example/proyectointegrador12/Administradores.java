package com.example.proyectointegrador12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.proyectointegrador12.adapters.Adapter_Admins;
import com.example.proyectointegrador12.adapters.Adapter_Principal;
import com.example.proyectointegrador12.db.DB_Admins;
import com.example.proyectointegrador12.db.DB_Noticias;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Administradores extends AppCompatActivity {

    public static final String TIPOP = "com.example.proyectointegrador12.Administradores.TIPOP";

    FirebaseDatabase DBRef;
    RecyclerView rv; //rv
    Adapter_Admins adapter;
    ImageView back;
    List<DB_Admins> admins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administradores);

        //DB
        DBRef = FirebaseDatabase.getInstance();

        //UI
        admins = new ArrayList<>();
        rv = findViewById(R.id.rv_Admins);
        adapter = new Adapter_Admins(admins);
        back = findViewById(R.id.btn_back_Admins);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { back(); }
        });


        //Rexicler View
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        //Content
        DBRef.getReference().getRoot().child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                admins.removeAll(admins);
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.child("tipo_Usr").getValue().toString().equals("2")){
                        DB_Admins db_admins = dataSnapshot.child(ds.getKey()).getValue(DB_Admins.class);
                        admins.add(db_admins);
                    }
                }
                adapter.notifyDataSetChanged();
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

    public void back() {
        this.finish();
    }

    public void agregar(View view){
        Intent i = new Intent(getApplicationContext(), EditPerfil.class);
        i.putExtra(TIPOP, "2");
        startActivity(i);
    }
}