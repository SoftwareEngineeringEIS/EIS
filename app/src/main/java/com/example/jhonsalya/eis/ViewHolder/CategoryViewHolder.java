package com.example.jhonsalya.eis.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jhonsalya.eis.Interface.ItemClickListener;
import com.example.jhonsalya.eis.R;

/**
 * Created by jhonsalya on 12/18/17.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtCategoryName;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        txtCategoryName = (TextView)itemView.findViewById(R.id.category_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

}
