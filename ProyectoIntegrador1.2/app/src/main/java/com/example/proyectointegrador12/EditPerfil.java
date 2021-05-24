package com.example.proyectointegrador12;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectointegrador12.adapters.Adapter_Admins;
import com.example.proyectointegrador12.cultura.Cultura;
import com.example.proyectointegrador12.deportes.Deportes;
import com.example.proyectointegrador12.principal.Registro;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditPerfil extends AppCompatActivity {

    public static final String USUARIO = "com.example.proyectointegrador12.EditPerfil.USUARIO";
    private static final int PICK_IMAGE_REQUEST = 5;

    String idUsr = "";
    int activityOr = 0;
    EditText usr, correo, contra, verifcontra;
    ImageView backbtn, img;
    DatabaseReference DBRef;
    StorageReference STRef;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    boolean cambImg = false, guardado = false, Progress = false;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        //Login
        activityOr = getActivity();
        idUsr = getUsr(activityOr);

        //Database
        DBRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(idUsr);
        STRef = FirebaseStorage.getInstance().getReference().child("Usuarios");
        mAuth.signInAnonymously();

        //UI
        usr = findViewById(R.id.nombreinput);
        correo = findViewById(R.id.emailinput);
        contra = findViewById(R.id.contrasenainput);
        verifcontra = findViewById(R.id.contrasena2input);
        backbtn = findViewById(R.id.btn_back);
        img = findViewById(R.id.imagenPerfilEdit);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        //Hints
        DBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usr.setHint(dataSnapshot.child("nombreUsuario").getValue().toString());
                correo.setHint(dataSnapshot.child("correo").getValue().toString());
                contra.setHint("*****");
                verifcontra.setHint("*****");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public int getActivity() {
        int activity = 0;
        /*  0: MiPerfil
            1: Admins */
        Intent i = getIntent();
        if(i.hasExtra(MiPerfil.USUARIO)){
            activity = 0;
        } else if(i.hasExtra(Adapter_Admins.USUARIO)) {
            activity = 1;
        }
        return activity;
    }

    public String getUsr(int actOr){
        String usr = "";
        Intent i = getIntent();
        if(actOr == 0){
            usr = i.getStringExtra(MiPerfil.USUARIO);
        } else if(actOr == 1){
            usr = i.getStringExtra(Adapter_Admins.USUARIO);
        }
        return usr;
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MiPerfil.class);
        i.putExtra(USUARIO, idUsr);
        startActivity(i);
        this.finish();
    }

    public void back() {
        this.finish();
    }

    public void save(View view){

        boolean err = false;
        try{
            if(usr.getText().length() < 3 && usr.getText().length() > 0){
                err = true;
                usr.setError("El nombre de Usuario debe contener al menos 3 caracteres, o dejarse vacio para conservar los datos.");
            }
            if(contra.getText().length() < 5 && contra.getText().length() > 0){
                err = true;
                contra.setError("La contraseña debe contener al menos 5 caracteres, o dejarse vacio para conservar los datos.");
            }
            else if(!verifcontra.getText().toString().equals(contra.getText().toString())){
                err = true;
                verifcontra.setError("Ambas contraseñas deben coincidir");
            }
            if(!err){
                guardado = false;
                DBRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!guardado){
                            if(usr.getText().length() > 0) {
                                DBRef.child("nombreUsuario").setValue(usr.getText().toString());
                            }
                            if(contra.getText().length() > 0) {
                                DBRef.child("contrasena").setValue(contra.getText().toString());
                            }
                            if(correo.getText().length() > 0) {
                                DBRef.child("correo").setValue(correo.getText().toString());
                            }
                            if(cambImg){
                                StorageReference filereference = STRef.child(System.currentTimeMillis() + "." + getFileExt(uri));
                                filereference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Progress = false;
                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                DBRef.child("imagen").setValue(uri.toString());
                                                //Picasso.get().load(dataSnapshot.child(idUsr).child("Imagen").getValue().toString()).into(Img);
                                            }
                                        });
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Progress = false;
                                                Toast.makeText(EditPerfil.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                                Progress = true;
                                            }
                                        });
                            }
                            guardado = true;
                            Toast.makeText(EditPerfil.this, "El usuario se ha Modificado con exito", Toast.LENGTH_LONG).show();
                            back();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }catch (Exception e){
            Log.e("Error", "Error--" + e.getMessage().toString());
            Toast.makeText(EditPerfil.this, "Ocurrio un Error --->  " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

    }

    public String getFileExt(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void OpenFileChooser(View view)
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            cambImg = true;
            uri = data.getData();
            Picasso.get().load(uri).into(img);
        }
    }
}