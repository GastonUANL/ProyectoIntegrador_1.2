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

        // EXTRAS
        Intent i = getIntent();
        id_Usr = i.getStringExtra(MenuDinamico.USUARIO);
        id_Ntc = i.getStringExtra(MenuDinamico.NOTICIA);

        Toast.makeText(EditNoticia.this,  id_Usr + ", " + id_Ntc, Toast.LENGTH_LONG).show();


    }

    public void back() {
        this.finish();
    }
}