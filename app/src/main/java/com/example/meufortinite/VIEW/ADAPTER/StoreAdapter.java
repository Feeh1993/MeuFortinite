package com.example.meufortinite.VIEW.ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meufortinite.MODEL.Store;
import com.example.meufortinite.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoreAdapter extends ArrayAdapter<Store> {

    public StoreAdapter(@NonNull Context context, int resource, @NonNull List<Store> storeList) {
        super(context, resource, storeList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //just like we did with fragments:
        //inflate a layout, wire widgets, insert data, return the layout
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.storeitemdisplay, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.imageView_storeactivity_imageurl);
        TextView manifest = convertView.findViewById(R.id.textview_storeactivity_manifestid);
        TextView rarity = convertView.findViewById(R.id.textview_storeactivity_rarity);
        TextView category = convertView.findViewById(R.id.textview_storeactivity_storecategory);
        TextView vBucks = convertView.findViewById(R.id.textview_storeactivity_vbucks);
        TextView name = convertView.findViewById(R.id.textview_storeactivity_name);

        Store currentStore = getItem(position);
        vBucks.setText(currentStore.getvBucks() + "");
        name.setText(currentStore.getName());
        rarity.setText(currentStore.getRarity());
        category.setText(currentStore.getStoreCategory());
        manifest.setText(currentStore.getManifestId() + "");
        Picasso.get().load(currentStore.getImageUrl()).into(image);

        //get the right number of $ signs

        return convertView;
    }
}

