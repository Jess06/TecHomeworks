package mx.edu.ittepic.tpdm_u3_practica2;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Consultar extends Fragment {

    RadioGroup group;
    ListView lista;
    EditText filtro;
    SimpleDateFormat formatoFecha;
    Calendar c;
    KeyListener keyListener;
    ListAdapter adapter;

    String [] ids;
    String [] titulos;
    String [] estatus;
    String [] fecha;
    Integer [] ico;



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
        /*ids=searchAll(filtro).get(0);
        titulos=searchAll(filtro).get(1);
        estatus=searchAll(filtro).get(2);
        fecha=searchAll(filtro).get(3);
        ico= new Integer[estatus.length];
        for(int s= 0; s<estatus.length;s++){
            if(Integer.parseInt(estatus[s])==0){
                ico[s]=R.drawable.cancelar;
            }else{
                ico[s]=R.drawable.terminado;
            }
        }
        adapter= new ListAdapter((Activity)getContext(),titulos,fecha,ico);*/
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
                        if(searchTask(filtro,false)!=null){
                            ids=searchTask(filtro,false).get(0);
                            Log.e("IDS ",ids[0]+""+ids[1]);
                            titulos=searchTask(filtro,false).get(1);
                            Log.e("Titulos ",titulos[0]+""+titulos[1]);
                            estatus=searchTask(filtro,false).get(2);
                            Log.e("Estatus ",estatus[0]+""+estatus[1]);
                            fecha=searchTask(filtro,false).get(3);
                            Log.e("Fechas ",fecha[0]+""+fecha[1]);
                            ico= new Integer[estatus.length];
                            for(int s= 0; s<estatus.length;s++){
                                if(Integer.parseInt(estatus[s])==0){
                                    ico[s]=R.drawable.cancelar;
                                }else{
                                    ico[s]=R.drawable.terminado;
                                }
                            }
                            adapter= new ListAdapter((Activity)getContext(),titulos,fecha,ico);
                        }
                        else{
                            Toast.makeText(getContext(),"Retorno nulo",Toast.LENGTH_LONG).show();
                        }
                        lista.setAdapter(adapter);
                        break;
                }
            }
        });
        //selectTask("2017-04-07");
        return view;
    }
    private boolean selectTask(String name){
        try {
            SQLiteDatabase db=Tareas.conexion.getReadableDatabase();
            String sql="SELECT * FROM TAREA WHERE DATE(FECH_ENTREGA)= DATE('now','+1 day')";
            Cursor result=db.rawQuery(sql,null);
            result.moveToFirst();
            String titulo=result.getString(1);
            String descripcion=result.getString(2);
            String fecha=result.getString(3);
            String notas=result.getString(4);
            String estatus=result.getString(5);
            Toast.makeText(getContext(),"Titulo "+titulo+"\nDescripcion "+descripcion+"\nFecha "+fecha+"\nNotas "+notas+"\nEstatus "+estatus,Toast.LENGTH_LONG).show();
            //sql="SELECT IMAGEN FROM IMAGEN WHERE ID_TAREA= "+id;
            //result=db.rawQuery(sql,null);
            //Toast.makeText(Principal.this,"Imagenes: "+result.getCount(),Toast.LENGTH_LONG).show();

            db.close();
        }catch (SQLException e){
            Log.e("Error :c ",e.getMessage());
            return false;
        }
        return true;
    }

    private ArrayList<String []> searchTask(EditText f,boolean caso){
        ArrayList<String[]> array = new ArrayList<String[]>();
        String id[]=null;String []t=null;String [] est= null;String fe[]=null;
        try{
            SQLiteDatabase db= Tareas.conexion.getReadableDatabase();
            String sql="";
            Cursor result;
            if(caso){
                sql="SELECT ID_TAREA,TITULO,ESTATUS,FECH_ENTREGA FROM TAREA WHERE TITULO LIKE '"+f.getText().toString()+"%'";
            }
            else{
                sql="SELECT ID_TAREA,TITULO, ESTATUS,FECH_ENTREGA FROM TAREA WHERE date(FECH_ENTREGA) = date('"+f.getText().toString()+"')";
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
             int r=0;
            while(result.moveToNext()){
                id[r]=result.getString(0);
                t[r]=result.getString(1);
                est[r]=result.getString(2);
                fe[r]=result.getString(3);
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
        return array;
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
    private ArrayList<String []> searchAll(EditText f){
        ArrayList<String[]> array = new ArrayList<String[]>();
        String id[]=null;String []t=null;String [] est= null;String fe[]=null;
        try{
            SQLiteDatabase db= Tareas.conexion.getReadableDatabase();
            String sql="SELECT ID_TAREA,TITULO,ESTATUS,FECH_ENTREGA FROM TAREA";
            Cursor result=db.rawQuery(sql,null);
            if(result.getCount()<= 0){
                Log.e("Error","El cursor es cero");
                return null;
            }
            int tam= result.getCount();
            id=new String[tam];
            t= new String[tam];
            est= new String[tam];
            fe= new String[tam];
            int r=0;
            while(result.moveToNext()){
                id[r]=result.getString(0);
                t[r]=result.getString(1);
                est[r]=result.getString(2);
                fe[r]=result.getString(3);
            }
            db.close();
        }catch (SQLException e){
            Log.e("Error",e.getMessage());
        }
        array.add(0,id);
        array.add(1,t);
        array.add(2,est);
        array.add(3,fe);
        return array;
    }

}
