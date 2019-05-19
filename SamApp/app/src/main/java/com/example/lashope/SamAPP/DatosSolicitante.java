package com.example.lashope.SamAPP;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.lashope.SamAPP.Models.Reserva;

import java.util.UUID;

public class DatosSolicitante extends AppCompatActivity {
    String[] carreras= { "DEPARTAMENTO DE METAL MECÁNICA",
            "DEPARTAMENTO SISTEMAS Y COMPUTACIÓN",
            "DEPARTAMENTO DE DESARROLLO ACADÉMICO",
            "DIVISIÓN DE ESTUDIOS PROFESIONALES",
            "DIVISIÓN DE ESTUDIOS DE POSGRADO",
            "DEPARTAMENTO DE INGENIERÍA INDUSTRIAL",
            "DEPARTAMENTO DE ORIENTACIÓN EDUCATIVA",
            "DEPARTAMENTO DE CIENCIAS BÁSICAS",
            "DEPARTAMENTO DE BIOQUÍMICA",
            "DEPARTAMENTO DE CIENCIAS DE LA TIERRA",
            "DEPARTAMENTO DE ELÉCTRICA Y ELECTRÓNICA",
            "DEPARTAMENTO DE CIENCIAS ECONÓMICO ADMINISTRATIVAS"};
    private Spinner mCarreras;
    private Button mDSButton;
    private Button mCancelar;
    private EditText mEdit_corr,mEdit_tel,mEdit_nom;
    // Crear constante tag
    private final static String TAG ="etiqueta";
    private final static String KEY_INDEX="indice";
    private static final String SEND_MESSAGE="message";
    private static final String EXTRA_NAME_SHOWN="extraName";
    //Asignacion del valor de la variable mensage a la constante
    //SEND_MESSAGE

    public static Intent newIntent(Context packageContext, Reserva message){
        Intent i= new Intent(packageContext,DatosSolicitante.class);
        i.putExtra(SEND_MESSAGE,message);
        return i;
    }

    private void sendBackName(Bundle name){
        Intent data = new Intent();
        // Se asigna el valor de la variable nombre a la constante
        data.putExtras(name);
        setResult(RESULT_OK,data);
    }
    private void sendCancel(){
        Intent data = new Intent();
        // Se asigna el valor de la variable nombre a la constante
        setResult(RESULT_CANCELED,data);
    }


    //Metodo que se manda llamar desde la MainACTIVITY PARA OBTENER EL VALOR DE LA VARIABLE NAME Que se arguardp en la ocnstante
    public static Bundle wasNameShown(Intent result){
        return result.getExtras();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_solicitante);
        //quitar ActionBar
        getSupportActionBar().hide();

        // mCarreras = (Spinner)findViewById(R.id.Carrera);
        mDSButton=(Button)findViewById(R.id.btns_reservar);
        mCancelar = (Button)findViewById(R.id.btns_cancelar);

        mEdit_nom = (EditText)findViewById(R.id.edit_nom);
        mEdit_corr = (EditText)findViewById(R.id.edit_corr);
        mEdit_tel = (EditText)findViewById(R.id.edit_tel);

        mDSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Maestro m = new Maestro();
                // m.setUid(UUID.randomUUID().toString());
                // m.setNombre(mEdit_nom.getText().toString());
                // m.setCorreo(mEdit_corr.getText().toString());
                // m.setTelefono(Integer.parseInt(mEdit_tel.getText().toString()));
                Bundle extras = new Bundle();
                extras.putString("m_id",UUID.randomUUID().toString());
                extras.putString("m_nombre",mEdit_nom.getText().toString());
                extras.putString("m_correo",mEdit_corr.getText().toString());
                extras.putString("m_telefono",mEdit_tel.getText().toString());



                // Reserva r = (Reserva) getIntent().getSerializableExtra(SEND_MESSAGE);
                // r.setMaestro(m);
                sendBackName(extras);
                finish();
            }
        });
        mCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCancel();
                finish();
            }
        });




    }
}
