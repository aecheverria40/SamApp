package com.example.lashope.SamAPP.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lashope.SamAPP.Models.Reserva;
import com.example.lashope.SamAPP.R;
import com.example.lashope.SamAPP.listviewAdapterReserva;
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

/**
 * A simple {@link Fragment} subclass.
 */public class ReservasAudiovisualFragment extends Fragment {

    //<editor-fold desc="Variables">
    private ArrayList<Reserva> productList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public listviewAdapterReserva adapter;
    Date fecha_actual = null;
    Date fecha_obj= null;
    DateFormat formatter = null;

    String sno, product,category, price,u;
    ListView lview;
    // Crear constante tag
    private final static String TAG ="etiqueta";
    private final static String KEY_INDEX="indice";
    private static final String SEND_MESSAGE="message";
    private static final String EXTRA_NAME_SHOWN="extraName";
    //</editor-fold>


    public ReservasAudiovisualFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reservas_audiovisual, container, false);
        initializeFirebase();
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        try {
            fecha_actual = formatter.parse(formatter.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        productList = new ArrayList<Reserva>();
        lview = (ListView) view.findViewById(R.id.listview);
        adapter = new listviewAdapterReserva( productList,getActivity());
        lview.setAdapter(adapter);
        populateList();
        adapter.notifyDataSetChanged();

        return view;
    }

    private void initializeFirebase(){
        FirebaseApp.initializeApp(getContext());
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
