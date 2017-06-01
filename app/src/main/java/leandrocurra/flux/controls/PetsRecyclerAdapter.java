package leandrocurra.flux.controls;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import leandrocurra.flux.MainActivity;
import leandrocurra.flux.R;
import leandrocurra.flux.negocio.Pet;

/**
 * Created by leand on 31/5/2017.
 */

public class PetsRecyclerAdapter
        extends RecyclerView.Adapter<PetsRecyclerAdapter.PetsViewHolder> implements View.OnClickListener {
    private View.OnClickListener listener;

    List<Pet> listaPets;

    public PetsRecyclerAdapter(List<Pet> listaPets) {
        this.listaPets=listaPets;
    }

    public void setItems(List<Pet> pets) {
        this.listaPets= pets;
    }

    public static class PetsViewHolder
            extends RecyclerView.ViewHolder {

        private TextView txtNombre;


        public PetsViewHolder(View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txt_rv);
        }

        public void bindPet(Pet pet) {
            txtNombre.setText(pet.getName());
        }
    }

    @Override
    public PetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_view_rv, parent, false);
        PetsViewHolder petsViewHolder = new PetsViewHolder(itemView);
        itemView.setOnClickListener(this);

        return petsViewHolder;
    }
    public void setOnClickListener(View.OnClickListener _listener){
        this.listener=_listener;
    }

    @Override
    public void onClick(View v) {
        if(listener !=null) {
            listener.onClick(v);

        }

    }

    @Override
    public void onBindViewHolder(PetsViewHolder holder, int position) {
        Pet pet = listaPets.get(position);

        holder.bindPet(pet);
    }

    @Override
    public int getItemCount() {
        if ( listaPets== null ) {
            return 0;
        } else{
                return listaPets.size();
            }

    }
}