package mx.edu.ittepic.tpdm_u3_practica2;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Consultar extends Fragment {

    RadioGroup group;
    ListView lista;
    EditText filtro;
    SimpleDateFormat formatoFecha;
    Calendar c;
    KeyListener keyListener;

    public Consultar(){
        formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        c = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar, container, false);

        group = (RadioGroup) view.findViewById(R.id.radioGroup);
        lista = (ListView) view.findViewById(R.id.lista_filtro);
        filtro = (EditText) view.findViewById(R.id.editText5);
        keyListener = filtro.getKeyListener();

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton:
                        filtro.setKeyListener(keyListener);
                        filtro.setText("");
                        filtro.setOnClickListener(null);
                        break;
                    case R.id.radioButton2:
                        filtro.setKeyListener(null);
                        filtro.setText(formatoFecha.format(c.getTime()));
                        filtro.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateDate();
                            }
                        });
                        break;
                }
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

            filtro.setText(formatoFecha.format(c.getTime()));
        }
    };

    private void updateDate(){
        new DatePickerDialog(getContext(), datePickerDialog,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
}
