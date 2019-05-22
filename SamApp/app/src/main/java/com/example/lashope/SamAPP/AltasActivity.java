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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AltasActivity extends AppCompatActivity {

    private ArrayList<Proyector> productList;
    private ImageView img;
    private BottomNavigationView bottomNavigationView;
    private Menu menu;
    private MenuItem menuItem;
    private StorageReference mStorage;
    private Uri downloadUrl;
    private String selected_url;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    listviewAdapter adapter;
    String sno, product,category, price,u;
    ListView lview;
    // Crear constante tag
    private final static String TAG ="etiqueta";
    private final static String KEY_INDEX="indice";
    private static final String SEND_MESSAGE="message";
    private static final String EXTRA_NAME_SHOWN="extraName";

    //Asignacion del valor de la variable mensage a la constante
    //SEND_MESSAGE
    public static Intent newIntent(Context packageContext, String message){
        Intent i= new Intent(packageContext,AltasActivity.class);
        i.putExtra(SEND_MESSAGE,message);
        return i;
    }
    //Metodo que se manda llamar desde la MainACTIVITY PARA OBTENER EL VALOR DE LA VARIABLE NAME Que se arguardp en la ocnstante
    public static String wasNameShown(Intent result){
        return result.getStringExtra(EXTRA_NAME_SHOWN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altas);
        initializeFirebase();
        mStorage = FirebaseStorage.getInstance().getReference();

        productList = new ArrayList<Proyector>();

        lview = (ListView) findViewById(R.id.listview);
        adapter = new listviewAdapter(this, productList);
        lview.setAdapter(adapter);


        populateList();



        adapter.notifyDataSetChanged();

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
                        Glide.with(AltasActivity.this).load(downloadUrl)
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

        // Navigation Bar
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_bar);
        menu = bottomNavigationView.getMenu();
        menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.audio:
                        startActivity(new Intent(AltasActivity.this,AdminAccount.class));
                        finish();
                        break;
                    case R.id.proyector:
                        break;
                    case R.id.admin_logout:
                        SignOut();
                        finish();
                        break;
                }
                return false;
            }
        });
        // Toast.makeText(AltasActivity.this, "OnCreate", Toast.LENGTH_SHORT).show();
    }
    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
    }
    private void initializeFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
    private void populateList() {

        /*

        Proyector item1, item2, item3, item4, item5, item6;

        item1 = new Proyector("1", "Epson", "111", "Si",R.drawable.proyector,"Alfredo");
        productList.add(item1);

        item2 = new Proyector("2", "Acer", "222", "No",R.drawable.acer,"Gloria");
        productList.add(item2);

        item3 = new Proyector("3", "Viewsonic", "333", "No",R.drawable.viewsonic,"Ricardo");
        productList.add(item3);

        item4 = new Proyector("4", "Optoma", "444", "No",R.drawable.optoma,"Jose");
        productList.add(item4);
        */
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
                            adapter.notifyDataSetChanged();
                            // Toast.makeText(AltasActivity.this, p.toString(), Toast.LENGTH_SHORT).show();

                            /*
                            ArrayList<Proyector> listPerson = adapter;
                            adapter =
                                    new ArrayAdapter<Proyector>
                                            (AltasActivity.this,
                                                    android.R.layout.simple_list_item_1,
                                                    listPerson);
                            lview.setAdapter(adapter);
                            */
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        /*
        item5 = new Proyector("5", "EVL", "555", "No");
        productList.add(item5);

        item6 = new Proyector("6", "Tarta", "Tarta", "Si");
        productList.add(item6);
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Toast.makeText(AltasActivity.this, "OnStart", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Toast.makeText(AltasActivity.this, "OnStart", Toast.LENGTH_SHORT).show();
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.altas_bajas,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.alta:
                Proyector p = new Proyector();
                p.setId(u);
                p.setMarca(sno.trim());
                p.setModelo(product.trim());
                p.setDisponible("Disponible");
                p.setMaestro(price.trim());
                p.setProyector(selected_url);
                databaseReference.child("Proyector").child(p.getId()).setValue(p);
                actualizar();
                break;
            case R.id.baja:

                Proyector po = new Proyector();
                po.setId(u);
                po.setMarca(sno.trim());
                po.setModelo(product.trim());
                po.setDisponible("Ocupado");
                po.setMaestro(price.trim());
                po.setProyector(selected_url);
                databaseReference.child("Proyector").child(po.getId()).setValue(po);
                actualizar();
                break;
            case R.id.refresh:
                actualizar();
                break;
            default:
                break;
        }
        return true;
        // return super.onOptionsItemSelected(item);
    }
    */
    public void actualizar() {
        // productList = new ArrayList<Proyector>();

        lview = (ListView) findViewById(R.id.listview);
        adapter = new listviewAdapter(this, productList);
        lview.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG","ES 1");
                Proyector temp = null;

                sno = ((TextView)view.findViewById(R.id.sNo)).getText().toString();
                product = ((TextView)view.findViewById(R.id.product)).getText().toString();
                category = ((TextView)view.findViewById(R.id.category)).getText().toString();
                price = ((TextView)view.findViewById(R.id.price)).getText().toString();
                /*
                Toast.makeText(getApplicationContext(),sno + "\n"
                        + product + "\n"
                        + category + "\n"
                        + price,Toast.LENGTH_SHORT).show();
                */
                Log.d("TAG","ES 2");
                for(int i=0; i < productList.size();i++){
                    if (productList.get(i).getMarca() == sno) {
                        temp = (Proyector)productList.get(i);
                        u = temp.getId();
                    }
                }
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
                        // downloadurl will be the resulted answer
                        img = (ImageView)findViewById(R.id.img_view);
                        // img.setImageResource(temp.getProyector());
                        // Toast.makeText(AltasActivity.this,"IMAGENA",Toast.LENGTH_SHORT).show();
                        Glide.with(AltasActivity.this).load(downloadUrl)
                                .fitCenter()
                                .centerCrop()
                                .into(img);
                    }
                });
                // IMAGEN
                Log.d("TAG","ES 3");
                /*
                if (temp != null) {
                    img = (ImageView)findViewById(R.id.img_view);
                    // img.setImageResource(temp.getProyector());
                    // Toast.makeText(AltasActivity.this,"IMAGENA",Toast.LENGTH_SHORT).show();
                    Glide.with(AltasActivity.this).load(downloadUrl)
                            .fitCenter()
                            .centerCrop()
                            .into(img);
                }
                */
                Log.d("TAG","ES 4");
                Toast.makeText(getApplicationContext(), "Marca : " + sno +"\n"
                        +"Modelo : " + product +"\n"
                        +"Disponible : " +category +"\n"
                        +"Maestro : " +price, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
