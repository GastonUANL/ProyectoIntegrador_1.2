package com.example.proyectointegrador12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectointegrador12.cultura.Cultura;
import com.example.proyectointegrador12.deportes.Deportes;
import com.example.proyectointegrador12.principal.Registro;

public class EditNoticia extends AppCompatActivity {

    String id_Usr, id_Ntc;
    int actFrom = 0;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_noticia);

        //UI
        back = findViewById(R.id.btn_back_EditN);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        // obtiene la Activity de origen
        /*  0: Error
            1: Principal
            2: Cultura
            3: Deportes
            4: VistaNoticia */
        actFrom = getActivity();
        id_Usr = getId_Usr(actFrom);
        id_Ntc = getId_Ntc(actFrom);

    }

    public int getActivity() {
        int activity = 0;
        /*  0: Error
            1: Principal
            2: Cultura
            3: Deportes
            4: VistaNoticia */
        Intent i = getIntent();
        if(i.hasExtra(MainActivity.USUARIO)){
            activity = 1;
        } else if(i.hasExtra(Cultura.USUARIO)) {
            activity = 2;
        } else if(i.hasExtra(Deportes.USUARIO)) {
            activity = 3;
        } else if(i.hasExtra(VistaNoticia.USUARIO)) {
            activity = 4;
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

    public void back() {
        this.finish();
    }
}