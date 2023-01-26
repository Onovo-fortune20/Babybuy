package com.example.babybuy.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.babybuy.Model.Item;
import com.example.babybuy.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public  class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    private Context mContext;
    private List<Item> teachers;
    private OnItemClickListener mListener;

    public RecyclerAdapter(Context context, List<Item> uploads) {
        mContext = context;
        teachers = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Item currentTeacher = teachers.get(position);
        holder.nameTextView.setText(currentTeacher.getItem_name());
        holder.descriptionTextView.setText(currentTeacher.getItem_description());
        holder.priceTextView.setText(currentTeacher.getItem_price());
        Picasso.with(mContext)
                .load(currentTeacher.getImageUrl())
                .placeholder(R.drawable.babybuy202)
                .fit()
                .centerCrop()
                .into(holder.itemImageView);
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView nameTextView,descriptionTextView, priceTextView,dateTextView;
        public ImageView itemImageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTextView =itemView.findViewById ( R.id.itemTextView );
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            itemImageView = itemView.findViewById(R.id.itemImageView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Show");
            MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");

            showItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onShowItemClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteItemClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onShowItemClick(int position);
        void onDeleteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
