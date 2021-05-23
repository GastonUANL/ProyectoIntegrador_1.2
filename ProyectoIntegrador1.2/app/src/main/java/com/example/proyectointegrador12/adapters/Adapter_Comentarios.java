package com.example.proyectointegrador12.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectointegrador12.EditNoticia;
import com.example.proyectointegrador12.R;
import com.example.proyectointegrador12.VistaNoticia;
import com.example.proyectointegrador12.db.DB_Comentarios;
import com.example.proyectointegrador12.db.DB_Noticias;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Comentarios extends RecyclerView.Adapter<Adapter_Comentarios.Comentarios_ViewHolder>  {

    List<DB_Comentarios> comentarios;
    DatabaseReference DBRef;
    String usr = "";

    public Adapter_Comentarios(List<DB_Comentarios> comentarios) {
        this.comentarios = comentarios;
    }

    @NonNull
    @Override
    public Adapter_Comentarios.Comentarios_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowfiller_comentarios, parent, false);
        Adapter_Comentarios.Comentarios_ViewHolder VH = new Adapter_Comentarios.Comentarios_ViewHolder(v);
        return VH;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Comentarios.Comentarios_ViewHolder holder, int position) {

        DB_Comentarios db_comentarios = comentarios.get(position);
        DBRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        DBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usr = dataSnapshot.child(db_comentarios.getId_Usr()).child("nombreUsuario").getValue().toString();
                holder.usuario.setText(usr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.contenido.setText(db_comentarios.getContenido());
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public class Comentarios_ViewHolder extends RecyclerView.ViewHolder {

        TextView usuario, contenido;

        public Comentarios_ViewHolder(@NonNull View itemView) {
            super(itemView);

            usuario = itemView.findViewById(R.id.rf_txt_Usr_Comment);
            contenido = itemView.findViewById(R.id.rf_txt_Contenido_Comment);
        }
    }
}
