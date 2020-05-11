package com.example.meufortinite.VIEW.ADAPTER;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.Amigo;
import com.example.meufortinite.MODEL.GERAL.Avatar;
import com.example.meufortinite.MODEL.GERAL.Mensagem;
import com.example.meufortinite.MODEL.GERAL.Noticia;
import com.example.meufortinite.MODEL.INTERFACE.CustomConversa;
import com.example.meufortinite.MODEL.INTERFACE.CustomNoticia;
import com.example.meufortinite.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorNoticias extends RecyclerView.Adapter<AdaptadorNoticias.ViewHolder>
{

    private ArrayList<Noticia> listNoticia;
    private Context mContext;
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    // Define listener member variable
    private CustomNoticia customNoticia;
    private long DURATION = 500;
    private boolean on_atach = true;

    public AdaptadorNoticias(Context context, ArrayList<Noticia> list, CustomNoticia customNoticia)
    {
        this.listNoticia = list;
        this.mContext = context;
        this.customNoticia= customNoticia;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adaptador_noticias, viewGroup, false);
        final AdaptadorNoticias.ViewHolder mViewHolder = new AdaptadorNoticias.ViewHolder(itemView);
        return mViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position)
    {

        // Get the data model based on position
        final Noticia noticia = listNoticia.get(position);
        //formatar horas
        Log.d("ADPTN","DADOS NOTICIA: "+ noticia.getTitulo());

        viewHolder.data.setText(noticia.getData());
        viewHolder.numViews.setText(noticia.getLikes() +" likes");
        viewHolder.titulo.setText(noticia.getTitulo());
        viewHolder.imageView.setImageResource(R.drawable.ic_id);
        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customNoticia.onShareClick(viewHolder.share,viewHolder.getAdapterPosition(),noticia);
            }
        });
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customNoticia.onLikeClick(viewHolder.like,viewHolder.getAdapterPosition(),noticia);
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("ADPTC","Enviando pelo onBindViewHolder");
                customNoticia.onClick(viewHolder.itemView, viewHolder.getAdapterPosition(),noticia);
            }
        });
        iniciarAnimacao(viewHolder.itemView,position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView)
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                on_atach = false;
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void iniciarAnimacao(View itemView, int position)
    {
        if (!on_atach)
        {
            position = -1;
        }
        boolean naoEstaNoPrimeiroItem = position == -1;
        position++;
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(itemView,"alpha",0.f,0.5f,1.0f);
        animator.setStartDelay(naoEstaNoPrimeiroItem ? DURATION / 2 : (position * DURATION / 3));
        animator.setDuration(500);
        animatorSet.play(animator);
        animator.start();
    }

    @Override
    public int getItemCount() {
        return listNoticia.size();
    }

    // Easy access to the context object in the recyclerview
    private Context getContext()
    {
        return mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        ImageButton like,share;
        View parentLayout;
        TextView titulo,data,numViews;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgCapaNoticias);
            titulo= (TextView)itemView.findViewById(R.id.txtTitulo);
            data = (TextView)itemView.findViewById(R.id.txtData);
            numViews = (TextView) itemView.findViewById(R.id.txtViews);
            like = (ImageButton) itemView.findViewById(R.id.imgLike);
            share = (ImageButton) itemView.findViewById(R.id.imgShare);


            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
