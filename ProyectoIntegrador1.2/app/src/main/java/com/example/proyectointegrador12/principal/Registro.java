package com.example.proyectointegrador12.principal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.proyectointegrador12.EditNoticia;
import com.example.proyectointegrador12.EditPerfil;
import com.example.proyectointegrador12.MainActivity;
import com.example.proyectointegrador12.R;

public class Registro extends AppCompatActivity {

    public static final String USUARIO = "com.example.proyectointegrador12.Registro.USUARIO";
    ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //UI
        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        this.finish();
    }

    public void guardar(View view) {

    }

    public void back() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        this.finish();
    }
}