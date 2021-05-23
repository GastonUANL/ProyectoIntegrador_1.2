package com.example.proyectointegrador12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectointegrador12.adapters.Adapter_Comentarios;
import com.example.proyectointegrador12.adapters.Adapter_Principal;
import com.example.proyectointegrador12.db.DB_Admins;
import com.example.proyectointegrador12.db.DB_Comentarios;
import com.example.proyectointegrador12.db.DB_Noticias;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VistaNoticia extends AppCompatActivity {

    public static final String USUARIO = "com.example.proyectointegrador12.VistaNoticia.USUARIO";
    public static final String NTC = "com.example.proyectointegrador12.VistaNoticia.USUARIO";
    public static final String TIPO = "com.example.proyectointegrador12.VistaNoticia.TIPO";

    String id_Ntc, id_Tp, id_UsrTp, id_Usr;
    DatabaseReference DBRef;
    ImageView backbtn;

    TextView titulo, contenido;
    RecyclerView rv;
    Adapter_Comentarios adapter;
    List<DB_Comentarios> comentarios;
    Button eliminar, editar;
    ImageView imgn;

    boolean coment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_noticia);

        //DB
        DBRef = FirebaseDatabase.getInstance().getReference();

        //GetData
        Intent i = getIntent();
        id_UsrTp = i.getStringExtra(Adapter_Principal.TPUSR);
        id_Ntc = i.getStringExtra(Adapter_Principal.NOTICIA);
        id_Tp = i.getStringExtra(Adapter_Principal.TIPO);
        id_Usr = i.getStringExtra(Adapter_Principal.USR);

        //UI
        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        titulo = findViewById(R.id.txt_Titulo_VistaNt);
        contenido = findViewById(R.id.txt_Contenido_VistaNt);
        rv = findViewById(R.id.rv_Comentarios);
        eliminar = findViewById(R.id.btn_EliminarVistaNt);
        editar = findViewById(R.id.btn_EditarVistaNt);
        imgn = findViewById(R.id.img_VistaNt);
        comentarios = new ArrayList<>();
        adapter = new Adapter_Comentarios(comentarios);

        //Rexicler View
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        //Hide
        if(id_UsrTp.equals("1")){
            eliminar.setVisibility(View.GONE);
            editar.setVisibility(View.GONE);
        }

        //SetData
        DBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                titulo.setText(dataSnapshot.child("Noticias").child(id_Ntc).child("titulo").getValue().toString());
                contenido.setText(dataSnapshot.child("Noticias").child(id_Ntc).child("contenido").getValue().toString());
                String img = dataSnapshot.child("Noticias").child(id_Ntc).child("imagen").getValue().toString();
                if(!img.equals("N/A")){
                    Picasso.get().load(img).into(imgn);
                }

                //Recicler View
                comentarios.removeAll(comentarios);
                for(DataSnapshot ds : dataSnapshot.child("Comentarios").getChildren()){
                    if(ds.child("id_Noticia").exists() && ds.child("id_Comentario").exists() && ds.child("contenido").exists() && ds.child("id_Usr").exists() && ds.child("fecha").exists()) {
                        if (ds.child("id_Noticia").getValue().toString().equals(id_Ntc)) {
                            DB_Comentarios db_comentarios = new DB_Comentarios();
                            db_comentarios.setContenido(ds.child("contenido").getValue().toString());
                            db_comentarios.setFecha(ds.child("fecha").getValue().toString());
                            db_comentarios.setId_Comentario(ds.child("id_Comentario").getValue().toString());
                            db_comentarios.setId_Noticia(ds.child("id_Noticia").getValue().toString());
                            db_comentarios.setId_Usr(ds.child("id_Usr").getValue().toString());
                            comentarios.add(db_comentarios);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void editar(View view) {
        Intent i = new Intent(getApplicationContext(), EditNoticia.class);
        i.putExtra(USUARIO, id_Usr);
        i.putExtra(NTC, id_Ntc);
        i.putExtra(TIPO, id_Tp);
        startActivity(i);
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void back() {
        this.finish();
    }

    public void delete(View view){
        AlertDialog.Builder bld = new AlertDialog.Builder(view.getContext());
        bld.setCancelable(true);
        bld.setTitle("Eliminar Noticia");
        bld.setMessage("¿Seguro que quieres eliminar la noticia: " + titulo.getText().toString() + "?");
        bld.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBRef.child("Noticias").child(id_Ntc).setValue(null);
                dialog.dismiss();
            }
        });
        bld.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = bld.create();
        dialog.show();
    }

    public void comment(View view){
        AlertDialog.Builder bld = new AlertDialog.Builder(view.getContext());
        bld.setCancelable(true);
        bld.setTitle("Comentar Noticia");
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Comentario");
        bld.setView(input);
        bld.setMessage("---");
        bld.setPositiveButton("Comentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                coment = true;
                DBRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(coment) {
                            int cont = 1;
                            for (DataSnapshot ds : dataSnapshot.child("Comentarios").getChildren()) {
                                cont = Integer.parseInt(ds.child("id_Comentario").getValue().toString());
                            }
                            cont++;
                            DBRef.child("Comentarios").child("" + cont).child("contenido").setValue(input.getText().toString());
                            Calendar rightNow = Calendar.getInstance();
                            int dia, mes, ano;
                            dia = rightNow.get(Calendar.DAY_OF_MONTH);
                            mes = rightNow.get(Calendar.MONTH);
                            ano = rightNow.get(Calendar.YEAR);
                            String fecha = dia + "," + mes + "," + ano;
                            DBRef.child("Comentarios").child("" + cont).child("fecha").setValue(fecha);
                            DBRef.child("Comentarios").child("" + cont).child("id_Comentario").setValue("" + cont);
                            DBRef.child("Comentarios").child("" + cont).child("id_Noticia").setValue(id_Ntc);
                            DBRef.child("Comentarios").child("" + cont).child("id_Usr").setValue(id_Usr);
                            coment = false;
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        bld.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = bld.create();
        dialog.show();
    }
}