package leandrocurra.flux;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import leandrocurra.flux.controls.TabsAdapter;
import leandrocurra.flux.negocio.Pet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static leandrocurra.flux.MainActivity.BASE_URL;

public class DetailActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
public static String ID="0";
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private GoogleApiClient apiClient;
    public static  String Latitude="desconodicda";
    public static  String Longitud="desconocida";

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        TabLayout tabLayout =(TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        toolbar = (Toolbar) findViewById(R.id.contenedorBar);
        setSupportActionBar(toolbar);
       ID= getIntent().getExtras().getString("ID");
        Log.d("leo", ID);
        cargarPet();
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.locate:
       popUp(Latitude,Longitud).show();

                break;


        }



        return super.onOptionsItemSelected(item);
    }


    private void cargarPet() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        petsInterface restClient = retrofit.create(petsInterface.class);
        Call<Pet> call = restClient.getPet(DetailActivity.ID);
        call.enqueue(new Callback<Pet>() {


            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
            int x =  response.code();
                if(x==200){//si la respuesta es 200 consulto el
                    Pet pet=response.body();
                    Log.d("leo", pet.getName());
                }


            }

            @Override
            public void onFailure(Call<Pet> call, Throwable t) {

            }
        });

    }

    private void updateUI(Location loc) {
        if (loc != null) {
           Latitude= String.valueOf(loc.getLatitude());
            Longitud =String.valueOf(loc.getLongitude());
        } else {
           Latitude= "Latitud: (desconocida)";
           Longitud ="Longitud: (desconocida)";
        }
    }



//CallBacks google Play
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e("test", "No se pudo conectar a google Play");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);
            Log.d("text", lastLocation.toString());
            updateUI(lastLocation);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);

            } else {


                Log.e("test", "Permiso denegado");
            }
        }
    }
    public AlertDialog popUp(String latitud,String Longitud){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Localización");
        builder.setMessage("Latitud: "+latitud +"\n" +
                        "Longitud: "+Longitud);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        return alert;
    }
}
