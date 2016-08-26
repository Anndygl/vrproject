package com.example.sun.test2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * 景区简介列表recyclerView Adapter
 */
public class ScenicListRecyViewAdapter extends RecyclerView.Adapter<ScenicListRecyViewAdapter.ScenicViewHolder>{

    private List<Scenic> scenices;
    private Context context;
    public ScenicListRecyViewAdapter(List<Scenic> scenices, Context context) {
        this.scenices = scenices;
        this.context=context;
    }


    //自定义ViewHolder类
    static class ScenicViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView cardviewitem_photo;
        TextView cardviewitem_title;
        TextView cardviewitem_desc;
        Button readMore;
        public ScenicViewHolder(final View itemView) {
            super(itemView);
            cardView= (CardView) itemView.findViewById(R.id.card_view);
            cardviewitem_photo= (ImageView) itemView.findViewById(R.id.cardviewitem_photo);
            cardviewitem_title= (TextView) itemView.findViewById(R.id.cardviewitem_title);
            cardviewitem_desc= (TextView) itemView.findViewById(R.id.cardviewitem_desc);
            readMore= (Button) itemView.findViewById(R.id.btn_more);
            //设置TextView背景为半透明
            cardviewitem_title.setBackgroundColor(Color.argb(20, 0, 0, 0));        }
    }
    @Override
    public ScenicListRecyViewAdapter.ScenicViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.scenic_item,viewGroup,false);
        ScenicViewHolder nvh=new ScenicViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(ScenicListRecyViewAdapter.ScenicViewHolder personViewHolder, int i) {
        final int j=i;
        personViewHolder.cardviewitem_photo.setImageBitmap(User.getInstance().bitmaps.get(i));
        personViewHolder.cardviewitem_title.setText(User.getInstance().currentplaces.get(i).getPlacename());
        personViewHolder.cardviewitem_desc.setText(User.getInstance().currentplaces.get(i).getIntroduce());
        //personViewHolder.cardviewitem_photo.setImageResource(scenices.get(i).getPhotoId());
        //personViewHolder.cardviewitem_title.setText(scenices.get(i).getTitle());
        //personViewHolder.cardviewitem_desc.setText(scenices.get(i).getDesc());
        personViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User.getInstance().setPlace(User.getInstance().currentplaces.get(j));
                Intent intent=new Intent(context,ScenicActivity.class);
                User.getInstance().setPlace(User.getInstance().currentplaces.get(j));
                //intent.putExtra("Scenic",scenices.get(j));
                context.startActivity(intent);

            }
        });
        personViewHolder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getInstance().setPlace(User.getInstance().currentplaces.get(j));
                Intent intent=new Intent(context,ScenicActivity.class);
                //intent.putExtra("Scenic",scenices.get(j));
                context.startActivity(intent);
            }
        });    }

    @Override
    public int getItemCount() {
        return User.getInstance().currentplaces.size();
    }
}