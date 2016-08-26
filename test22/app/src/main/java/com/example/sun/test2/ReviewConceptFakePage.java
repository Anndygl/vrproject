package com.example.sun.test2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sun on 2016/7/10.
 */
public class ReviewConceptFakePage extends Fragment {
        private RecyclerView mRootView;
        private static int itemNumber=8;//tabPager内的carditem数量，应该动态加载赋值

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);

        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = (RecyclerView) inflater.inflate(R.layout.fragment_page, container, false);
            return mRootView;
        }

        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            initRecyclerView();
        }

        private void initRecyclerView() {

            //修改itemNumber

            mRootView.setAdapter(new ReviewFakePageAdapter(User.getInstance().getCommentitemnumber()));


        }

        public static Fragment newInstance() {
            return new ReviewConceptFakePage();
        }

    }

    class ReviewFakePageAdapter extends RecyclerView.Adapter<FakePageVH> {

        private final int numItems;//tabPager内的carditem数量
         private View itemView;
        public ReviewFakePageAdapter(int numItems) {
            this.numItems = numItems;
        }

        @Override public FakePageVH onCreateViewHolder(ViewGroup viewGroup, int i) {
            Log.d(User.getInstance().currentroutes.get(User.getInstance().getRoutepointer()).getRoutename(),User.getInstance().currentroutes.get(User.getInstance().getRoutepointer()).getComments().get(i).getContent()+i);
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.review_item, viewGroup, false);
            return new FakePageVH(itemView);
        }

        @Override
        public void onBindViewHolder(FakePageVH fakePageVH, int i) {
            // do nothing
            TextView name=(TextView)itemView.findViewById(R.id.review_user_name);
            name.setText(User.getInstance().currentroutes.get(User.getInstance().getRoutepointer()).getComments().get(i).getCommentor());
            TextView date=(TextView)itemView.findViewById(R.id.review_date);
            date.setText(User.getInstance().currentroutes.get(User.getInstance().getRoutepointer()).getComments().get(i).getTime());
            TextView content=(TextView)itemView.findViewById(R.id.review_content);
            content.setText(User.getInstance().currentroutes.get(User.getInstance().getRoutepointer()).getComments().get(i).getContent());
        }

        @Override public int getItemCount() {
            return numItems;
        }
    }
