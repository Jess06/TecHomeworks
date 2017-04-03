package mx.edu.ittepic.tpdm_u3_practica2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class NuevaTarea extends Fragment {

    SimpleDateFormat formatoFecha;
    Calendar c;
    EditText fecha, titulo, descripcion, notas;
    Button aceptar, cancelar;

    public NuevaTarea(){
        formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        c = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nueva_tarea, container, false);
        titulo = (EditText) view.findViewById(R.id.editText);
        descripcion = (EditText) view.findViewById(R.id.editText2);
        notas = (EditText) view.findViewById(R.id.editText4);
        cancelar = (Button) view.findViewById(R.id.button);
        aceptar = (Button) view.findViewById(R.id.button2);
        fecha=(EditText) view.findViewById(R.id.editText3);
        fecha.setKeyListener(null);
        fecha.setText(formatoFecha.format(c.getTime()));
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            fecha.setText(formatoFecha.format(c.getTime()));
        }
    };

    private void updateDate(){
        new DatePickerDialog(getContext(), datePickerDialog,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

}
