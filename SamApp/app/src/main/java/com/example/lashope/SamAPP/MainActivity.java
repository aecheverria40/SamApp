package com.example.lashope.SamAPP;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lashope.SamAPP.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private TextView mPrincipalText;

    //creacion de cosntantes
    private static final int REQUEST_CODE_NAME=0;
    private static final String TAG="Etiqueta";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Probando Android Studio - GitHub



        //---------------Diseño------------------------------------------//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //---------------Diseño------------------------------------------//
    }



    //---------------Diseño------------------------------------------//
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //---------------Diseño------------------------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //---------------Diseño------------------------------------------//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    //---------------Diseño------------------------------------------//
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_reservaciones) {
            // Handle the camera action
        } else if (id == R.id.nav_solicitar) {

            Intent i = ReservarAudiovisual.newIntent(MainActivity.this,"");
            //esperando a boton back
            startActivityForResult(i, 0);

        } else if (id == R.id.nav_proyectores) {

            Intent i = ConsultarProyector.newIntent(MainActivity.this,"");
            //esperando a boton back
            startActivityForResult(i, 0);

        } else if (id == R.id.nav_admin) {
            startActivity(new Intent(MainActivity.this,AdminLogin.class));
        }
        return true;
    }
    //---------------Diseño------------------------------------------//
}
