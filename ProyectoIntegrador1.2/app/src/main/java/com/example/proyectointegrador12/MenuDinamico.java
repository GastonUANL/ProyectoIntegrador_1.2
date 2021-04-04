package com.example.proyectointegrador12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.proyectointegrador12.admins.Administradores;
import com.example.proyectointegrador12.cultura.Cultura;
import com.example.proyectointegrador12.deportes.Deportes;
import com.example.proyectointegrador12.miperfil.MiPerfil;
import com.example.proyectointegrador12.preguntas.Preguntas;
import com.example.proyectointegrador12.principal.Principal;
import com.google.android.material.navigation.NavigationView;

public class MenuDinamico extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout dl;
    NavigationView nv;
    Toolbar tb;
    MenuItem lastOpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dinamico);

        //UI
        dl = findViewById(R.id.drawer_layout);
        nv = findViewById(R.id.nav_view);
        tb = findViewById(R.id.toolbar);
        getSupportFragmentManager().beginTransaction().add(R.id.content, new Principal()).commit();
        setTitle("Principal");

        //setup Toolbar
        setSupportActionBar(tb);
        nv.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectItemNav(item);
        return true;
    }

    private void selectItemNav(MenuItem item){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch(item.getItemId()){
            case R.id.btn_Cultura:{
                ft.replace(R.id.content, new Cultura()).commit();
                break;
            }
            case R.id.btn_Deportes:{
                ft.replace(R.id.content, new Deportes()).commit();
                break;
            }
            case R.id.btn_Preguntas:{
                ft.replace(R.id.content, new Preguntas()).commit();
                break;
            }
            case R.id.btn_MiPerful:{
                ft.replace(R.id.content, new MiPerfil()).commit();
                break;
            }
            case R.id.btn_Admins:{
                ft.replace(R.id.content, new Administradores()).commit();
                break;
            }
            case R.id.btn_Logout:{
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                break;
            }
            default:{
                ft.replace(R.id.content, new Principal()).commit();
                break;
            }
        }
        setTitle(item.getTitle());
        dl.closeDrawers();
    }
}