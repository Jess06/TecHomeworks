package mx.edu.ittepic.tpdm_u3_practica2;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jessica on 2/04/17.
 */
public class ListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] itemname;
    private final Integer[] integers;
    private final String[] description;
    private final String[] materias;
    public ListAdapter(Activity context, String[] itemname, String [] description, Integer[] integers, String [] materias) {
        super(context, R.layout.fila_lista, itemname);

        this.context=context;
        this.itemname=itemname;
        this.integers=integers;
        this.description=description;
        this.materias=materias;
    }

    public View getView(int posicion, View view, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.fila_lista,null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.texto_principal);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView etxDescripcion = (TextView) rowView.findViewById(R.id.texto_secundario);
        TextView materia=(TextView)rowView.findViewById(R.id.tvmateria);

        txtTitle.setText(itemname[posicion]);
        imageView.setImageResource(integers[posicion]);
        etxDescripcion.setText(description[posicion]);
        materia.setText("Materia: "+materias[posicion]);

        return rowView;
    }
}

