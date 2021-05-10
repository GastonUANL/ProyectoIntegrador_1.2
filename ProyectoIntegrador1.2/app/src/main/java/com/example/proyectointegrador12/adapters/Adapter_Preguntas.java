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

import com.example.proyectointegrador12.EditPerfil;
import com.example.proyectointegrador12.EditPregunta;
import com.example.proyectointegrador12.R;
import com.example.proyectointegrador12.db.DB_Admins;
import com.example.proyectointegrador12.db.DB_Preguntas;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Preguntas extends RecyclerView.Adapter<Adapter_Preguntas.Preguntas_ViewHolder> {

    public static final String PREGUNTA = "com.example.proyectointegrador12.Adapter_Preguntas.PREGUNTA";
    public static final String TIPO = "com.example.proyectointegrador12.Adapter_Preguntas.PREGUNTA";

    List<DB_Preguntas> preguntas;
    String TipoUsr = "";

    public Adapter_Preguntas(List<DB_Preguntas> pregs, String tipoUsr) {
        this.preguntas = pregs;
        TipoUsr = tipoUsr;
    }

    @NonNull
    @Override
    public Adapter_Preguntas.Preguntas_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowfiller_preguntas, parent, false);
        Adapter_Preguntas.Preguntas_ViewHolder VH = new Adapter_Preguntas.Preguntas_ViewHolder(v);
        return VH;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Preguntas.Preguntas_ViewHolder holder, int position) {

        DB_Preguntas db_preguntas = preguntas.get(position);

        holder.pregunta.setText(db_preguntas.getPregunta());
        holder.respuesta.setText(db_preguntas.getRespuesta());

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditPregunta.class);
                i.putExtra(PREGUNTA, db_preguntas.getId_Pregunta());
                i.putExtra(TIPO, "1");
                v.getContext().startActivity(i);
            }
        });

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Preguntas");
                AlertDialog.Builder bld = new AlertDialog.Builder(v.getContext());
                bld.setCancelable(true);
                bld.setTitle("Eliminar Pregunta");
                bld.setMessage("¿Estas seguro de que deseas Eliminar esta pregunta?");
                bld.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Database
                        DBRef.child(db_preguntas.getId_Pregunta()).setValue(null);
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
        });

        if(TipoUsr.equals("1")){
            holder.ll_btns.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    public class Preguntas_ViewHolder extends RecyclerView.ViewHolder {

        TextView pregunta;
        TextView respuesta;
        Button editar;
        Button eliminar;
        LinearLayout ll_btns;


        public Preguntas_ViewHolder(@NonNull View itemView) {
            super(itemView);

            pregunta = (TextView) itemView.findViewById(R.id.rf_txt_Pregunta);
            respuesta = (TextView) itemView.findViewById(R.id.rf_txt_Respuesta);
            editar = (Button) itemView.findViewById(R.id.rf_btn_EditarPreg);
            eliminar = (Button) itemView.findViewById(R.id.rf_btn_EliminarPreg);
            ll_btns = (LinearLayout) itemView.findViewById(R.id.rf_ll_BtnsAdminP);
        }
    }
}
