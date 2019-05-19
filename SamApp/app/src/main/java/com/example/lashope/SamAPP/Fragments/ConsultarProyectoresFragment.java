package com.example.lashope.SamAPP.Fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lashope.SamAPP.ConsultarProyector;
import com.example.lashope.SamAPP.Models.Proyector;
import com.example.lashope.SamAPP.R;
import com.example.lashope.SamAPP.listviewAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultarProyectoresFragment extends Fragment {
    //<editor-fold desc="Variables">
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
    //</editor-fold>

    //Asignacion del valor de la variable mensage a la constante
    //SEND_MESSAGE
    public static Intent newIntent(Context packageContext, String message){
        Intent i= new Intent(packageContext, ConsultarProyector.class);
        i.putExtra(SEND_MESSAGE,message);
        return i;
    }
    private void initializeFirebase(){
        //Cambie this a getContext()
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }


    public ConsultarProyectoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View views = inflater.inflate(R.layout.fragment_consultar_proyectores, container, false);

        //Inicializador de BD
        initializeFirebase();
        mStorage = FirebaseStorage.getInstance().getReference();

        //Quien sabe no comentaron
        productList = new ArrayList<Proyector>();
        lview = (ListView) views.findViewById(R.id.listview);
        //Cambie this por getActivity
        adapter = new listviewAdapter(getActivity(), productList);
        lview.setAdapter(adapter);

        populateList();

        adapter.notifyDataSetChanged();

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Log.d("TAG","ES 1");
                Proyector temp = null;

                sno = ((TextView)views.findViewById(R.id.sNo)).getText().toString();
                product = ((TextView)views.findViewById(R.id.product)).getText().toString();
                category = ((TextView)views.findViewById(R.id.category)).getText().toString();
                price = ((TextView)views.findViewById(R.id.price)).getText().toString();



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
                        img = (ImageView)views.findViewById(R.id.img_view);
                        // img.setImageResource(temp.getProyector());
                        // Toast.makeText(AltasActivity.this,"IMAGENA",Toast.LENGTH_SHORT).show();
                        Glide.with(ConsultarProyectoresFragment.this).load(downloadUrl)
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

                Log.d("TAG","ES 4");
            }
        });

        return views;
    }


    //Para listar los proyectores
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
    }

}
