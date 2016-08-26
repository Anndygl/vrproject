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
 * Created by sun on 2016/7/4.
 */
public class UserGuideAdapter  extends RecyclerView.Adapter<UserGuideAdapter.GuideViewHolder> {
    private List<Scenic> scenices;
    private Context context;
    public UserGuideAdapter(List<Scenic> scenices, Context context) {
        this.scenices = scenices;
        this.context=context;
    }


    //自定义ViewHolder类
    static class GuideViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView cardviewitem_photo;
        TextView cardviewitem_title;
        TextView cardviewitem_desc;
        // Button share; 暂时不实现每个卡片item的附加功能按钮
       // Button readMore;
        public GuideViewHolder(final View itemView) {
            super(itemView);
            cardView= (CardView) itemView.findViewById(R.id.guidecard_view);
            cardviewitem_photo= (ImageView) itemView.findViewById(R.id.guide_item_photo);
            cardviewitem_title= (TextView) itemView.findViewById(R.id.guide_item_title);
            cardviewitem_desc= (TextView) itemView.findViewById(R.id.guide_item_desc);
            //share= (Button) itemView.findViewById(R.id.btn_share);
          //  readMore= (Button) itemView.findViewById(R.id.btn_more);
            //设置TextView背景为半透明
            cardviewitem_title.setBackgroundColor(Color.argb(20, 0, 0, 0));        }


    }
    @Override
    public UserGuideAdapter.GuideViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.guide_item,viewGroup,false);
        GuideViewHolder nvh=new GuideViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(UserGuideAdapter.GuideViewHolder personViewHolder, int i) {
        final int j = i;
        personViewHolder.cardviewitem_photo.setImageResource(scenices.get(i).getPhotoId());
        personViewHolder.cardviewitem_title.setText(scenices.get(i).getTitle());
        personViewHolder.cardviewitem_desc.setText(scenices.get(i).getDesc());        //为btn_share btn_readMore cardView设置点击事件
    }

    public int getItemCount() {
        return scenices.size();
    }
}
