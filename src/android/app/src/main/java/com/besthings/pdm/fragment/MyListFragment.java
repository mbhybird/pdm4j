package com.besthings.pdm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.besthings.pdm.activity.GraphicNotesActivity;
import com.besthings.pdm.activity.MyDesignActivity;
import com.besthings.pdm.activity.SettingActivity;
import com.besthings.pdm.activity.ShareActivity;
import com.besthings.pdm.entity.HomeItem;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.RecyclerAdapter;
import com.besthings.pdm.helper.MyItemTouchCallback;
import com.besthings.pdm.helper.OnRecyclerItemClickListener;
import com.besthings.pdm.utils.VibratorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class MyListFragment extends Fragment{

    private List<HomeItem> results = new ArrayList<HomeItem>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        results.add(new HomeItem(1, "扫码分享", R.drawable.pre_icon_adv));
        results.add(new HomeItem(2, "设置", R.drawable.pre_icon_adv));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RecyclerView(container.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerAdapter adapter = new RecyclerAdapter(R.layout.item_list,results);

        RecyclerView recyclerView = (RecyclerView)view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                /*
                if (vh.getLayoutPosition()!=results.size()-1) {
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(getActivity(), 70);   //震动70ms
                }*/
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                HomeItem item = results.get(vh.getLayoutPosition());
                Intent it = null;
                switch (item.getId()) {
                    case 1:
                        it = new Intent(getActivity(), ShareActivity.class);
                        it.putExtra("title", "扫码分享");
                        break;

                    case 2:
                        it = new Intent(getActivity(), SettingActivity.class);
                        it.putExtra("title", "设置");
                        break;

                    default:
                        break;
                }
                if (it != null) {
                    startActivity(it);
                }
            }
        });
    }
}
