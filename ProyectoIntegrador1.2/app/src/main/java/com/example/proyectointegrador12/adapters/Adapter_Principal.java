package com.example.proyectointegrador12.adapters;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectointegrador12.MenuDinamico;
import com.example.proyectointegrador12.R;
import com.example.proyectointegrador12.db.DB_Noticias;
import com.example.proyectointegrador12.principal.Principal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Principal extends RecyclerView.Adapter<Adapter_Principal.Noticias_ViewHolder> {

    List<DB_Noticias> noticiasFull, noticias;
    String TipoUsr = "";

    public Adapter_Principal(List<DB_Noticias> noticias, String tipoUsr) {
        this.noticias = noticias;
        noticiasFull = new ArrayList<>(noticias);
        TipoUsr = tipoUsr;
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

        holder.titulo.setText(db_noticias.getTitulo());
        holder.contenido.setText(db_noticias.getContenido());
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
