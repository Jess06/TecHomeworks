package mx.edu.ittepic.tpdm_u3_practica2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kon_n on 03/04/2017.
 */

public class ConexionBD extends SQLiteOpenHelper {

    public ConexionBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TAREA(ID_TAREA INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TITULO VARCHAR(100),DESCRIPCION VARCHAR(200),FECH_ENTREGA DATE, NOTAS VARCHAR(400),ESTATUS BOOL)");
        db.execSQL("CREATE TABLE IMAGEN(ID_IMG INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ID_TAREA INTEGER, IMAGEN BLOB, FOREIGN KEY(ID_TAREA) REFERENCES TAREA(ID_TAREA))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
