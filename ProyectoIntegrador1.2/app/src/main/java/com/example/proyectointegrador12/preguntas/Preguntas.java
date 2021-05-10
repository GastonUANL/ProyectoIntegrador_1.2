package com.example.proyectointegrador12.preguntas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.proyectointegrador12.EditPerfil;
import com.example.proyectointegrador12.EditPregunta;
import com.example.proyectointegrador12.MenuDinamico;
import com.example.proyectointegrador12.R;
import com.example.proyectointegrador12.adapters.Adapter_Preguntas;
import com.example.proyectointegrador12.adapters.Adapter_Principal;
import com.example.proyectointegrador12.db.DB_Noticias;
import com.example.proyectointegrador12.db.DB_Preguntas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Preguntas extends Fragment {

    public static final String USUARIO = "com.example.proyectointegrador12.preguntas.Preguntas.USUARIO";
    public static final String TIPO = "com.example.proyectointegrador12.preguntas.Preguntas.TIPO";

    FirebaseDatabase DBRef;
    RecyclerView rv;
    Adapter_Preguntas adapter;
    List<DB_Preguntas> preguntas;
    LinearLayout ll_add;

    String TipoUsr = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preguntas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //DB
        DBRef = FirebaseDatabase.getInstance();

        MenuDinamico MD = (MenuDinamico) getActivity();
        TipoUsr = MD.getTipoUsr();

        //UI
        preguntas = new ArrayList<>();
        rv = view.findViewById(R.id.rv_Preguntas);
        adapter = new Adapter_Preguntas(preguntas, TipoUsr);
        ll_add = view.findViewById(R.id.rf_ll_BtnsAdminFragP);

        //Hide
        if(TipoUsr.equals("1")){
            ll_add.setVisibility(view.GONE);
        }

        //Rexicler View
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        //Content
        DBRef.getReference().getRoot().child("Preguntas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                preguntas.removeAll(preguntas);
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                        DB_Preguntas db_preguntas = dataSnapshot.child(ds.getKey()).getValue(DB_Preguntas.class);
                        preguntas.add(db_preguntas);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void add(View view){
        Intent i = new Intent(view.getContext(), EditPregunta.class);
        i.putExtra(TIPO, "0");
        startActivity(i);
    }


}