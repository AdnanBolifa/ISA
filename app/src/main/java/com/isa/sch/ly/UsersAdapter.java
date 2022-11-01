package com.isa.sch.ly;

import android.content.Context;
import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder>
{
    private ArrayList<User> users;
    private Context context;
    private  OnUserClickListener onUserClickListener;

    public UsersAdapter(ArrayList<User> users, Context context, OnUserClickListener onUserClickListener)
    {
        this.users = users;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
    }

    interface OnUserClickListener
    {
        void OnUserClicked(int position);
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.user_holder, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position)
    {
        holder.txtUsername.setText(users.get(position).getUsername());
        Glide.with(context).load(users.get(position).getProfilePicture()).error(R.drawable.user).placeholder(R.drawable.user).into(holder.imageView);
    }

    @Override
    public int getItemCount()
    {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder
    {
        TextView txtUsername;
        ImageView imageView;
        public UserHolder(@NonNull View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(view ->
            {
                onUserClickListener.OnUserClicked(getAdapterPosition());
            });
            txtUsername = itemView.findViewById(R.id.txt_username);
            imageView = itemView.findViewById(R.id.image_Profile);
        }
    }
}
