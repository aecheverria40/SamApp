package com.example.lashope.SamAPP;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lashope.SamAPP.Models.Proyector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ConsultarProyector extends AppCompatActivity {

    private ArrayList<Proyector> productList;
    private ImageView img;
    private BottomNavigationView bottomNavigationView;
    private Menu menu;
    private MenuItem menuItem;
    FirebaseDatabase firebaseDatabase;
    // private Button button;
    DatabaseReference databaseReference;
    ListView lview;
    listviewAdapter adapter;
    private StorageReference mStorage;
    private Uri downloadUrl;
    private String selected_url,u;
    String sno, product, category, price;
    // Crear constante tag
    private final static String TAG ="etiqueta";
    private final static String KEY_INDEX="indice";
    private static final String SEND_MESSAGE="message";
    private static final String EXTRA_NAME_SHOWN="extraName";

    //Asignacion del valor de la variable mensage a la constante
    //SEND_MESSAGE
    public static Intent newIntent(Context packageContext, String message){
        Intent i= new Intent(packageContext,ConsultarProyector.class);
        i.putExtra(SEND_MESSAGE,message);
        return i;
    }
    private void initializeFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
    //Metodo que se manda llamar desde la MainACTIVITY PARA OBTENER EL VALOR DE LA VARIABLE NAME Que se arguardp en la ocnstante
    /*public static String wasNameShown(Intent result){
        return result.getStringExtra(EXTRA_NAME_SHOWN);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_proyector);
        //quitar ActionBar
        getSupportActionBar().hide();

        initializeFirebase();
        mStorage = FirebaseStorage.getInstance().getReference();

        productList = new ArrayList<Proyector>();
        lview = (ListView) findViewById(R.id.listview);
        adapter = new listviewAdapter(this, productList);
        lview.setAdapter(adapter);

        populateList();

        adapter.notifyDataSetChanged();
        // button = (Button)findViewById(R.id.btn_act);

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG","ES 1");
                Proyector temp = null;

                sno = ((TextView)view.findViewById(R.id.sNo)).getText().toString();
                product = ((TextView)view.findViewById(R.id.product)).getText().toString();
                category = ((TextView)view.findViewById(R.id.category)).getText().toString();
                price = ((TextView)view.findViewById(R.id.price)).getText().toString();



                Log.d("TAG","ES 2");
                for(int i=0; i < productList.size();i++){
                    if (productList.get(i).getMarca() == sno) {
                        temp = (Proyector)productList.get(i);
                        u = temp.getId();
                    }
                }
                // Toast.makeText(getApplicationContext(),u,Toast.LENGTH_LONG).show();
                //---IMAGEN
                StorageReference filepath = mStorage
                        .child("images")
                        .child("Proyectores")
                        .child(temp.getId())
                        //.child("2eed46a8-31af-41d7-872d-cd69df0a5549")
                        .child(temp.getProyector());
                selected_url = temp.getProyector();

                filepath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        downloadUrl = task.getResult();
                        // Toast.makeText(getApplicationContext(),downloadUrl.toString(),Toast.LENGTH_LONG).show();
                        // if (temp != null) {
                        img = (ImageView)findViewById(R.id.img_view);
                        // img.setImageResource(temp.getProyector());
                        // Toast.makeText(AltasActivity.this,"IMAGENA",Toast.LENGTH_SHORT).show();
                        Glide.with(ConsultarProyector.this).load(downloadUrl)
                                .fitCenter()
                                .centerCrop()
                                .into(img);
                        // }
                        // downloadurl will be the resulted answer
                    }
                });
                // Toast.makeText(getApplicationContext(),downloadUrl.toString(),Toast.LENGTH_LONG).show();
                // IMAGEN
                Log.d("TAG","ES 3");
                /*
                if (temp != null) {
                    img = (ImageView)findViewById(R.id.img_view);
                    // img.setImageResource(temp.getProyector());
                    // Toast.makeText(AltasActivity.this,"IMAGENA",Toast.LENGTH_SHORT).show();
                    Glide.with(ConsultarProyector.this).load(downloadUrl)
                            .fitCenter()
                            .centerCrop()
                            .into(img);
                }
                */
                Log.d("TAG","ES 4");
                /*
                Toast.makeText(getApplicationContext(), "Marca : " + sno +"\n"
                        +"Modelo : " + product +"\n"
                        +"Disponible : " +category +"\n"
                        +"Maestro : " +price, Toast.LENGTH_SHORT).show();
                */
            }
        });
        /*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar();
            }
        });
        */
        // Navigation Bar
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_bar);
        menu = bottomNavigationView.getMenu();
        menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.audio:
                        Intent a = new Intent(ConsultarProyector.this,ReservarAudiovisual.class);
                        startActivity(a);
                        finish();
                        break;
                    case R.id.proyector:
                        Intent b = new Intent(ConsultarProyector.this,ReservarAudiovisual.class);
                        startActivity(b);
                        finish();
                        break;
                    case R.id.otros:
                        finish();
                        break;
                }
                return false;
            }
        });


    }
//    public void actualizar() {
//        // productList = new ArrayList<Proyector>();
//
//        lview = (ListView) findViewById(R.id.listview);
//        adapter = new listviewAdapter(this, productList);
//        lview.setAdapter(adapter);
//
//        adapter.notifyDataSetChanged();
//
//        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("TAG","ES 1");
//                Proyector temp = null;
//
//                sno = ((TextView)view.findViewById(R.id.sNo)).getText().toString();
//                product = ((TextView)view.findViewById(R.id.product)).getText().toString();
//                category = ((TextView)view.findViewById(R.id.category)).getText().toString();
//                price = ((TextView)view.findViewById(R.id.price)).getText().toString();
//                /*
//                Toast.makeText(getApplicationContext(),sno + "\n"
//                        + product + "\n"
//                        + category + "\n"
//                        + price,Toast.LENGTH_SHORT).show();
//                */
//                Log.d("TAG","ES 2");
//                for(int i=0; i < productList.size();i++){
//                    if (productList.get(i).getMarca() == sno) {
//                        temp = (Proyector)productList.get(i);
//                        u = temp.getId();
//                    }
//                }
//                //---IMAGEN
//                StorageReference filepath = mStorage
//                        .child("images")
//                        .child("Proyectores")
//                        .child(temp.getId())
//                        //.child("2eed46a8-31af-41d7-872d-cd69df0a5549")
//                        .child(temp.getProyector());
//                selected_url = temp.getProyector();
//
//                filepath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//
//                        downloadUrl = task.getResult();
//                        // downloadurl will be the resulted answer
//                    }
//                });
//                // IMAGEN
//                Log.d("TAG","ES 3");
//                if (temp != null) {
//                    img = (ImageView)findViewById(R.id.img_view);
//                    // img.setImageResource(temp.getProyector());
//                    // Toast.makeText(AltasActivity.this,"IMAGENA",Toast.LENGTH_SHORT).show();
//                    Glide.with(ConsultarProyector.this).load(downloadUrl)
//                            .fitCenter()
//                            .centerCrop()
//                            .into(img);
//                }
//                Log.d("TAG","ES 4");
//                Toast.makeText(getApplicationContext(), "Marca : " + sno +"\n"
//                        +"Modelo : " + product +"\n"
//                        +"Disponible : " +category +"\n"
//                        +"Maestro : " +price, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
    private void populateList() {

        databaseReference.child("Proyector").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // listPerson.clear();
                        productList.clear();
                        for(DataSnapshot objSnapShot :
                                dataSnapshot.getChildren()){
                            Proyector p = objSnapShot.getValue(Proyector.class);
                            productList.add(p);
                        }
                        adapter.notifyDataSetChanged();
                        // actualizar();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        /*
        Proyector item1, item2, item3, item4, item5, item6;

        item1 = new Proyector("1", "Epson", "111", "Si","A","Alfredo");
        productList.add(item1);

        item2 = new Proyector("2", "Acer", "222", "No","B","Gloria");
        productList.add(item2);

        item3 = new Proyector("3", "Viewsonic", "333", "No","C","Ricardo");
        productList.add(item3);

        item4 = new Proyector("4", "Optoma", "444", "No","D","Jose");
        productList.add(item4);

        item5 = new Proyector("5", "EVL", "555", "No");
        productList.add(item5);

        item6 = new Proyector("6", "Tarta", "Tarta", "Si");
        productList.add(item6);
        */
    }

}
