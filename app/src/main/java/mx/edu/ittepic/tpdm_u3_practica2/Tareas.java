package mx.edu.ittepic.tpdm_u3_practica2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Tareas extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Toolbar toolbar;
    NavigationView navigationView;

    ImageView img;
    Bitmap bmp;
    public static LinearLayout layo;
    final static int cons=0;

    public static ArrayList<Bitmap> imagenes= new ArrayList<Bitmap>();
    public static ConexionBD conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        conexion= new ConexionBD(this,"BD_FOTOS2",null,1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tareas próximas");
        //openFragment(0);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openFragment(1);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ListaTareas(), "Tareas próximas").commit();
        }
        ActivityCompat.requestPermissions(Tareas.this,
                new String[]{Manifest.permission.CAMERA},
                1);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Tareas.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK && requestCode==cons){
            Bundle ext= data.getExtras();
            bmp=(Bitmap)ext.get("data");
            img= newImageView();
            imagenes.add(bmp);
            img.setImageBitmap(bmp);

            layo.addView(img);
        }
        Toast.makeText(this,"Se metio al protected",Toast.LENGTH_LONG).show();
    }
    public ImageView newImageView(){
        ImageView iv = new ImageView(this);
        int width = 800;//ancho
        int height =600;//altura
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
        parms.setMargins(0,0,10,0);
        iv.setLayoutParams(parms);
        return iv;
    }
    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tareas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_salir) {
            finish();
        }
        if (id == R.id.action_acercaDe) {
            openFragment(3);
            navigationView.getMenu().getItem(3).setChecked(true);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_lista) {
            openFragment(0);
        }
        else if (id == R.id.nav_nueva) {
            openFragment(1);

        } else if (id == R.id.nav_consultar) {
            openFragment(2);

        } else if (id == R.id.nav_acercaDe) {
            openFragment(3);

        } else if (id == R.id.nav_salir) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(int n){
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        String title = "";
        switch(n){
            case 0:
                title = "Tareas próximas";
                fragmentTransaction.replace(R.id.content_frame, new ListaTareas(), title);
                break;
            case 1:
                title = "Nueva tarea";
                fragmentTransaction.replace(R.id.content_frame, new NuevaTarea(), title);
                break;
            case 2:
                title = "Consultar";
                fragmentTransaction.replace(R.id.content_frame, new Consultar(), title);
                break;
            case 3:
                title = "Acerca de...";
                fragmentTransaction.replace(R.id.content_frame, new AcercaDe(), title);
                break;
        }
        fragmentTransaction.addToBackStack(title);
        toolbar.setTitle(title);
        fragmentTransaction.commit();
    }
}
