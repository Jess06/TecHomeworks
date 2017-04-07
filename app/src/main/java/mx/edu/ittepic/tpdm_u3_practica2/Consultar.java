package mx.edu.ittepic.tpdm_u3_practica2;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Consultar extends Fragment {
    public static String title="";
    ListView lista;
    EditText filtro;
    SimpleDateFormat formatoFecha;
    Calendar c;
    KeyListener keyListener;
    ListAdapter adapter;
    public static Spinner spiner;

    String [] ids;
    String [] titulos;
    String [] estatus;
    String [] fecha;
    String [] materias;
    Integer [] icono;



    public Consultar(){
        formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        c = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar, container, false);
        lista = (ListView) view.findViewById(R.id.lista_filtro);

        spiner=(Spinner)view.findViewById(R.id.spinner);

        filtro = (EditText) view.findViewById(R.id.editText5);

        keyListener = filtro.getKeyListener();
        adapter=null;

        lista.setAdapter(adapter);

        spiner.setSelection(0);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        filtro.setVisibility(View.INVISIBLE);
                        fillList(0);
                        lista.setAdapter(adapter);
                        break;
                    case 1:
                        filtro.setVisibility(View.VISIBLE);
                        filtro.setKeyListener(keyListener);
                        filtro.setText("");
                        filtro.setHint("Buscar por Nombre");
                        filtro.setOnClickListener(null);
                        filtro.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                fillList(1);
                                lista.setAdapter(adapter);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        break;
                    case 2:
                        filtro.setKeyListener(null);
                        filtro.setVisibility(View.VISIBLE);
                        filtro.setText("");filtro.setHint("YYY-MM-DD");
                        filtro.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateDate();
                            }
                        });
                        break;
                    case 3:
                        filtro.setVisibility(View.VISIBLE);
                        filtro.setKeyListener(keyListener);
                        filtro.setText("");
                        filtro.setHint("Buscar por Materia");
                        filtro.setOnClickListener(null);
                        filtro.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                fillList(3);
                                lista.setAdapter(adapter);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                        break;
                    case 4:
                        filtro.setVisibility(View.INVISIBLE);
                        fillList(4);
                        lista.setAdapter(adapter);
                        break;
                    case 5:
                        filtro.setVisibility(View.INVISIBLE);
                        fillList(5);
                        lista.setAdapter(adapter);
                        break;
                    case  6:
                        filtro.setVisibility(View.INVISIBLE);
                        fillList(6);
                        lista.setAdapter(adapter);
                        break;
                }
                if(filtro.getText().toString().isEmpty()){
                    fillList(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //selectTask("2017-04-07");
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //sendData(position);
                Tareas.id=Integer.parseInt(ids[position]);
                title=titulos[position];

                showTask();
            }
        });
        return view;
    }


    private void fillList(int caso){
        adapter=null;
        if(searchTask(filtro,caso)!=null){
            ids=searchTask(filtro,caso).get(0);
            titulos=searchTask(filtro,caso).get(1);
            estatus=searchTask(filtro,caso).get(2);
            fecha=searchTask(filtro,caso).get(3);
            materias=searchTask(filtro,caso).get(4);
            icono= new Integer[estatus.length];
            Date date1=null,date2=null;
            for(int s= 0; s<estatus.length;s++){

                Calendar d;
                d=Calendar.getInstance();
                try {
                    date1 = formatoFecha.parse(formatoFecha.format(d.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    date2 = formatoFecha.parse(fecha[s]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if((date1.before(date2) || date1.equals(date2)) && Integer.parseInt(estatus[s])==0){
                    icono[s]=R.drawable.agendada;
                }else if(Integer.parseInt(estatus[s])==1){
                    icono[s]=R.drawable.terminado;
                }else if(date1.after(date2) && Integer.parseInt(estatus[s])==0){
                    icono[s]=R.drawable.cancelar;
                }
            }
            adapter= new ListAdapter((Activity)getContext(),titulos,fecha,icono,materias);
        }
        else{
            Toast.makeText(getContext(),"No tienes tareas ;)",Toast.LENGTH_LONG).show();
        }
    }

    private void showTask() {
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new VerTarea(), "Ver tarea");
        Tareas.toolbartitle=Tareas.toolbar.getTitle().toString();
        fragmentTransaction.addToBackStack("Ver tarea");
        fragmentTransaction.commit();
    }


    private ArrayList<String []> searchTask(EditText f,int caso){
        ArrayList<String[]> array = new ArrayList<String[]>();
        String id[]=null;String []t=null;String [] est= null;String fe[]=null;String ma []=null;
        try{
            SQLiteDatabase db= Tareas.conexion.getReadableDatabase();
            String sql="";
            Cursor result;

            switch (caso){
                case 0:
                    sql="SELECT ID_TAREA,TITULO,ESTATUS,FECH_ENTREGA, MATERIA FROM TAREA ORDER BY DATE(FECH_ENTREGA) ASC";
                    break;
                case 1:
                    sql="SELECT ID_TAREA,TITULO,ESTATUS,FECH_ENTREGA, MATERIA FROM TAREA WHERE TITULO LIKE '%"+f.getText().toString()+"%' ORDER BY DATE(FECH_ENTREGA) ASC";
                    break;
                case 2:
                    sql="SELECT ID_TAREA,TITULO, ESTATUS,FECH_ENTREGA, MATERIA FROM TAREA WHERE date(FECH_ENTREGA) = date('"+f.getText().toString()+"')";
                    break;
                case 3:
                    sql="SELECT ID_TAREA,TITULO,ESTATUS,FECH_ENTREGA, MATERIA FROM TAREA WHERE MATERIA LIKE '%"+f.getText().toString()+"%' ORDER BY DATE(FECH_ENTREGA) ASC";
                    break;
                case 4:
                    sql="SELECT ID_TAREA,TITULO,ESTATUS,FECH_ENTREGA, MATERIA FROM TAREA WHERE ESTATUS=1 ORDER BY DATE(FECH_ENTREGA) ASC";
                    break;
                case 5:
                    sql="SELECT ID_TAREA,TITULO,ESTATUS,FECH_ENTREGA, MATERIA FROM TAREA WHERE ESTATUS=0 AND DATE(FECH_ENTREGA) < DATE('NOW') ORDER BY DATE(FECH_ENTREGA) ASC";
                    break;
                case 6:
                    sql="SELECT ID_TAREA,TITULO,ESTATUS,FECH_ENTREGA, MATERIA FROM TAREA WHERE ESTATUS=0 AND DATE(FECH_ENTREGA) >= DATE('NOW') ORDER BY DATE(FECH_ENTREGA) ASC";
                    break;
            }
            result=db.rawQuery(sql,null);
            if(result.getCount()<= 0){
                Log.e("Error","El cursor es cero");
                return null;
            }
            int tam= result.getCount();
            id=new String[tam];
            t= new String[tam];
            est= new String[tam];
            fe= new String[tam];
            ma=new String[tam];

             int r=0;
            while(result.moveToNext()){
                id[r]=result.getString(0);
                t[r]=result.getString(1);
                est[r]=result.getString(2);
                fe[r]=result.getString(3);
                ma[r]=result.getString(4);
                r++;
            }
            db.close();
        }catch (SQLException e){
            Log.e("Error",e.getMessage());
        }
        array.add(0,id);
        array.add(1,t);
        array.add(2,est);
        array.add(3,fe);
        array.add(4,ma);
        return array;
    }
    DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            filtro.setText(formatoFecha.format(c.getTime()));
            fillList(2);
            lista.setAdapter(adapter);
        }
    };

    private void updateDate(){
        new DatePickerDialog(getContext(), datePickerDialog,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

}
