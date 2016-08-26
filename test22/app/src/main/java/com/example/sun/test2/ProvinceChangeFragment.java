package com.example.sun.test2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Administrator on 2016/7/17.
 */
public class ProvinceChangeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.scenic_change_item, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final ViewPager viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        PCPagerAdapter pcPagerAdapter=new PCPagerAdapter();

        viewpager.setAdapter(pcPagerAdapter);
        viewpager.getCurrentItem();
        indicator.setViewPager(viewpager);
    }
}

