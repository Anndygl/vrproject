package com.example.sun.test2;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by sun on 2016/7/11.
 */
//provinceChangePagerAdapter
public class PCPagerAdapter extends PagerAdapter {

    Resources resources ;
    private final Random random = new Random();
    private int mSize;

    public PCPagerAdapter() {
        mSize = User.getInstance().Hotprovinces.size();
    }  //滑頁總數

    public PCPagerAdapter(int count) {
        mSize = count;
    }

    @Override public int getCount() {
        return mSize;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView(User.getInstance().ptextviews.get(position));
    }

    //設置顯示
    @Override public Object instantiateItem(ViewGroup view, int position) {
        view.addView(User.getInstance().ptextviews.get(position), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return User.getInstance().ptextviews.get(position);
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