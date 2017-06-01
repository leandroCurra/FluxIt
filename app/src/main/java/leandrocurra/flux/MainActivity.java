package leandrocurra.flux;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import leandrocurra.flux.controls.PetsRecyclerAdapter;
import leandrocurra.flux.negocio.Pet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public List<Pet> listaPets;
    public static final String BASE_URL = "http://petstore.swagger.io/v2/";
    RecyclerView recyclerView;
    PetsRecyclerAdapter adaptador;
    ProgressDialog progresDailog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progresDailog = new ProgressDialog(MainActivity.this);
        recyclerView= (RecyclerView)findViewById(R.id.rv);
        adaptador= new PetsRecyclerAdapter(listaPets);


        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //obtengo el id y se lo paso a la activity.
                int index = recyclerView.getChildAdapterPosition(v);

                String ID= String.valueOf(listaPets.get(index).getId());
                Intent i = new Intent(MainActivity.this,DetailActivity.class);
                i.putExtra("ID",ID);
                startActivity(i);


            }
        });


        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));


        cargarApi();



    }
    @Override
    protected void onStart() {
        super.onStart();
        if (listaPets == null){
            progresDailog.setMessage("Leyendo Web Service");
            progresDailog.setCancelable(false);
            progresDailog.show();
        }

    }


    private void cargarApi() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        petsInterface restClient = retrofit.create(petsInterface.class);
Call<List<Pet>> call = restClient.getPets();
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                //obtengo la respuesta y actualizo el RecyclerView
                listaPets= response.body();
                adaptador.setItems(listaPets);
                adaptador.notifyDataSetChanged();

                progresDailog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Log.d("error response", t.toString());
                progresDailog.dismiss();

            }
        });

    }

}