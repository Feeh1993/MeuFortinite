package com.example.meufortinite.VIEW.ADAPTER;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meufortinite.MODEL.API.Store;
import com.example.meufortinite.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorLoja extends RecyclerView.Adapter<AdaptadorLoja.CustomViewHolder>{

    private Context context;
    private List<Store> popularList;

    public AdaptadorLoja(Context context, List<Store> popularList)
    {
        this.context = context;
        this.popularList = popularList;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adaptador_loja, null);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position)
    {

        Store currentStore = popularList.get(position);
        Log.d("AdaptLoja_","Raridade de cada skin: "+ currentStore.getRarity());

        holder.vBucks.setText(currentStore.getvBucks() + "");
        holder.name.setText(currentStore.getName());
        //holder.rarity.setText(currentStore.getRarity());
        holder.category.setText(currentStore.getStoreCategory());
        Picasso.get().load(currentStore.getImageUrl()).into(holder.image);
        if (currentStore.getRarity().contains("Quality"))
        {
            holder.lnlADPT.setBackgroundResource(R.color.epica);
        }
        else if (currentStore.getRarity().contains("Handmade")||currentStore.getRarity().contains("Sturdy"))
        {
            holder.lnlADPT.setBackgroundResource(R.color.incomum);
        }
        else if (currentStore.getRarity().contains("Rare"))
        {
            holder.lnlADPT.setBackgroundResource(R.color.raro);
        }
        //falta lendario e mitico

    }


    @Override
    public int getItemCount() {
        return (null != popularList ? popularList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        ImageView image;
        TextView rarity;
        TextView category;
        TextView vBucks;
        TextView name;
        CardView lnlADPT;

        CustomViewHolder(View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.imgSkinLoja);
            //rarity = itemView.findViewById(R.id.textview_storeactivity_rarity);
            category = itemView.findViewById(R.id.categoriaLoja);
            vBucks = itemView.findViewById(R.id.vBucksLoja);
            name = itemView.findViewById(R.id.nameLoja);
            lnlADPT = itemView.findViewById(R.id.card_view);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Toast.makeText(context, popularList.get(clickedPosition).getName(), Toast.LENGTH_LONG).show();
        }
    }
}





