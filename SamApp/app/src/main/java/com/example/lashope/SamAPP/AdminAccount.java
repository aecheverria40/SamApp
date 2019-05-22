package com.example.lashope.SamAPP;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lashope.SamAPP.Models.Proyector;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AdminAccount extends AppCompatActivity {

    private List<Proyector> listPerson = new ArrayList<Proyector>();
    ArrayAdapter<Proyector> arrayAdapterPersona;
    private Button mSelectImage;
    private StorageReference mStorage;
    private Uri uri;
    private String nombre_proyector = "";


    private EditText edMar;
    private EditText edMod;
    private EditText edDis;
    private EditText edMas;
    private ListView listV_proyectores;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Proyector personSelected;
    private Button button;
    private BottomNavigationView bottomNavigationView;
    private Menu menu;
    private MenuItem menuItem;
    private TextView text_file;
    String downloadUrl;
    private static final int GALLERY_INTENT=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);

        mSelectImage = (Button)findViewById(R.id.btn_upload);
        mStorage = FirebaseStorage.getInstance().getReference();
        text_file = (TextView)findViewById(R.id.txt_upload);


        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        /*
        button = (Button)findViewById(R.id.btn_logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        */
        edMar = (EditText)findViewById(R.id.txt_marca);
        edMod = (EditText)findViewById(R.id.txt_modelo);
        edDis = (EditText)findViewById(R.id.txt_disponible);
        edMas = (EditText)findViewById(R.id.txt_maestro);

        listV_proyectores = findViewById(R.id.lv_datosProyectores);

        initializeFirebase();

        // ArrayList
        listData();

        listV_proyectores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                personSelected = (Proyector) adapterView.getItemAtPosition(i);
                edMar.setText(personSelected.getMarca());
                edMod.setText(personSelected.getModelo());
                edDis.setText(personSelected.getDisponible());
                edMas.setText(personSelected.getMaestro());
                text_file.setText(personSelected.getProyector());
                nombre_proyector = personSelected.getProyector();
            }
        });
        // Navigation Bar
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_barAdmin);
        menu = bottomNavigationView.getMenu();
        menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.audio:
                        break;
                    case R.id.proyector:
                        startActivity(new Intent(AdminAccount.this,AltasActivity.class));
                        finish();
                        break;
                    case R.id.admin_logout:
                        SignOut();
                        finish();
                        break;
                }
                return false;
            }
        });
        /*
        if(text_file.getText().equals("Subir archivo..."))
        {
            Toast.makeText(getApplicationContext(),"IGUAL",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),"NO IGUAL",Toast.LENGTH_SHORT).show();
        }
        */
        // Toast.makeText(getApplicationContext(),text_file.getText(),Toast.LENGTH_SHORT).show();
    }
    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
    }
    private void listData(){
        databaseReference.child("Proyector").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listPerson.clear();
                        for(DataSnapshot objSnapShot :
                                dataSnapshot.getChildren()){
                            Proyector p = objSnapShot.getValue(Proyector.class);
                            listPerson.add(p);
                            arrayAdapterPersona =
                                    new ArrayAdapter<Proyector>
                                            (AdminAccount.this,
                                                    android.R.layout.simple_list_item_1,
                                                    listPerson);
                            listV_proyectores.setAdapter(arrayAdapterPersona);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            uri = data.getData();
            text_file.setText(uri.getLastPathSegment());
            nombre_proyector = uri.getLastPathSegment();
            text_file.setText(nombre_proyector);
            /*
            StorageReference filepath = mStorage
                    .child("images")
                    .child("Proyectores")
                    .child("2eed46a8-31af-41d7-872d-cd69df0a5549")
                    .child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"Subido",Toast.LENGTH_LONG).show();

                }
            });
            */
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        // Add Icon
        Drawable drawable = menu.findItem(R.id.icon_add).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this,R.color.white));
        menu.findItem(R.id.icon_add).setIcon(drawable);

        // Delete Icon
        Drawable bdrawable = menu.findItem(R.id.icon_delete).getIcon();
        bdrawable = DrawableCompat.wrap(bdrawable);
        DrawableCompat.setTint(bdrawable, ContextCompat.getColor(this,R.color.white));
        menu.findItem(R.id.icon_delete).setIcon(bdrawable);

        // Update Icon
        Drawable cdrawable = menu.findItem(R.id.icon_save).getIcon();
        cdrawable = DrawableCompat.wrap(cdrawable);
        DrawableCompat.setTint(cdrawable, ContextCompat.getColor(this,R.color.white));
        menu.findItem(R.id.icon_save).setIcon(cdrawable);

        return super.onCreateOptionsMenu(menu);
    }
    private void initializeFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String marca = edMar.getText().toString();
        String modelo = edMod.getText().toString();
        String disponibilidad = edDis.getText().toString();
        String maestro = edMas.getText().toString();
        switch (item.getItemId()){
            case R.id.icon_add:
                if(marca.equals("")||modelo.equals("")||disponibilidad.equals("")||
                        maestro.equals("")||text_file.getText().equals("Subir archivo...")){
                    validation();
                }
                else {
                    Proyector p = new Proyector();
                    p.setId(UUID.randomUUID().toString());
                    p.setMarca(marca);
                    p.setModelo(modelo);
                    p.setDisponible(disponibilidad);
                    p.setMaestro(maestro);
                    p.setProyector(nombre_proyector);
                    StorageReference filepath = mStorage
                            .child("images")
                            .child("Proyectores")
                            .child(p.getId())
                            //.child("2eed46a8-31af-41d7-872d-cd69df0a5549")
                            .child(uri.getLastPathSegment());
                    /*
                    filepath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            downloadUrl = task.getResult().toString();
                            // downloadurl will be the resulted answer
                        }
                    });
                    */
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(),"Subido",Toast.LENGTH_LONG).show();
                            // Uri descarga = taskSnapshot.getUploadSessionUri();
                            // Task<Uri> downUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            // Log.i("url:",downUrl.getResult().toString());
                            //Toast.makeText(getApplicationContext(),downUrl.getResult().toString(),Toast.LENGTH_LONG).show();

                        }
                    });
                    databaseReference.child("Proyector").child(p.getId()).setValue(p);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_SHORT).show();
                    clearText();
                }
                break;
            case R.id.icon_save:
                Proyector p = new Proyector();

                p.setId(personSelected.getId());
                p.setMarca(edMar.getText().toString().trim());
                p.setModelo(edMod.getText().toString().trim());
                p.setDisponible(edDis.getText().toString().trim());
                p.setMaestro(edMas.getText().toString().trim());

                // p.setProyector(nombre_proyector);

                String tp = personSelected.getProyector();
                if(tp.equals(nombre_proyector))
                {
                    p.setProyector(personSelected.getProyector());

                }else {
                    StorageReference filepath = mStorage
                            .child("images")
                            .child("Proyectores")
                            .child(p.getId())
                            //.child("2eed46a8-31af-41d7-872d-cd69df0a5549")
                            .child(uri.getLastPathSegment());
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(),"Subido",Toast.LENGTH_LONG).show();
                            // Uri descarga = taskSnapshot.getUploadSessionUri();
                            // Task<Uri> downUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            // Log.i("url:",downUrl.getResult().toString());
                            //Toast.makeText(getApplicationContext(),downUrl.getResult().toString(),Toast.LENGTH_LONG).show();

                        }
                    });
                    p.setProyector(nombre_proyector);

                }
                /*
                StorageReference filepath = mStorage
                        .child("images")
                        .child("Proyectores")
                        .child(p.getId())
                        //.child("2eed46a8-31af-41d7-872d-cd69df0a5549")
                        .child(uri.getLastPathSegment());
                        */
                    /*
                    filepath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            downloadUrl = task.getResult().toString();
                            // downloadurl will be the resulted answer
                        }
                    });
                    */
                    /*
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Subido",Toast.LENGTH_LONG).show();
                        // Uri descarga = taskSnapshot.getUploadSessionUri();
                        // Task<Uri> downUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        // Log.i("url:",downUrl.getResult().toString());
                        //Toast.makeText(getApplicationContext(),downUrl.getResult().toString(),Toast.LENGTH_LONG).show();

                    }
                });
                */

                databaseReference.child("Proyector").child(p.getId()).
                        setValue(p);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show();
                clearText();
                break;
            case R.id.icon_delete:
                Proyector pe = new Proyector();
                pe.setId(personSelected.getId());
                databaseReference.child("Proyector").child(pe.getId()).removeValue();
                Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        nombre_proyector = "";
        return true;
        // return super.onOptionsItemSelected(item);
    }
    private void clearText(){
        edMar.setText("");
        edMod.setText("");
        edDis.setText("");
        edMas.setText("");
        text_file.setText("Subir archivo...");
    }
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    private void validation(){
        String marca = edMar.getText().toString();
        String modelo = edMod.getText().toString();
        String disponibilidad = edDis.getText().toString();
        String maestro = edMas.getText().toString();
        if(marca.equals("")){
            edMar.setError("Requerido");
        } else if(modelo.equals("")){
            edMod.setError("Requerido");
        } else if (disponibilidad.equals("")){
            edDis.setError("Requerido");
        } else if (maestro.equals("")){
            edMas.setError("Requerido");
        } else if (text_file.getText().equals("Subir archivo...")){
            text_file.setError("Requerido");
        }
    }

}
