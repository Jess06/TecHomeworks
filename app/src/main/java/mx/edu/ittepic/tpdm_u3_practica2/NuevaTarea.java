package mx.edu.ittepic.tpdm_u3_practica2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class NuevaTarea extends Fragment {
    SimpleDateFormat formatoFecha;
    Calendar c;
    EditText fecha, titulo, descripcion, notas,materia;
    Button aceptar, cancelar,foto;
    LinearLayout layout;
    Intent intent;

    final static int cons=0;
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
        materia=(EditText) view.findViewById(R.id.materia);
        layout=(LinearLayout)view.findViewById(R.id.layo);
        Tareas.layo=layout;
        cancelar = (Button) view.findViewById(R.id.button);
        aceptar = (Button) view.findViewById(R.id.button2);
        foto=(Button)view.findViewById(R.id.foto);
        fecha=(EditText) view.findViewById(R.id.editText3);
        fecha.setKeyListener(null);
        fecha.setText("");
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tareas.toolbar.setTitle(Tareas.toolbartitle);
                Tareas.fab.show();
                getFragmentManager().popBackStack();
            }
        });
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(intent,cons);
            }
        });
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titulo.getText().toString().isEmpty() || descripcion.getText().toString().isEmpty()
                        || fecha.getText().toString().isEmpty() || notas.getText().toString().isEmpty() || materia.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Verifique que los campos este llenos",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!insertTask(titulo,descripcion,fecha,notas,materia)){
                    Toast.makeText(getContext(),"Error al insertar",Toast.LENGTH_LONG).show();
                    return;
                }
                cleanCampos();
                Toast.makeText(getContext(),"Se inserto correctamente",Toast.LENGTH_SHORT).show();
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
    private boolean insertTask(EditText t,EditText d,EditText f,EditText n, EditText m){
        try{
            SQLiteDatabase db= Tareas.conexion.getWritableDatabase();
            String sql="INSERT INTO TAREA VALUES(NULL,'<TITULO>','<DESCRIPCION>','<FECHA>','<NOTAS>',<ESTATUS>,'<MATERIA>')";
            sql=sql.replace("<TITULO>",t.getText().toString());
            sql= sql.replace("<DESCRIPCION>",d.getText().toString());
            sql= sql.replace("<FECHA>",f.getText().toString());
            sql= sql.replace("<NOTAS>",n.getText().toString());
            sql=sql.replace("<MATERIA>", m.getText().toString());
            sql= sql.replace("<ESTATUS>","0");
            db.execSQL(sql);

            sql="SELECT COUNT(ID_TAREA) FROM TAREA";
            Cursor result= db.rawQuery(sql,null);
            result.moveToFirst();
            int tam=result.getInt(0);

            if(Tareas.imagenes.size()!=0){
                for(int i=0;i<Tareas.imagenes.size();i++){
                    ContentValues cv= new ContentValues();
                    cv.put("ID_TAREA",tam);
                    cv.put("IMAGEN",getBytes(Tareas.imagenes.get(i)));
                    db.insert("IMAGEN",null,cv);
                }
            }
            sql="SELECT COUNT(ID_IMG) FROM IMAGEN WHERE ID_TAREA= "+tam;
            result=db.rawQuery(sql,null);
            result.moveToFirst();
            int numero= result.getInt(0);

            db.close();
        }catch (SQLException e){
            Log.e("Error :c ",e.getMessage());
            return false;
        }
        return true;
    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
    private void cleanCampos(){
        titulo.setText("");
        descripcion.setText("");
        fecha.setText("");
        notas.setText("");
        materia.setText("");
        layout.removeAllViews();
        Tareas.layo.removeAllViews();
        Tareas.imagenes.removeAll(Tareas.imagenes);
    }

}
