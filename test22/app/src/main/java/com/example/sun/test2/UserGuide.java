package com.example.sun.test2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun on 2016/7/4.
 */
public class UserGuide extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Scenic> guideList;
    private UserGuideAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userguide);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);


        recyclerView= (RecyclerView) findViewById(R.id.guide_recyclerView);

        initPersonData();
        adapter=new UserGuideAdapter(guideList, UserGuide.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



    }

    private void initPersonData() {
        guideList =new ArrayList<>();
        //添加景区，即卡片item
        boolean add = guideList.add(new Scenic(getString(R.string.help_one_title), getString(R.string.help_one_desc), R.mipmap.help_one));
        guideList.add(new Scenic(getString(R.string.help_two_title),getString(R.string.help_two_desc),R.mipmap.help_two));
        guideList.add(new Scenic(getString(R.string.help_three_title),getString(R.string.help_three_desc),R.mipmap.scenic_three));
        guideList.add(new Scenic(getString(R.string.help_four_title),getString(R.string.help_four_desc),R.mipmap.help_four));
    }
}

