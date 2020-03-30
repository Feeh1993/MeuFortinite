package com.example.meufortinite.VIEW.ADAPTER;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.Amigo;
import com.example.meufortinite.MODEL.GERAL.Avatar;
import com.example.meufortinite.MODEL.GERAL.CHAT.ChatModel;
import com.example.meufortinite.MODEL.INTERFACE.CustomClick;
import com.example.meufortinite.MODEL.INTERFACE.CustomMsgeNtfc;
import com.example.meufortinite.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorChat extends RecyclerView.Adapter<AdaptadorChat.ChatListViewHolder>
{

    private ArrayList<ChatModel> listConversa;
    private Context mContext;
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    // Define listener member variable
    private CustomClick customBuscaClickList;
    private CustomMsgeNtfc customMsgeNtfc;
    private int tipo;
    private Amigo meuUsuario;
    private long DURATION = 500;
    private boolean on_atach = true;

    public AdaptadorChat(Context context, ArrayList<ChatModel> conversa, int tipo , CustomClick customBuscaClickList, CustomMsgeNtfc customMsgeNtfc)
    {
        this.tipo = tipo;
        this.listConversa = conversa;
        this.mContext = context;
        this.customBuscaClickList = customBuscaClickList;
        this.customMsgeNtfc = customMsgeNtfc;
    }

    @NonNull
    @Override
    public AdaptadorChat.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adaptador_amigo, viewGroup, false);
        final AdaptadorChat.ChatListViewHolder mViewHolder = new AdaptadorChat.ChatListViewHolder(itemView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdaptadorChat.ChatListViewHolder viewHolder, int position)
    {

        // Get the data model based on position
        final ChatModel usuario = listConversa.get(position);
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
        return listConversa.size();
    }

    // Easy access to the context object in the recyclerview
    private Context getContext()
    {
        return mContext;
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder
    {

        LinearLayout parent;
        CircleImageView imgAvatar;
        TextView txtNick;
        TextView txtUltimaConversa;

        ChatListViewHolder(@NonNull View itemView)
        {
            super(itemView);
            parent = itemView.findViewById(R.id.parentLayout);
            imgAvatar = itemView.findViewById(R.id.avatar_adpt_amigo);
            txtNick = itemView.findViewById(R.id.txtNick_adpt_Amigos);
            txtUltimaConversa = itemView.findViewById(R.id.txtUltimaConversa);
        }
    }
}