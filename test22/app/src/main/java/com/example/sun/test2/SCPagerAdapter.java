package com.example.sun.test2;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

public class SCPagerAdapter extends PagerAdapter {

    Resources resources ;
    private final Random random = new Random();
    private int mSize;

    public SCPagerAdapter() {
        mSize = User.getInstance().Hotplaces.size();
    }  //滑頁總數

    public SCPagerAdapter(int count) {
        mSize = count;
    }

    @Override public int getCount() {
        return mSize;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public void destroyItem(ViewGroup view, int position, Object object) {
        //view.removeView((View) object);
        view.removeView(User.getInstance().stextviews.get(position));
    }

    //設置顯示
    @Override public Object instantiateItem(ViewGroup view, int position) {

        /*TextView textView = new TextView(view.getContext());
        textView.setClickable(true);
        textView.setText("景區名字");//動態設置名字
        textView.setBackgroundResource(R.mipmap.scenic_three);//動態設置背景圖片
        textView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(35);
        view.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return textView;
        */
        view.addView(User.getInstance().stextviews.get(position), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return User.getInstance().stextviews.get(position);
    }

    public void addItem() {
        mSize++;
        notifyDataSetChanged();
    }

    public void removeItem() {
        mSize--;
        mSize = mSize < 0 ? 0 : mSize;

        notifyDataSetChanged();
    }
}