package com.example.proyectointegrador12;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyectointegrador12.adapters.Adapter_Preguntas;
import com.example.proyectointegrador12.db.DB_Preguntas;
import com.example.proyectointegrador12.principal.Registro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditPregunta extends AppCompatActivity {

    EditText pregunta, respuesta;
    int tipo = 0;
    String id_Preg = "";
    DatabaseReference DBRef;
    boolean save = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pregunta);

        //Database
        DBRef = FirebaseDatabase.getInstance().getReference().child("Preguntas");

        //UI
        pregunta = findViewById(R.id.txt_Pregunta_EditP);
        respuesta = findViewById(R.id.txt_Respuesta_EditP);

        Intent i = getIntent();
        if(i.hasExtra(Adapter_Preguntas.TIPO)){
            tipo = 1;
            id_Preg = i.getStringExtra(Adapter_Preguntas.PREGUNTA);
        }

        if(tipo == 1){
            DBRef.child(id_Preg).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pregunta.setHint(dataSnapshot.child("pregunta").getValue().toString());
                    respuesta.setHint(dataSnapshot.child("respuesta").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void guardarP(View view){
        boolean preg = false;
        boolean resp = false;
        if(tipo == 1){
            if(pregunta.getText().length() > 0){
                if(pregunta.getText().length() < 5) {
                    pregunta.setError("La pregunta debe contener al menos 5 caracteres, o deje el campo en blanco para mantener la información actual.");
                } else {
                    //DBRef.child(id_Preg).child("pregunta").setValue(pregunta.getText());
                    preg = true;
                }
            }
            if(respuesta.getText().length() > 0){
                if(respuesta.getText().length() < 2) {
                    respuesta.setError("La respuesta debe contener al menos 2 caracteres, o deje el campo en blanco para mantener la información actual.");
                } else {
                    //DBRef.child(id_Preg).child("respuesta").setValue(respuesta.getText());
                    resp = true;
                }
            }
            if(preg && resp){
                DBRef.child(id_Preg).child("pregunta").setValue(pregunta.getText().toString());
                DBRef.child(id_Preg).child("id_Pregunta").setValue(id_Preg);
                DBRef.child(id_Preg).child("respuesta").setValue(respuesta.getText().toString());
                Toast.makeText(this, "Se guardaron los cambios.", Toast.LENGTH_SHORT).show();
                this.finish();
            } else {
                Toast.makeText(this, "Favor de Ingresar los datos correctamente o dejar los campos vacios para mantener la informacion.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(pregunta.getText().length() < 5) {
                pregunta.setError("La pregunta debe contener al menos 5 caracteres, o deje el campo en blanco para mantener la información actual.");
            } else {
                preg = true;
            }
            if(respuesta.getText().length() < 2) {
                respuesta.setError("La respuesta debe contener al menos 2 caracteres, o deje el campo en blanco para mantener la información actual.");
            } else {
                resp = true;
            }
            if(preg && resp){
                save = true;
                DBRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(save){
                            int cont = 1;
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                if(ds.child("id_Pregunta").getValue().toString().equals(""+ cont)){
                                    cont++;
                                } else { break; }
                            }
                            DBRef.child("" + cont).child("pregunta").setValue(pregunta.getText().toString());
                            DBRef.child("" + cont).child("id_Pregunta").setValue("" + cont);
                            DBRef.child("" + cont).child("respuesta").setValue(respuesta.getText().toString());
                            Toast.makeText(EditPregunta.this, "Se guardaron los cambios.", Toast.LENGTH_SHORT).show();
                            save = false;
                            EditPregunta.this.finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }
    }

    public void back(View view){
        this.finish();
    }
}