package com.example.proyectointegrador12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.proyectointegrador12.cultura.Cultura;
import com.example.proyectointegrador12.deportes.Deportes;

public class VistaNoticia extends AppCompatActivity {

    public static final String USUARIO = "com.example.proyectointegrador12.MainActivity.USUARIO";

    String id_Usr, id_Ntc;
    ImageView backbtn;
    int actFrom = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_noticia);

        //UI
        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        // obtiene la Activity de origen
        /*  0: Error
            1: Principal
            2: Cultura
            3: Deportes */
        actFrom = getActivity();
        id_Usr = getId_Usr(actFrom);
        id_Ntc = getId_Ntc(actFrom);
    }

    public int getActivity() {
        int activity = 0;
        /*  0: Error
            1: Principal
            2: Cultura
            3: Deportes */
        Intent i = getIntent();
        if(i.hasExtra(MainActivity.USUARIO)){
            activity = 1;
        } else if(i.hasExtra(Cultura.USUARIO)) {
            activity = 2;
        } else if(i.hasExtra(Deportes.USUARIO)) {
            activity = 3;
        }
        return activity;
    }

    public String getId_Usr(int actFrom) {
        String id_Usr = "";
        return id_Usr;
    }

    public String getId_Ntc(int actFrom) {
        String id_Ntc = "";
        return id_Ntc;
    }

    public void editar(View view) {
        Intent i = new Intent(getApplicationContext(), EditNoticia.class);
        i.putExtra(USUARIO, id_Usr);
        i.putExtra(USUARIO, id_Ntc);
        startActivity(i);
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void back() {
        this.finish();
    }
}