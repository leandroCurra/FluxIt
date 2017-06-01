package leandrocurra.flux.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class FragmentIdQuery extends Fragment {
    TextView txtNombre,txtIdCategoria,txtNombreCategoria,txtNombreTag,txtIdTag;
    CheckBox checkStatus;
    EditText editId;
    Button btnBuscar;


    public static FragmentIdQuery newInstance(){

        FragmentIdQuery fragmentIdQuery = new FragmentIdQuery();

        return fragmentIdQuery;
    }


    public FragmentIdQuery() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_libre,container,false);
        txtNombre=(TextView)view.findViewById(R.id.txtNombre);
        txtIdCategoria=(TextView)view.findViewById(R.id.txtIdCategoria);
        txtNombreCategoria=(TextView)view.findViewById(R.id.txtNombreCategoria);
        txtNombreTag=(TextView)view.findViewById(R.id.txtNombreTag);
        txtIdTag=(TextView)view.findViewById(R.id.txtIdTag);
        checkStatus=(CheckBox)view.findViewById(R.id.idchecStatus);
        editId=(EditText)view.findViewById(R.id.editId);
        btnBuscar=(Button)view.findViewById(R.id.btnBuscar);
        checkStatus.setClickable(false);
        BuscarId();
        return view;

    }
    public void BuscarId(){
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String id= editId.getText().toString();
                if(editId.getText().length()== 0){
                    alert("Debe Ingresar un Id!!").show();
                    reiniciarCOntroles();
                    return;
                }
                BuscarPet(id);

            }

        });

    }
    public void reiniciarCOntroles(){
        txtIdCategoria.setText("");
        txtIdTag.setText("");
        txtNombre.setText("");
        txtNombreCategoria.setText("");
        txtNombreTag.setText("");
        checkStatus.setChecked(false);

    }
    private void BuscarPet(String id) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        petsInterface restClient = retrofit.create(petsInterface.class);
        Call<Pet> call = restClient.getPet(id);
        call.enqueue(new Callback<Pet>() {


            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                int x =  response.code();
                boolean tags=false;
                Log.d("test", ""+x);
                String tagName;
                String tagId;



                if(x==200){//si la respuesta es 200 consulto la respuesta

                    Pet pet=response.body();
                    txtNombre.setText(pet.getName());
                    tagName=(pet.getTags().isEmpty())?"no tags":pet.getTags().get(0).getName();
                    tagId=(pet.getTags().isEmpty())?"no tags id":String.valueOf(pet.getTags().get(0).getId());

                    txtIdTag.setText(tagId);
                    txtNombreTag.setText(tagName);
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

                }else if(x ==404){
                    alert("No se encuentra el id!!!").show();
                    reiniciarCOntroles();

                }


            }

            @Override
            public void onFailure(Call<Pet> call, Throwable t) {

            }
        });

    }
    public AlertDialog alert(String alerta){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Mensaje");
        builder.setMessage(alerta);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
               editId.setText("");
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
       return alert;
    }
}
