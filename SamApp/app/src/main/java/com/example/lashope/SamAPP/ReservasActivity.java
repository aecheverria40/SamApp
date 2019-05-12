package com.example.lashope.SamAPP;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lashope.SamAPP.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReservasActivity extends AppCompatActivity {

    private ArrayList<Reserva> productList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public listviewAdapterReserva adapter;
    Date fecha_actual = null;
    Date fecha_obj= null;
    DateFormat formatter = null;

    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    // var date = Date();
    ///val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
    // SimpleDateFormat formatter = new SimpleDateFormat("d/MM/yyyy");
    String sno, product,category, price,u;
    ListView lview;
    // Crear constante tag
    private final static String TAG ="etiqueta";
    private final static String KEY_INDEX="indice";
    private static final String SEND_MESSAGE="message";
    private static final String EXTRA_NAME_SHOWN="extraName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);
        initializeFirebase();
        //quitar ActionBar
        getSupportActionBar().hide();

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        try {
            fecha_actual = formatter.parse(formatter.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Date date = new Date();
        /*
        DateFormat formatter = null;
        Date convertedDate = null;
        Date temp = null;

        String stringDateFormat = "14/09/2011";
        String datestring = "13/09/2011";
        formatter =new SimpleDateFormat("dd/MM/yyyy");
        try {
            convertedDate =(Date) formatter.parse(stringDateFormat);
            temp = (Date) formatter.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int i = convertedDate.compareTo(temp);
        Toast.makeText(getApplicationContext(),Integer.toString(i),Toast.LENGTH_SHORT).show();
        */


        productList = new ArrayList<Reserva>();
        lview = (ListView) findViewById(R.id.listview);
        adapter = new listviewAdapterReserva( productList,this);
        lview.setAdapter(adapter);
        populateList();
        adapter.notifyDataSetChanged();
    }
    private void initializeFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
    private void populateList() {

        databaseReference.child("Reserva").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // listPerson.clear();
                        productList.clear();
                        for(DataSnapshot objSnapShot :
                                dataSnapshot.getChildren()){
                            Reserva r = objSnapShot.getValue(Reserva.class);
                            try {
                                fecha_obj = (Date) formatter.parse(r.getFecha());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (fecha_actual.compareTo(fecha_obj) == 0) {
                                productList.add(r);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

    }
}
