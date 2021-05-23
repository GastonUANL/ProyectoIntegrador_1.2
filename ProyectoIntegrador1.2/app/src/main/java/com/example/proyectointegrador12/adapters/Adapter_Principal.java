package com.example.proyectointegrador12.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectointegrador12.EditNoticia;
import com.example.proyectointegrador12.MenuDinamico;
import com.example.proyectointegrador12.R;
import com.example.proyectointegrador12.VistaNoticia;
import com.example.proyectointegrador12.db.DB_Noticias;
import com.example.proyectointegrador12.principal.Principal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Principal extends RecyclerView.Adapter<Adapter_Principal.Noticias_ViewHolder> {

    public static final String NOTICIA = "com.example.proyectointegrador12.adapters.Adapter_Principal.NOTICIA";
    public static final String TIPO = "com.example.proyectointegrador12.adapters.Adapter_Principal.TIPO";
    public static final String TPUSR = "com.example.proyectointegrador12.adapters.Adapter_Principal.TPUSR";
    public static final String USR = "com.example.proyectointegrador12.adapters.Adapter_Principal.USR";

    List<DB_Noticias> noticiasFull, noticias;
    String TipoUsr = "", id_Usr = "";

    DatabaseReference DBRef;

    public Adapter_Principal(List<DB_Noticias> noticias, String tipoUsr, String usr) {
        this.noticias = noticias;
        noticiasFull = new ArrayList<>(noticias);
        TipoUsr = tipoUsr;
        id_Usr = usr;
    }

    @NonNull
    @Override
    public Noticias_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowfiller_noticias, parent, false);
        Noticias_ViewHolder VH = new Noticias_ViewHolder(v);
        return VH;
    }

    @Override
    public void onBindViewHolder(@NonNull Noticias_ViewHolder holder, int position) {

        DB_Noticias db_noticias = noticias.get(position);
        DBRef = FirebaseDatabase.getInstance().getReference().child("Noticias");

        holder.titulo.setText(db_noticias.getTitulo());
        holder.contenido.setText(db_noticias.getContenido());
        holder.btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditNoticia.class);
                i.putExtra(NOTICIA, db_noticias.getId_Noticia());
                i.putExtra(TIPO, db_noticias.getTipo_Noticia());
                v.getContext().startActivity(i);
            }
        });
        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder bld = new AlertDialog.Builder(v.getContext());
                bld.setCancelable(true);
                bld.setTitle("Eliminar Noticia");
                bld.setMessage("¿Seguro que quieres eliminar la noticia: " + db_noticias.getTitulo() + "?");
                bld.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBRef.child(db_noticias.getId_Noticia()).setValue(null);
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
        });
        holder.btn_Consulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), VistaNoticia.class);
                i.putExtra(NOTICIA, db_noticias.getId_Noticia());
                i.putExtra(TIPO, db_noticias.getTipo_Noticia());
                i.putExtra(TPUSR, TipoUsr);
                i.putExtra(USR, id_Usr);
                v.getContext().startActivity(i);
            }
        });
        if(!db_noticias.getImagen().equals("N/A")){
            Picasso.get().load(db_noticias.getImagen()).into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    public class Noticias_ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView titulo;
        TextView contenido;
        Button btn_Edit;
        Button btn_Delete;
        Button btn_Consulta;
        LinearLayout ll_btnsAdmin;
        LinearLayout ll_btnsUser;

        public Noticias_ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.rf_img);
            titulo = (TextView) itemView.findViewById(R.id.rf_txt_Titulo);
            contenido = (TextView) itemView.findViewById(R.id.rf_txt_Contenido);
            btn_Edit = (Button) itemView.findViewById(R.id.rf_btn_Editar);
            btn_Delete = (Button) itemView.findViewById(R.id.rf_btn_Eliminar);
            btn_Consulta = (Button) itemView.findViewById(R.id.rf_btn_Consultar);
            ll_btnsAdmin = (LinearLayout) itemView.findViewById(R.id.rf_ll_BtnsAdmin);

            if(TipoUsr.equals("1")){
                ll_btnsAdmin.setVisibility(View.GONE);
            }
        }
    }
}
