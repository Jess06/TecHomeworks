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

import java.util.ArrayList;


public class ListaTareas extends Fragment {

    ListAdapter adapter;

    //String [] tarea = {"Soy una tarea que esta bien pero bien larga     ", "Tarea 2"};
    //String [] fecha = {"01/04/2017", "07/05/2017"};
    //Integer [] icon = {R.drawable.cancelar, R.drawable.terminado};
    String [] ids;
    String []titulo;
    String [] estatus;
    String [] fecha;

    Integer [] icon;

    ListView lista;
    public static String data[];


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_tareas, container, false);
        lista = (ListView) view.findViewById(R.id.lista);
        adapter= null;
            if(searchTask().get(0)!=null){
                ids=searchTask().get(0);
                titulo=searchTask().get(1);
                estatus=searchTask().get(2);
                fecha=searchTask().get(3);
                icon= new Integer[estatus.length];
                for(int s= 0; s<estatus.length;s++){
                    if(Integer.parseInt(estatus[s])==0){
                        icon[s]=R.drawable.cancelar;
                    }else{
                        icon[s]=R.drawable.terminado;
                    }
            }
            adapter= new ListAdapter((Activity)getContext(),titulo,fecha,icon);
        }
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendData(position);
                showTask();
            }
        });

        return view;
    }

    private void showTask() {
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new VerTarea(), "Ver tarea");
        fragmentTransaction.addToBackStack("Ver tarea");
        fragmentTransaction.commit();
    }

    public String [] sendData(int position){
        data = new String[2];

        data[0] = titulo[position];
        data[1] = fecha[position];

        return data;
    }
    private ArrayList<String []> searchTask(){
        ArrayList<String[]> array = new ArrayList<String[]>();
        String id[]=null;String []t=null;String [] est= null;String fe[]=null;
        try{
            SQLiteDatabase db= Tareas.conexion.getReadableDatabase();
            String sql="";
            Cursor result;
            sql="SELECT ID_TAREA,TITULO, ESTATUS,FECH_ENTREGA FROM TAREA WHERE date(FECH_ENTREGA) BETWEEN date('now') AND DATE('now','+1 day')";
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
}
