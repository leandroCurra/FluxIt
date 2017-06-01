package leandrocurra.flux.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import leandrocurra.flux.DetailActivity;
import leandrocurra.flux.R;
import leandrocurra.flux.negocio.Pet;
import leandrocurra.flux.petsInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static leandrocurra.flux.MainActivity.BASE_URL;


/**
 * Created by leand on 31/5/2017.
 */

public class FragmentInfo extends Fragment {
    TextView txtNombre,txtId,txtIdCategoria,txtNombreCategoria,txtNombreTag,txtIdTag;
    CheckBox checkStatus;



private int id;
    public static FragmentInfo newInstance(){

        FragmentInfo fragmentInfo= new FragmentInfo();

        return fragmentInfo;
    }


    public FragmentInfo() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_info,container,false);
        txtId=(TextView)view.findViewById(R.id.txtId);
        txtNombre=(TextView)view.findViewById(R.id.txtNombre);
        txtIdCategoria=(TextView)view.findViewById(R.id.txtIdCategoria);
        txtNombreCategoria=(TextView)view.findViewById(R.id.txtNombreCategoria);
        txtNombreTag=(TextView)view.findViewById(R.id.txtNombreTag);
        txtIdTag=(TextView)view.findViewById(R.id.txtIdTag);
        checkStatus=(CheckBox)view.findViewById(R.id.idchecStatus);




        // txtIdCategoria,txtNombreCategoria,txtNombreTag,txtIdTag;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarPet();
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
                boolean tags=false;
                String tag;


                if(x==200){//si la respuesta es 200 consulto la respuesta

                    Pet pet=response.body();
                    txtNombre.setText(pet.getName());
                    txtId.setText(String.valueOf(pet.getId()));
                    tag=(pet.getTags().isEmpty())?"no tags":pet.getTags().get(0).getName();

                    txtIdTag.setText(tag);
                    txtNombreTag.setText(tag);
                    Log.d("leo","status = "+pet.getStatus());
                    tags=(!pet.getTags().isEmpty());
                    checkStatus.setChecked(tags);
                    checkStatus.setClickable(false);
                    if (pet.getCategory()!=null){
                        txtNombreCategoria.setText(pet.getCategory().getName());
                       txtIdCategoria.setText(String.valueOf(pet.getCategory().getId()));
                    }else{
                        txtNombreCategoria.setText("no category");
                        txtIdCategoria.setText("no category");
                    }

                }


            }

            @Override
            public void onFailure(Call<Pet> call, Throwable t) {

            }
        });

    }
}
