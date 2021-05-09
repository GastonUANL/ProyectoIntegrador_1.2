package com.example.proyectointegrador12.principal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectointegrador12.Administradores;
import com.example.proyectointegrador12.EditNoticia;
import com.example.proyectointegrador12.EditPerfil;
import com.example.proyectointegrador12.MainActivity;
import com.example.proyectointegrador12.MenuDinamico;
import com.example.proyectointegrador12.MiPerfil;
import com.example.proyectointegrador12.R;
import com.example.proyectointegrador12.adapters.Adapter_Admins;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

public class Registro extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 5;
    public static final String USUARIO = "com.example.proyectointegrador12.Registro.USUARIO";
    ImageView backbtn;
    boolean cambImg, guardado, Progress = false;
    ImageView Img;
    Uri img;
    DatabaseReference DBRef;
    StorageReference STRef;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    EditText nombre, correo, contra1, contra2;
    String idUsr = "";
    int tipoReg = 1; //1: normal, 2: Admin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Database
        DBRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        STRef = FirebaseStorage.getInstance().getReference().child("Usuarios");
        mAuth.signInAnonymously();

        //UI
        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        Img = findViewById(R.id.imagenPerfil);
        nombre = findViewById(R.id.nombreinput);
        correo = findViewById(R.id.emailinput);
        contra1 = findViewById(R.id.contrasenainput);
        contra2 = findViewById(R.id.contrasena2input);

        //GetTipo
        tipoReg = getActivity();
    }

    public int getActivity() {
        int activity = 1;
        /*  0: MiPerfil
            1: Admins */
        Intent i = getIntent();
        if(i.hasExtra(MainActivity.TIPOP)){
            activity = 1;
        } else if(i.hasExtra(Administradores.TIPOP)) {
            activity = 2;
        }
        return activity;
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void guardar(View view) {
        boolean err = false;
        try{
            if(nombre.getText().length() < 3){
                err = true;
                nombre.setError("El nombre de Usuario debe contener al menos 3 caracteres.");
            }
            if(contra1.getText().length() < 5){
                err = true;
                contra1.setError("La contraseña debe contener al menos 5 caracteres");
            }
            else if(!contra2.getText().toString().equals(contra1.getText().toString())){
                err = true;
                contra2.setError("Ambas contraseñas deben coincidir");
            }
            if(!err){
                guardado = false;
                DBRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!guardado){
                            boolean encontrado = false;
                            boolean valido = true;
                            int cont = 1;
                            for(DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                if(
                                        nombre.getText().toString().equals(ds.child("nombreUsuario").getValue().toString()) |
                                        correo.getText().toString().equals(ds.child("correo").getValue().toString())
                                ) {
                                    valido = false;
                                }
                            }
                            if(valido) {
                                while (!encontrado) {
                                    if (!dataSnapshot.child("" + cont).exists()) {
                                        idUsr = "" + cont;
                                        encontrado = true;
                                    }
                                    else { cont++; }
                                }
                                DBRef.child(idUsr).child("activo").setValue("1");
                                DBRef.child(idUsr).child("contrasena").setValue(contra1.getText().toString());
                                DBRef.child(idUsr).child("correo").setValue(correo.getText().toString());
                                DBRef.child(idUsr).child("id_Usr").setValue(idUsr);
                                DBRef.child(idUsr).child("nombreUsuario").setValue(nombre.getText().toString());
                                DBRef.child(idUsr).child("tipo_Usr").setValue("1");
                                if(cambImg){
                                    StorageReference filereference = STRef.child(System.currentTimeMillis() + "." + getFileExt(img));
                                    filereference.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Progress = false;
                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    DBRef.child(idUsr).child("imagen").setValue(uri.toString());
                                                    //Picasso.get().load(dataSnapshot.child(idUsr).child("Imagen").getValue().toString()).into(Img);
                                                }
                                            });
                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Progress = false;
                                                    Toast.makeText(Registro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                                    Progress = true;
                                                }
                                            });
                                } else {
                                    DBRef.child(idUsr).child("imagen").setValue("N/A");
                                }
                                Toast.makeText(Registro.this, "El usuario se ha creado con exito", Toast.LENGTH_LONG).show();
                                back();
                            } else {
                                Toast.makeText(Registro.this, "Ya existe un usuario registrado con ese nombre/correo, favor de intentar con otro.", Toast.LENGTH_LONG).show();
                            }
                            guardado = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }catch (Exception e){
            Log.e("Error", "Error--" + e.getMessage().toString());
            Toast.makeText(Registro.this, "Ocurrio un Error --->  " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public String getFileExt(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void back() {
        this.finish();
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
            img = data.getData();
            Picasso.get().load(img).into(Img);
        }
    }
}