package com.example.proyectointegrador12;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectointegrador12.adapters.Adapter_Cultura;
import com.example.proyectointegrador12.adapters.Adapter_Deportes;
import com.example.proyectointegrador12.adapters.Adapter_Principal;
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

import java.util.Calendar;

public class EditNoticia extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 5;

    String id_Tp, id_Ntc, id_Usr;
    EditText titulo, descripcion;
    Button btnImg;
    ImageView imagen;
    Uri uri;
    boolean cambImg = false, Progress = false, toSaveNew = false;
    DatabaseReference DBRef;
    StorageReference STRef;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    int actFrom = 0;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_noticia);

        //DB
        DBRef = FirebaseDatabase.getInstance().getReference().child("Noticias");
        STRef = FirebaseStorage.getInstance().getReference().child("Noticias");
        mAuth.signInAnonymously();

        //UI
        back = findViewById(R.id.btn_back_EditN);
        titulo = findViewById(R.id.txt_TituloEditN);
        descripcion = findViewById(R.id.txt_DescripcionEditNt);
        imagen = findViewById(R.id.img_editNt);
        btnImg = findViewById(R.id.btn_imgEditNt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        // EXTRAS
        Intent i = getIntent();
        actFrom = getFrom(i);

        id_Ntc = getNtc(actFrom, i);
        id_Tp = getTp(actFrom, i);
        id_Usr = getUsr(actFrom, i);

        //Hint
        DBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(actFrom==1 || actFrom==2){
                    titulo.setHint(dataSnapshot.child(id_Ntc).child("titulo").getValue().toString());
                    descripcion.setHint(dataSnapshot.child(id_Ntc).child("contenido").getValue().toString());
                    String img = dataSnapshot.child(id_Ntc).child("imagen").getValue().toString();
                    if(!img.equals("N/A")){
                        Picasso.get().load(img).into(imagen);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getUsr(int actFrom, Intent i) {
        if(actFrom==0){
            return i.getStringExtra(MenuDinamico.USUARIO);
        }
        return "1";
    }

    private String getTp(int actFrom, Intent i) {
        if(actFrom == 1){
            return i.getStringExtra(Adapter_Principal.TIPO);
        } else if(actFrom == 2){
            return i.getStringExtra(VistaNoticia.NTC);
        } else if(actFrom == 0){
            return i.getStringExtra(MenuDinamico.TIPO);
        }
        return "1";
    }

    private String getNtc(int actFrom, Intent i) {
        if(actFrom == 1){
            return i.getStringExtra(Adapter_Principal.NOTICIA);
        } else if(actFrom == 2){
            return i.getStringExtra(VistaNoticia.NTC);
        }
        return "0";
    }

    private int getFrom(Intent i) {
        if(i.hasExtra(Adapter_Principal.NOTICIA)){
            return 1;
        } else if(i.hasExtra(VistaNoticia.NTC)){
            return 2;
        }
        return 0;
    }

    public void back() {
        this.finish();
    }

    public void save(View v){
        if(actFrom == 1 || actFrom == 2){
            saveEdit();
        } else {
            saveNew();
        }
    }

    private void saveNew() {
        boolean err = false;
        if(titulo.getText().toString().length() < 5){
            titulo.setError("El titulo de la noticia debe contener al menos 5 caracteres");
            err = true;
        }
        if(descripcion.getText().toString().length() < 5){
            titulo.setError("El contenido de la noticia debe contener al menos 5 caracteres");
            err = true;
        }
        if(err){
            return;
        }
        toSaveNew = true;
        DBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(toSaveNew){
                    int cont = 1;
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        if(!ds.child("id_Noticia").getValue().toString().equals("" + cont)){
                            id_Ntc = "" + cont;
                            break;
                        } else {
                            cont++;
                            id_Ntc = "" + cont;
                        }
                    }
                    DBRef.child(id_Ntc).child("titulo").setValue(titulo.getText().toString());
                    DBRef.child(id_Ntc).child("id_Noticia").setValue(id_Ntc);
                    DBRef.child(id_Ntc).child("contenido").setValue(descripcion.getText().toString());
                    Calendar rightNow = Calendar.getInstance();
                    int dia, mes, ano;
                    dia = rightNow.get(Calendar.DAY_OF_MONTH);
                    mes = rightNow.get(Calendar.MONTH);
                    ano = rightNow.get(Calendar.YEAR);
                    String fecha = dia + "," + mes + "," + ano;
                    DBRef.child(id_Ntc).child("fecha").setValue(fecha);
                    DBRef.child(id_Ntc).child("id_Usr").setValue(id_Usr);
                    DBRef.child(id_Ntc).child("tipo_Noticia").setValue(id_Tp);
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
                                        DBRef.child(id_Ntc).child("imagen").setValue(uri.toString());
                                        //Picasso.get().load(dataSnapshot.child(idUsr).child("Imagen").getValue().toString()).into(Img);
                                    }
                                });
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Progress = false;
                                        Toast.makeText(EditNoticia.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                        Progress = true;
                                    }
                                });
                    } else {
                        DBRef.child(id_Ntc).child("imagen").setValue("N/A");
                    }
                    toSaveNew = false;
                    EditNoticia.this.finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveEdit() {
        boolean err = false;
        if(titulo.getText().toString().length() < 5 && titulo.getText().toString().length() > 0){
            titulo.setError("El titulo de la noticia debe contener al menos 5 caracteres, o deje e campo vacio para mantener sus datos originales");
            err = true;
        }
        if(descripcion.getText().toString().length() < 5 && descripcion.getText().toString().length() > 0){
            titulo.setError("El contenido de la noticia debe contener al menos 5 caracteres, o deje e campo vacio para mantener sus datos originales.");
            err = true;
        }
        if(err){
            return;
        }
        if (titulo.getText().toString().length() > 0){ DBRef.child(id_Ntc).child("titulo").setValue(titulo.getText().toString()); }
        if (descripcion.getText().toString().length() > 0){ DBRef.child(id_Ntc).child("contenido").setValue(descripcion.getText().toString()); }
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
                            DBRef.child(id_Ntc).child("imagen").setValue(uri.toString());
                            //Picasso.get().load(dataSnapshot.child(idUsr).child("Imagen").getValue().toString()).into(Img);
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Progress = false;
                            Toast.makeText(EditNoticia.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            Progress = true;
                        }
                    });
        } else {
            DBRef.child(id_Ntc).child("imagen").setValue("N/A");
        }
        this.finish();
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
            Picasso.get().load(uri).into(imagen);
        }
    }
}