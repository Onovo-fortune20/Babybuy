package com.example.babybuy.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babybuy.Model.item;
import com.example.babybuy.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {
    private Context mContext;
    private List<item> items;
    private OnItemClickListener mListener;

    public RecycleAdapter(Context context, List<item> uploads){
        mContext = context;
        items = uploads;

    }

    @NonNull
    @Override
    public RecycleAdapter.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);
        return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.RecycleViewHolder holder, int position) {
        item currentitems = items.get(position);
        holder.itemTextView.setText(currentitems.getItem_name());
        holder.descriptionTextView.setText(currentitems.getItem_description());
        holder.priceTextView.setText(currentitems.getItem_price());
        Picasso.with(mContext)
                .load(currentitems.getImageUrl())
                .placeholder(R.drawable.babybuy2023)
                .fit()
                .centerCrop()
                .into(holder.itemImageView);


    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView itemTextView, descriptionTextView, priceTextView;
        public ImageView itemImageView;


        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTextView = itemView.findViewById(R.id.itemTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            itemImageView = itemView.findViewById(R.id.itemImageView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener !=null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);

                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem showItem = contextMenu.add (Menu.NONE, 1, 1, "Show");
            MenuItem deleteItem = contextMenu.add(Menu.NONE, 2, 2, "Delete");

            showItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){

                    switch (menuItem.getItemId()){
                        case 1:
                            mListener.onShowItemClick(position);
                            return true;
                        case 2:
                            mListener.onShowDeleteClick(position);
                            return true;


                    }
                }
            }

            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick (int position);
        void onShowItemClick (int position);
        void onShowDeleteClick (int position);

    }
    public void setOnclickListener(OnItemClickListener listener){
        mListener = listener;
    }


    }

