package mx.edu.ittepic.tpdm_u3_practica2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class ListaTareas extends Fragment {

    ListAdapter adapter;

    String [] tarea = {"Soy una tarea que esta bien pero bien larga     ", "Tarea 2"};
    String [] fecha = {"01/04/2017", "07/05/2017"};
    Integer [] icon = {R.drawable.cancelar, R.drawable.terminado};
    ListView lista;
    public static String data[];


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_tareas, container, false);
        lista = (ListView) view.findViewById(R.id.lista);
        adapter= new ListAdapter((Activity)getContext(),tarea,fecha,icon);
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

        data[0] = tarea[position];
        data[1] = fecha[position];

        return data;
    }
}
