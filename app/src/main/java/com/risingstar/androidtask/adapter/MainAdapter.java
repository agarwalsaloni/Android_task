package com.risingstar.androidtask.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.risingstar.androidtask.databases.DataEntity;
import com.risingstar.androidtask.databases.Database;
import com.risingstar.androidtask.model.ModelMain;
import com.risingstar.androidtask.R;
import com.risingstar.androidtask.utility.ConnectionManager;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    Context context;
    ArrayList<ModelMain> itemList;
    public MainAdapter(@NonNull Context context , ArrayList<ModelMain> itemList){
        this.context = context ;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_single_row,parent,false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MainViewHolder holder, int position) {
        ModelMain data = itemList.get(position);

        if (new ConnectionManager().checkConnectivity(context)){
            holder.txtName.setText(data.getName());
            holder.txtCapital.setText(data.getCapital());
            holder.txtRegion.setText(data.getRegion());
            holder.txtSubregion.setText(data.getSubregion());
            holder.txtPopulation.setText(data.getPopulation());
            for(int x = 0; x<data.getBorders().size();x++){
                holder.txtBorder.append(data.getBorders().get(x) + " , ");
            }
            for(int y = 0; y<data.getLanguages().size();y++){
                holder.txtLanguages.append(data.getLanguages().get(y) + " , ");
            }
            if (new ConnectionManager().checkConnectivity(context)){
            Picasso.get().load(data.getFlag()).error(R.drawable.ic_launcher_background).into(holder.imgFlag);}
            else
            {Picasso.get().load(data.getFlag()).networkPolicy(NetworkPolicy.OFFLINE).error(R.drawable.ic_launcher_background).into(holder.imgFlag);}

        }



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtCapital;
        TextView txtRegion;
        TextView txtSubregion;
        TextView txtPopulation;
        TextView txtBorder;
        TextView txtLanguages;
        ImageView imgFlag;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
             txtName = itemView.findViewById(R.id.txtName);
             txtCapital = itemView.findViewById(R.id.txtCapital);
             txtRegion = itemView.findViewById(R.id.txtRegion);
             txtSubregion = itemView.findViewById(R.id.txtSubregion);
             txtPopulation= itemView.findViewById(R.id.txtPopulation);
             txtBorder= itemView.findViewById(R.id.txtBorder);
             txtLanguages= itemView.findViewById(R.id.txtLanguages);
             imgFlag= itemView.findViewById(R.id.imgFlag);
        }
    }

}


