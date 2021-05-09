package com.example.proyectointegrador12;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectointegrador12.adapters.Adapter_Principal;
import com.example.proyectointegrador12.cultura.Cultura;
import com.example.proyectointegrador12.db.DB_Noticias;
import com.example.proyectointegrador12.deportes.Deportes;
import com.example.proyectointegrador12.preguntas.Preguntas;
import com.example.proyectointegrador12.principal.Principal;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuDinamico extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String USUARIO = "com.example.proyectointegrador12.MenuDinamico.USUARIO";
    public static final String NOTICIA = "com.example.proyectointegrador12.MenuDinamico.NOTICIA";

    DrawerLayout dl;
    NavigationView nv;
    Toolbar tb;
    ActionBarDrawerToggle toggle;
    String idUsr = "";
    public String tipoUsr = "";
    ImageView imgPrerfil;
    TextView namePerfil;

    DatabaseReference DBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dinamico);

        //Login
        Intent i = getIntent();
        idUsr = i.getStringExtra(MainActivity.USUARIO);



        //UI
        dl = findViewById(R.id.drawer_layout);
        nv = findViewById(R.id.nav_view);
        tb = findViewById(R.id.toolbar);

        getSupportFragmentManager().beginTransaction().add(R.id.content, new Principal()).commit();
        setTitle("Principal");

        //setup Toolbar
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggle = new ActionBarDrawerToggle(this,
                dl,
                tb,
                R.string.drawer_open,
                R.string.drawer_close
        );
        dl.addDrawerListener(toggle);

        tb.setNavigationIcon(R.drawable.ic_menu);

        nv.setNavigationItemSelectedListener(this);

        hideItmes(nv);

        //Database
        DBRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(idUsr);

        //set data usr
        View headView = nv.getHeaderView(0);
        imgPrerfil = headView.findViewById(R.id.img_nav_perfil);
        namePerfil = headView.findViewById(R.id.txt_nav_nombreusr);

        DBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namePerfil.setText(dataSnapshot.child("nombreUsuario").getValue().toString());
                if(!dataSnapshot.child("imagen").getValue().toString().equals("N/A")) {
                    Picasso.get().load(dataSnapshot.child("imagen").getValue().toString()).into(imgPrerfil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hideItmes(NavigationView nv) {
        Menu nav_Menu = nv.getMenu();
        Intent i = getIntent();
        tipoUsr = i.getStringExtra(MainActivity.TIPOUSR);
        int tipo = Integer.parseInt(tipoUsr);
        if(tipo == 3){
            nav_Menu.findItem(R.id.btn_MiPerful).setVisible(false);
            nav_Menu.findItem(R.id.btn_Admins).setVisible(true);
        } else if(tipo == 2){
            nav_Menu.findItem(R.id.btn_MiPerful).setVisible(false);
            nav_Menu.findItem(R.id.btn_Admins).setVisible(false);
        } else if(tipo == 1){
            nav_Menu.findItem(R.id.btn_MiPerful).setVisible(true);
            nav_Menu.findItem(R.id.btn_Admins).setVisible(false);
        }


    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
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
                setTitle(item.getTitle());
                break;
            }
            case R.id.btn_Deportes:{
                ft.replace(R.id.content, new Deportes()).commit();
                setTitle(item.getTitle());
                break;
            }
            case R.id.btn_Preguntas:{
                ft.replace(R.id.content, new Preguntas()).commit();
                setTitle(item.getTitle());
                break;
            }
            case R.id.btn_MiPerful:{
                Intent i = new Intent(getApplicationContext(), MiPerfil.class);
                i.putExtra(USUARIO, idUsr);
                startActivity(i);
                break;
            }
            case R.id.btn_Admins:{
                Intent i = new Intent(getApplicationContext(), Administradores.class);
                startActivity(i);
                break;
            }
            case R.id.btn_Logout:{
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                this.finish();
                break;
            }
            default:{
                ft.replace(R.id.content, new Principal()).commit();
                setTitle(item.getTitle());
                break;
            }
        }
        dl.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if(dl.isDrawerOpen(GravityCompat.START)){
            dl.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            this.finish();
        }
    }

    public void editNoticia(View view) {
        Intent i = new Intent(getApplicationContext(), EditNoticia.class);
        i.putExtra(USUARIO, "id_Usr");
        i.putExtra(NOTICIA, "id_Ntc");
        startActivity(i);
    }

    public String getTipoUsr() {
        return tipoUsr;
    }
}