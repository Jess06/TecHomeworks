package mx.edu.ittepic.tpdm_u3_practica2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class VerTarea extends Fragment {

    EditText titulo, descripcion, notas, fecha;
    Button aceptar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_tarea, container, false);;
        titulo = (EditText) view.findViewById(R.id.editText9);
        descripcion = (EditText) view.findViewById(R.id.editText10);
        notas = (EditText) view.findViewById(R.id.editText12);
        aceptar = (Button) view.findViewById(R.id.button5);
        fecha=(EditText) view.findViewById(R.id.editText11);

        titulo.setKeyListener(null);
        descripcion.setKeyListener(null);
        fecha.setKeyListener(null);
        notas.setKeyListener(null);

        Tareas.toolbar.setTitle(ListaTareas.data[0]);
        fecha.setText(ListaTareas.data[1]);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

}
