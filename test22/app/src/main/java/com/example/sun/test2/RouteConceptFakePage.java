package com.example.sun.test2;
/**
 * Created by sun on 2016/7/3.
 */
import android.graphics.Color;
import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RouteConceptFakePage extends Fragment {
    private RecyclerView mRootView;
    private static int itemNumber=1;//tabPager内的carditem数量，应该动态加载赋值

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

            mRootView.setAdapter(new RouteFakePageAdapter(itemNumber));


    }

    public static Fragment newInstance() {
        return new RouteConceptFakePage();
    }

}
class RouteFakePageAdapter extends RecyclerView.Adapter<FakePageVH> {

    private final int numItems;

    public RouteFakePageAdapter(int numItems) {
        this.numItems = numItems;
    }


    @Override public FakePageVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        //動態替換路綫介紹信息
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.routeinfor_tabpager_item, viewGroup, false);
        TextView textView=(TextView)itemView.findViewById(R.id.carditem_info);
        textView.setText(User.getInstance().currentroutes.get(User.getInstance().getRoutepointer()).getIntroduce());
        Log.d("intrduce"+User.getInstance().currentroutes.get(User.getInstance().getRoutepointer()).getRoutename(),User.getInstance().currentroutes.get(User.getInstance().getRoutepointer()).getIntroduce());
        return new FakePageVH(itemView);
    }

    @Override
    public void onBindViewHolder(FakePageVH fakePageVH, int i) {
        // do nothing
    }

    @Override public int getItemCount() {
        return numItems;
    }
}
