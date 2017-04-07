package mx.edu.ittepic.tpdm_u3_practica2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ListaTareas extends Fragment {

    ListAdapter adapter;


    String [] ids;
    String []titulo;
    String [] estatus;
    String [] fecha;
    String [] materias;

    Integer [] icon;

    ListView lista;
    public static String title="";
    SimpleDateFormat formatoFecha;
    Calendar c;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_tareas, container, false);
        lista = (ListView) view.findViewById(R.id.lista);
        formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        c = Calendar.getInstance();
        adapter= null;
            if(searchTask()!=null){
                ids=searchTask().get(0);
                titulo=searchTask().get(1);
                estatus=searchTask().get(2);

                fecha=searchTask().get(3);
                materias=searchTask().get(4);
                icon= new Integer[estatus.length];

                Date date1=null,date2=null;
                for(int s= 0; s<estatus.length;s++){
                    try {
                        date1 = formatoFecha.parse(formatoFecha.format(c.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        date2 = formatoFecha.parse(fecha[s]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if((date1.before(date2) || date1.equals(date2)) && Integer.parseInt(estatus[s])==0){
                        icon[s]=R.drawable.agendada;
                    }else if(Integer.parseInt(estatus[s])==1){
                        icon[s]=R.drawable.terminado;
                    }else if(date1.after(date2) && Integer.parseInt(estatus[s])==0){
                        icon[s]=R.drawable.cancelar;
                    }
            }
            adapter= new ListAdapter((Activity)getContext(),titulo,fecha,icon,materias);
        }
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //sendData(position);
                Tareas.id=Integer.parseInt(ids[position]);
                title=titulo[position];
                showTask();
            }
        });

        return view;
    }

    private void showTask() {
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();

        Tareas.toolbartitle=Tareas.toolbar.getTitle().toString();
        fragmentTransaction.replace(R.id.content_frame, new VerTarea(), "Ver tarea");
        fragmentTransaction.addToBackStack("Ver tarea");
        fragmentTransaction.commit();
    }

    /*public String [] sendData(int position){
        data = new String[2];

        data[0] = titulo[position];
        data[1] = fecha[position];

        return data;
    }*/
    private ArrayList<String []> searchTask(){
        ArrayList<String[]> array = new ArrayList<String[]>();
        String id[]=null;String []t=null;String [] est= null;String fe[]=null;String []ma=null;
        try{
            SQLiteDatabase db= Tareas.conexion.getReadableDatabase();
            String sql="";
            Cursor result;
            sql="SELECT ID_TAREA,TITULO, ESTATUS,FECH_ENTREGA,MATERIA FROM TAREA WHERE ESTATUS=0 AND date(FECH_ENTREGA) BETWEEN date('now','-4 days') AND DATE('now','+7 days') ORDER BY date(FECH_ENTREGA) ASC";
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
}
