package mx.edu.ittepic.tpdm_u3_practica2;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class VerTarea extends Fragment {
    CheckBox cb;
    EditText titulo, descripcion, notas, fecha,materia;
    Button aceptar;
    LinearLayout layout;
    ArrayList<Bitmap> imagenes= new ArrayList<Bitmap>();
    ImageView img;
    Bitmap bmp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_tarea, container, false);;
        titulo = (EditText) view.findViewById(R.id.editText9);
        layout=(LinearLayout)view.findViewById(R.id.layo2);
        cb=(CheckBox)view.findViewById(R.id.cb);
        descripcion = (EditText) view.findViewById(R.id.editText10);
        notas = (EditText) view.findViewById(R.id.editText12);
        aceptar = (Button) view.findViewById(R.id.button5);
        fecha=(EditText) view.findViewById(R.id.editText11);
        materia=(EditText)view.findViewById(R.id.materia2);
        titulo.setKeyListener(null);
        descripcion.setKeyListener(null);
        fecha.setKeyListener(null);
        notas.setKeyListener(null);
        materia.setKeyListener(null);

        //fecha.setText(ListaTareas.data[1]);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cb.isChecked()){
                    if(!updateStatus(Tareas.id)){
                        Toast.makeText(getContext(),"¡Error al actualizar el estatus!",Toast.LENGTH_LONG).show();
                        getFragmentManager().popBackStack();
                        return;
                    }else if(cb.isChecked()){
                        getFragmentManager().popBackStack();
                        return;
                    }
                }
                Tareas.id=0;

                getFragmentManager().popBackStack();
            }
        });
        if(!selectTask(Tareas.id)){
            Toast.makeText(getContext(),"Hubo un error",Toast.LENGTH_LONG).show();
        }
        Tareas.toolbar.setTitle(titulo.getText().toString());
        return view;
    }
    private boolean updateStatus(int id){
        try {
            SQLiteDatabase db= Tareas.conexion.getWritableDatabase();
            String sql="UPDATE TAREA SET ESTATUS=1 WHERE ID_TAREA="+id;
            db.execSQL(sql);
        }catch (SQLException e){
            Log.e("Error","noo");
            return false;
        }
        return true;
    }
    private boolean selectTask(int id){
        try {
            SQLiteDatabase db=Tareas.conexion.getReadableDatabase();
            String sql="SELECT * FROM TAREA WHERE ID_TAREA= "+id;
            Cursor result=db.rawQuery(sql,null);
            result.moveToFirst();
            if(result.getCount()==0){
                Log.e("Error ","Retorno nulo");
                return false;
            }
            titulo.setText(result.getString(1));
            descripcion.setText(result.getString(2));
            fecha.setText(result.getString(3));
            notas.setText(result.getString(4));
            if(Integer.parseInt(result.getString(5))==1){
                cb.setChecked(true);
                cb.setEnabled(false);
            }
            materia.setText(result.getString(6));

            sql="SELECT IMAGEN FROM IMAGEN WHERE ID_TAREA= "+id;
            result=db.rawQuery(sql,null);
            while(result.moveToNext()){
                imagenes.add(getImage(result.getBlob(0)));
            }

            for(int i=0;i<imagenes.size();i++){
                img= newImageView();
                img.setImageBitmap(imagenes.get(i));
                layout.addView(img);
            }
            db.close();
        }catch (SQLException e){
            Log.e("Error :c ",e.getMessage());
            return false;
        }
        return true;
    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    private ImageView newImageView(){
        ImageView iv = new ImageView(getContext());
        int width = 800;//ancho
        int height =600;//altura
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
        parms.setMargins(0,0,10,0);
        iv.setLayoutParams(parms);
        return iv;
    }

}
