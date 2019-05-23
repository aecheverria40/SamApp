package com.example.lashope.SamAPP.Fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.example.lashope.SamAPP.Models.Reserva;
import com.example.lashope.SamAPP.R;
import com.example.lashope.SamAPP.ReservarAudiovisual;
import com.example.lashope.SamAPP.listviewAdapterReserva;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */public class ReservasAudiovisualFragment extends Fragment {

    //<editor-fold desc="Variables">
    private ArrayList<Reserva> productList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public listviewAdapterReserva adapter;
    Date fecha_actual = null, fecha_des = null, fecha_hasta = null;

    //Para formatear la fecha
    SimpleDateFormat parseDate = new SimpleDateFormat("dd/MM/yyyy");

    Date fecha_obj= null;
    DateFormat formatter = null;
    Button btn_query ,btn_deletequery;
    EditText picker_desde, picker_hasta;
    int dia,mes,ano;

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
        final View view= inflater.inflate(R.layout.fragment_reservas_audiovisual, container, false);

        //Inicialzo los objetos Date y Firebase
        fecha_des = new Date();
        fecha_hasta = new Date();
        initializeFirebase();

        //Inflo los componentes
        btn_query=view.findViewById(R.id.btn_query);
        btn_deletequery=view.findViewById(R.id.btn_deletequery);
        picker_desde=view.findViewById(R.id.picker_desde);
        picker_desde.setKeyListener(null);
        picker_hasta=view.findViewById(R.id.picker_hasta);
        picker_hasta.setKeyListener(null);

        //Este a estaba
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        try {
            fecha_actual = formatter.parse(formatter.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Aqui se guardaran los resultados obtenidos
        productList = new ArrayList<Reserva>();

        //Inflo el listview donde se desplegaran los registros
        lview = (ListView) view.findViewById(R.id.listview);

        adapter = new listviewAdapterReserva( productList,getActivity());
        lview.setAdapter(adapter);
        populateList();

        adapter.notifyDataSetChanged();

        //Desplegar el calendario cuando es picado el EditText
        picker_desde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                dia=calendar.get(Calendar.DAY_OF_MONTH);
                mes=calendar.get(Calendar.MONTH);
                ano=calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        picker_desde.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+ year);
                    }
                }
                        ,ano ,mes , dia);
                datePickerDialog.show();

                picker_hasta.requestFocus();
            }
        });

        //Parseo y guardo la fecha
        try {
            fecha_des = parseDate.parse(picker_desde.getText().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //Desplegar el calendario cuando es picado el EditText
        picker_hasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                dia=calendar.get(Calendar.DAY_OF_MONTH);
                mes=calendar.get(Calendar.MONTH);
                ano=calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        picker_hasta.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+ year);
                    }
                }
                        ,ano ,mes , dia);
                datePickerDialog.show();

                //Parseo de la fecha
                try {
                    fecha_hasta = parseDate.parse(picker_hasta.getText().toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateListQuery(fecha_des, fecha_hasta);
            }
        });

        return view;
    }

    //Inicializador de Firebase
    private void initializeFirebase(){
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //Se guardan los registros en la lista
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

    //Metodo para consultar de fecha tal hasta fecha tal
    private void populateListQuery(final Date fecha_des_value, final Date fecha_hasta_value){
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
                            if (fecha_obj.after(fecha_des_value) && fecha_obj.before(fecha_hasta_value)) {
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
