package com.example.lashope.SamAPP;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lashope.SamAPP.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUno extends Fragment {


    public FragmentUno() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // edMar = (EditText)getView().findViewById(R.id.txt_marca);
        // edMod = (EditText)getView().findViewById(R.id.txt_modelo);
        // edDis = (EditText)getView().findViewById(R.id.txt_disponible);
        // edMas = (EditText)getView().findViewById(R.id.txt_maestro);

        // listV_proyectores = getView().findViewById(R.id.lv_datosProyectores);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_uno, container, false);
    }
}
