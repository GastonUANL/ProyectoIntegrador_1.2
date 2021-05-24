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

import com.example.proyectointegrador12.Administradores;
import com.example.proyectointegrador12.EditPerfil;
import com.example.proyectointegrador12.R;
import com.example.proyectointegrador12.db.DB_Admins;
import com.example.proyectointegrador12.db.DB_Noticias;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Admins extends RecyclerView.Adapter<Adapter_Admins.Admins_ViewHolder> {

    public static final String USUARIO = "com.example.proyectointegrador12.Administradores.USUARIO";

    List<DB_Admins> Admins, AdminsFull;

    public Adapter_Admins(List<DB_Admins> admins) {
        this.Admins = admins;
        AdminsFull = new ArrayList<>(admins);
    }

    @NonNull
    @Override
    public Admins_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowfiller_admins, parent, false);
        Admins_ViewHolder VH = new Admins_ViewHolder(v);
        return VH;
    }

    @Override
    public void onBindViewHolder(@NonNull Admins_ViewHolder holder, int position) {

        DB_Admins db_admins = Admins.get(position);

        holder.nombre.setText(db_admins.getNombreUsuario());
        if( db_admins.getActivo().equals("1")){
            holder.estatus.setText("Estatus: Activo");
        } else {
            holder.estatus.setText("Estatus: Inactivo");
        }
        holder.tipousr.setText("Tipo de Usuario: Administrador");
        if(!db_admins.getImagen().equals("N/A")){
            Picasso.get().load(db_admins.getImagen()).into(holder.img);
        }
        //Buttons
        if(db_admins.getActivo().equals("0")){
            holder.eliminar.setText("Activar");
        } else {
            holder.eliminar.setText("Desactivar");
        }
        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditPerfil.class);
                i.putExtra(USUARIO, db_admins.getId_Usr());
                v.getContext().startActivity(i);
            }
        });
        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(db_admins.getId_Usr());
                AlertDialog.Builder bld = new AlertDialog.Builder(v.getContext());
                bld.setCancelable(true);
                if(db_admins.getActivo().equals("1")) {
                    bld.setTitle("Desactivar Administrador");
                    bld.setMessage("¿Estas seguro de que deseas desactivar el Administrador: " + db_admins.getNombreUsuario() +"?");
                    bld.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Database
                            DBRef.child("activo").setValue("0");
                        }
                    });
                    bld.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    bld.setTitle("Activar Administrador");
                    bld.setMessage("¿Estas seguro de que deseas Activar el Administrador: " + db_admins.getNombreUsuario() +"?");
                    bld.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Database
                            DBRef.child("activo").setValue("1");
                        }
                    });
                }
                AlertDialog dialog = bld.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Admins.size();
    }

    public class Admins_ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView nombre;
        TextView estatus;
        TextView tipousr;
        Button editar;
        Button eliminar;


        public Admins_ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.rf_img_Adm);
            nombre = (TextView) itemView.findViewById(R.id.rf_txt_Nombre_Adm);
            estatus = (TextView) itemView.findViewById(R.id.rf_txt_Estatus_Adm);
            tipousr = (TextView) itemView.findViewById(R.id.rf_txt_TipoUsr_Adm);
            editar = (Button) itemView.findViewById(R.id.rf_btn_EditarAdm);
            eliminar = (Button) itemView.findViewById(R.id.rf_btn_EliminarAdm);
        }
    }
}
