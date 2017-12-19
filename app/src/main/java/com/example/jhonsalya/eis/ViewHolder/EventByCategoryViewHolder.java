package com.example.jhonsalya.eis.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jhonsalya.eis.Interface.ItemClickListener;
import com.example.jhonsalya.eis.R;
import com.squareup.picasso.Picasso;

/**
 * Created by jhonsalya on 12/18/17.
 */

public class EventByCategoryViewHolder extends RecyclerView.ViewHolder{

    View mView;
    public EventByCategoryViewHolder(View itemView){
        super(itemView);
        mView = itemView;
    }

    public void setTitle(String title){
        TextView post_title = (TextView) mView.findViewById(R.id.textTitle);
        post_title.setText(title);
    }

    public void setDesc(String desc){
        TextView post_desc = (TextView) mView.findViewById(R.id.textDescription);
        post_desc.setText(desc);
    }

    public void setImage(Context ctx, String image){
        ImageView post_image = (ImageView) mView.findViewById(R.id.eventPost);
        Picasso.with(ctx).load(image).into(post_image);
    }
}

/*public class EventByCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView event_name, event_desc;
    public ImageView event_image;

    //isinya onCLick
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public EventByCategoryViewHolder(View itemView) {
        super(itemView);

        event_name = (TextView)itemView.findViewById(R.id.event_image);
        event_desc = (TextView)itemView.findViewById(R.id.event_desc);
        event_image = (ImageView)itemView.findViewById(R.id.event_image);

        itemView.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}*/
