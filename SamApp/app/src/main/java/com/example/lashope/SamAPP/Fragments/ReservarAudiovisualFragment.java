package com.example.lashope.SamAPP.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lashope.SamAPP.DatosSolicitante;
import com.example.lashope.SamAPP.Models.Maestro;
import com.example.lashope.SamAPP.Models.Reserva;
import com.example.lashope.SamAPP.R;
import com.example.lashope.SamAPP.ReservarAudiovisual;
import com.example.lashope.SamAPP.ReservasActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReservarAudiovisualFragment extends Fragment {

    //<editor-fold desc="Variables">
    //Variable miembro
    //Declaracion de constante
    private static final String SEND_MESSAGE="message";
    private static final String EXTRA_NAME_SHOWN="extraName";
    private static  final int REQUEST_CODE_NAME=0;
    private static final String TAG="etiqueta";
    private final Reserva[] reservas = null;
    private List<Reserva> listReserva = new ArrayList<Reserva>();
    private int cont = 0;
    private Reserva p;
    private boolean band = false;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private int dia,mes,ano,hora,min;
    private String none = "--------------------------";
    private BottomNavigationView bottomNavigationView;
    String[] salas = { "--------------------------",
            "Aula Magna",
            "Audiovisual No.1",
            "Audiovisual No.2",
            "Audiovisual No.3" };
    String[] hi = { "--------------------------",
            "07:00-08:00",
            "08:00-09:00",
            "09:00-10:00",
            "10:00-11:00",
            "11:00-12:00",
            "12:00-13:00",
            "13:00-14:00",
            "14:00-15:00",
            "15:00-16:00",
            "16:00-17:00",
            "17:00.18:00",
            "18:00-19:00",
            "19:00-20:00",
            "07:00-09:00",
            "08:00-10:00",
            "09:00-11:00",
            "10:00-12:00",
            "11:00-13:00",
            "12:00-14:00",
            "13:00-15:00",
            "14:00-16:00",
            "16:00-18:00",
            "18:00-20:00"};
    // String[] hf = { "12:00","1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00" ,"10:00","11:00"};
    String[] actividad = { "--------------------------",
            "Capacitación",
            "Clase ordinaria",
            "Conferencia",
            "Curso",
            "Titulación",
            "Pláticas",
            "Evento especial" };
    private Menu menu;
    private MenuItem menuItem;
    private Spinner mSalas;
    private Spinner mHi;
    // private Spinner mHf;
    private Spinner mActividad;
    private Button btn_fecha,btn_hora,btn_fhora;
    private EditText edit_fecha,edit_hora,edit_fhora,edit_comm;
    private Button mSolicitar,mReservas;
    //</editor-fold>


    public ReservarAudiovisualFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View views = inflater.inflate(R.layout.fragment_reservar_audiovisual, container, false);

        //Inicializar firebase
        initializeFirebase();
        listData("names");

        mSalas = (Spinner)views.findViewById(R.id.sp1);
        mHi = (Spinner)views.findViewById(R.id.sp2);
        // mHf = (Spinner)findViewById(R.id.sp3);
        mActividad = (Spinner)views.findViewById(R.id.sp4);
        mSolicitar = (Button)views.findViewById(R.id.solicitar_button);

        // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.salas,android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,salas);
        mSalas.setAdapter(adapter);

        // ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.hi,android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, hi);
        mHi.setAdapter(adapter2);

        // ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.hf,android.R.layout.simple_spinner_item);
        // ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,hf);
        // mHf.setAdapter(adapter3);

        // ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,R.array.actividad,android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,actividad);
        mActividad.setAdapter(adapter4);

        btn_fecha = (Button)views.findViewById(R.id.btn_fecha);
        mReservas = (Button)views.findViewById(R.id.reservas_button);
        // btn_fhora = (Button)findViewById(R.id.btn_horaF);
        // btn_hora = (Button)findViewById(R.id.btn_hora);
        edit_fecha = (EditText) views.findViewById(R.id.text_fecha);
        // edit_hora = (EditText) findViewById(R.id.text_hora);
        // edit_fhora = (EditText) findViewById(R.id.text_horaF);
        edit_comm = (EditText) views.findViewById(R.id.edit_comm);

        btn_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                ano = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edit_fecha.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+ year);
                    }
                }
                        ,ano ,mes , dia);
                datePickerDialog.show();
            }
        });
        mReservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ReservasActivity.class));
            }
        });

        mSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // band = false;

                // Toast.makeText(ReservarAudiovisual.this, edit_fecha.getText(), Toast.LENGTH_SHORT).show();
                String f =  edit_fecha.getText().toString();
                if(mSalas.getSelectedItem().toString() == none ||
                        mHi.getSelectedItem().toString() == none ||
                        mActividad.getSelectedItem().toString() == none || f.equals(""))
                {
                    validation();

                }
                else {
                    for(int i = 0; i < cont; i++)
                    {
                        // Toast.makeText(ReservarAudiovisual.this,listReserva.get(i).toString(),Toast.LENGTH_SHORT).show();
                        if(listReserva.get(i).getHora().equals(mHi.getSelectedItem().toString()) && listReserva.get(i).getFecha().equals(f)
                                && listReserva.get(i).getAudiovisual().equals(mSalas.getSelectedItem().toString()))
                        {
                            Toast.makeText(getContext(),"ESTA FECHA Y HORA NO ESTÁ DISPONIBLE",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    p = new Reserva();
                    p.setUid(UUID.randomUUID().toString());
                    p.setAudiovisual(mSalas.getSelectedItem().toString());
                    p.setHora(mHi.getSelectedItem().toString());
                    // p.setMaestro(new Maestro(1,"example","use.example.com",66421313));
                    p.setFecha(edit_fecha.getText().toString());
                    p.setComment(edit_comm.getText().toString());
                    p.setTema(mActividad.getSelectedItem().toString());
                    p.setMaestro(null);

                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                    dlgAlert.setMessage("Sala: " + p.getAudiovisual() + "\n" +
                            "Hora: " + p.getHora() + "\n" +
                            "Fecha: " + p.getFecha());
                    dlgAlert.setTitle("Confirmación");
                    dlgAlert.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                            Reserva message = p;
                            Intent i = DatosSolicitante.newIntent(getContext(), message);
                            startActivityForResult(i, 0);
                        }
                    });
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            }
        });



        return views;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK) {
            Toast.makeText(getContext(),"Reservación Cancelada",Toast.LENGTH_LONG).show();
            return;
        }
        if(requestCode == REQUEST_CODE_NAME){
            if (data == null) {
                return;
            }

            initializeFirebase();
            Bundle extras = DatosSolicitante.wasNameShown(data);
            Maestro m = new Maestro();
            try {
                m.setUid(extras.getString("m_id"));
                m.setNombre(extras.getString("m_nombre"));
                m.setCorreo(extras.getString("m_correo"));
                m.setTelefono(Long.parseLong(extras.getString("m_telefono")));
                // m.setTelefono(Integer.parseInt(extras.getString("m_telefono")));
                p.setMaestro(m);
            }
            catch (Exception e) {
                Toast.makeText(getContext(),"Datos del solicitante inválidos\nIntentelo de nuevo",Toast.LENGTH_LONG).show();
                return;
            }
            // Toast.makeText(ReservarAudiovisual.this, m.toString(), Toast.LENGTH_SHORT).show();

            databaseReference.child("Reserva").child(p.getUid()).setValue(p);
            Toast.makeText(getContext(), "Reservación Realizada", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG,"onActivityResult() llamado");
    }

    private void initializeFirebase(){
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    private void listData(final String selected){
        databaseReference.child("Reserva").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // reserva = new Reserva[dataSnapshot.getChildren().Length]
                        // Toast.makeText(ReservarAudiovisual.this, Integer.toString(cont), Toast.LENGTH_SHORT).show();
                        for(DataSnapshot objSnapShot :
                                dataSnapshot.getChildren()){


                            Reserva p = objSnapShot.getValue(Reserva.class);
                            listReserva.add(p);
                            cont = cont + 1;
                            // reservas
                        }
                        // Toast.makeText(ReservarAudiovisual.this, Integer.toString(cont), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void validation(){
        String nombre = mSalas.getSelectedItem().toString();
        String apellido = mHi.getSelectedItem().toString();
        String correo = edit_fecha.getText().toString();
        String password = mActividad.getSelectedItem().toString();
        if(nombre.equals(none)){
            TextView errorText = (TextView)mSalas.getSelectedView();
            errorText.setError("anything here, just to add the icon");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Requerido");
            // mSalas.setError("Requerido");
        }else if(apellido.equals(none)){
            TextView errorText = (TextView)mHi.getSelectedView();
            errorText.setError("anything here, just to add the icon");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Requerido");
            // edAp.setError("Requerido");
        }else if (correo.equals("")){
            edit_fecha.setError("Requerido");
        } else if (password.equals(none)){
            TextView errorText = (TextView)mActividad.getSelectedView();
            errorText.setError("anything here, just to add the icon");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Requerido");
            // edPas.setError("Requerido");
        }
    }

}
