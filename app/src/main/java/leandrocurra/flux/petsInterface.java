package leandrocurra.flux;

import java.util.List;

import leandrocurra.flux.negocio.Pet;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static android.R.attr.path;

/**
 * Created by leand on 30/5/2017.
 */

public interface petsInterface {


        @GET("pet/findByStatus?status=available")
        Call<List<Pet>> getPets();



    @GET("pet/{id}")
    Call<Pet> getPet(
            @Path("id")String id);


    }