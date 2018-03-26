package com.besthings.pdm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.besthings.bean.UserPermissionRet;
import com.besthings.pdm.activity.AnalysisOfResearchActivity;
import com.besthings.pdm.activity.BuyerSampleActivity;
import com.besthings.pdm.activity.CostAccountsActivity;
import com.besthings.pdm.activity.DesignMaterialActivity;
import com.besthings.pdm.activity.GraphicNotesActivity;
import com.besthings.pdm.activity.MaterialPurchaseActivity;
import com.besthings.pdm.activity.MyApplication;
import com.besthings.pdm.activity.MyDesignActivity;
import com.besthings.pdm.activity.PersonCustomActivity;
import com.besthings.pdm.activity.SampleManagerActivity;
import com.besthings.pdm.activity.SpecialTechnicsActivity;
import com.besthings.pdm.activity.TeamClothesCustomActivity;
import com.besthings.pdm.activity.WarningTipsActivity;
import com.besthings.pdm.common.DividerGridItemDecoration;
import com.besthings.pdm.entity.HomeItem;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.RecyclerAdapter;
import com.besthings.pdm.helper.MyItemTouchCallback;
import com.besthings.pdm.helper.OnRecyclerItemClickListener;
import com.besthings.pdm.utils.ACache;
import com.besthings.pdm.utils.VibratorUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class MyGridFragment extends Fragment implements MyItemTouchCallback.OnDragListener {

    private List<HomeItem> results = new ArrayList<HomeItem>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////////////////////////////////////////////////////////
        /////////初始化数据，如果缓存中有就使用缓存中的
        ArrayList<HomeItem> items = (ArrayList<HomeItem>) ACache.get(getActivity()).getAsObject("items");
        if (items != null)
            results.addAll(items);
        else {
            MyApplication myApp = (MyApplication)getActivity().getApplication();
            List<UserPermissionRet> permissionList = myApp.userPermissionRetList;
            if (permissionList != null) {
                for (UserPermissionRet upr : permissionList) {
                    switch (upr.getModule()) {
                        case "1":
                            if (upr.getB1().equals("1") && upr.getB2().equals("1") && upr.getB3().equals("1")) {
                                results.add(new HomeItem(2, "我的设计", R.drawable.home_icon_myde));
                            } else {
                                results.add(new HomeItem(2, "我的设计", R.drawable.home_icon_myde_lock, true));
                            }

                            if (upr.getB6().equals("1")) {
                                results.add(new HomeItem(3, "款图批注", R.drawable.home_icon_lable));
                            } else {
                                results.add(new HomeItem(3, "款图批注", R.drawable.home_icon_lable_lock));
                            }

                            if (upr.getB12().equals("1")) {
                                results.add(new HomeItem(4, "设计样料", R.drawable.home_icon_mt));
                            } else {
                                results.add(new HomeItem(4, "设计样料", R.drawable.home_icon_mt_lock));
                            }
                            break;
                        case "7":
                            if (upr.getB1().equals("1") && upr.getB2().equals("1") && upr.getB3().equals("1")) {
                                results.add(new HomeItem(7, "成本核算", R.drawable.home_icon_count));
                            } else {
                                results.add(new HomeItem(7, "成本核算", R.drawable.home_icon_count_lock, true));
                            }
                            break;
                        case "8":
                            if (upr.getB1().equals("1") && upr.getB2().equals("1") && upr.getB3().equals("1")) {
                                results.add(new HomeItem(6, "样衣管家", R.drawable.home_icon_butler));
                            } else {
                                results.add(new HomeItem(6, "样衣管家", R.drawable.home_icon_butler_lock, true));
                            }
                            break;
                        case "9":
                            if (upr.getB1().equals("1") && upr.getB2().equals("1") && upr.getB3().equals("1")) {
                                results.add(new HomeItem(1, "买手样衣", R.drawable.home_icon_buyc));
                            } else {
                                results.add(new HomeItem(1, "买手样衣", R.drawable.home_icon_buyc_lock, true));
                            }
                            break;
                        case "10":
                            if (upr.getB1().equals("1") && upr.getB2().equals("1") && upr.getB3().equals("1")) {
                                results.add(new HomeItem(11, "个人定制", R.drawable.home_icon_cust));
                                results.add(new HomeItem(12, "团服定制", R.drawable.home_icon_team));
                            } else {
                                results.add(new HomeItem(11, "个人定制", R.drawable.home_icon_cust_lock, true));
                                results.add(new HomeItem(12, "团服定制", R.drawable.home_icon_team_lock));
                            }
                            break;
                        case "13":
                            if (upr.getB1().equals("1")) {
                                results.add(new HomeItem(8, "研发分析", R.drawable.home_icon_resd));
                                results.add(new HomeItem(10, "大货进度", R.drawable.home_icon_sch));
                                results.add(new HomeItem(15, "预警提示", R.drawable.home_icon_earlyw));
                            } else {
                                results.add(new HomeItem(8, "研发分析", R.drawable.home_icon_resd_lock));
                                results.add(new HomeItem(10, "大货进度", R.drawable.home_icon_sch_lock));
                                results.add(new HomeItem(15, "预警提示", R.drawable.home_icon_earlyw_lock));
                            }
                            break;
                        case "16":
                            if (upr.getB1().equals("1") && upr.getB2().equals("1")) {
                                results.add(new HomeItem(5, "特殊工艺", R.drawable.home_icon_tech));
                            } else {
                                results.add(new HomeItem(5, "特殊工艺", R.drawable.home_icon_tech_lock, true));
                            }
                            break;
                        case "18":
                            if (upr.getB1().equals("1") && upr.getB2().equals("1") && upr.getB3().equals("1")) {
                                results.add(new HomeItem(9, "物料采购", R.drawable.home_icon_shop));
                            } else {
                                results.add(new HomeItem(9, "物料采购", R.drawable.home_icon_shop_lock, true));
                            }
                            break;
                        default:
                            break;
                    }
                }

                results.add(new HomeItem(13, "智能裁床", R.drawable.home_icon_cut_lock, true));
                results.add(new HomeItem(14, "智能吊挂", R.drawable.home_icon_cs_lock, true));

                Collections.sort(results);

                /*
                results.add(new HomeItem(1, "买手样衣", R.drawable.home_icon_buyc));    //9:b1,b2,b3
                results.add(new HomeItem(2, "我的设计", R.drawable.home_icon_myde));    //1:b1,b2,b3
                results.add(new HomeItem(3, "款图批注", R.drawable.home_icon_lable));   //1:b6
                results.add(new HomeItem(4, "设计样料", R.drawable.home_icon_mt));      //1:b12
                results.add(new HomeItem(5, "特殊工艺", R.drawable.home_icon_tech));    //16:b1,b2
                results.add(new HomeItem(6, "样衣管家", R.drawable.home_icon_butler));  //8:b1,b2,b3
                results.add(new HomeItem(7, "成本核算", R.drawable.home_icon_count));   //7:b1,b2,b3
                results.add(new HomeItem(8, "研发分析", R.drawable.home_icon_resd));    //13:b1
                results.add(new HomeItem(9, "物料采购", R.drawable.home_icon_shop));    //18:b1,b2,b3
                results.add(new HomeItem(10, "大货进度", R.drawable.home_icon_sch));    //13:b1
                results.add(new HomeItem(11, "个人定制", R.drawable.home_icon_cust));    //10:b1,b2,b3
                results.add(new HomeItem(12, "团服定制", R.drawable.home_icon_team));   //10:b1,b2,b3
                results.add(new HomeItem(13, "智能裁床", R.drawable.home_icon_cut));    //
                results.add(new HomeItem(14, "智能吊挂", R.drawable.home_icon_cs));     //
                results.add(new HomeItem(15, "预警提示", R.drawable.home_icon_earlyw)); //13:b1
                */

            } else {
                results.add(new HomeItem(1, "买手样衣", R.drawable.home_icon_buyc_lock, true));
                results.add(new HomeItem(2, "我的设计", R.drawable.home_icon_myde_lock, true));
                results.add(new HomeItem(3, "款图批注", R.drawable.home_icon_lable_lock, true));
                results.add(new HomeItem(4, "设计样料", R.drawable.home_icon_mt_lock, true));
                results.add(new HomeItem(5, "特殊工艺", R.drawable.home_icon_tech_lock, true));
                results.add(new HomeItem(6, "样衣管家", R.drawable.home_icon_butler_lock, true));
                results.add(new HomeItem(7, "成本核算", R.drawable.home_icon_count_lock, true));
                results.add(new HomeItem(8, "研发分析", R.drawable.home_icon_resd_lock, true));
                results.add(new HomeItem(9, "物料采购", R.drawable.home_icon_shop_lock, true));
                results.add(new HomeItem(10, "大货进度", R.drawable.home_icon_sch_lock, true));
                results.add(new HomeItem(11, "个人定制", R.drawable.home_icon_cust_lock, true));
                results.add(new HomeItem(12, "团服定制", R.drawable.home_icon_team_lock, true));
                results.add(new HomeItem(13, "智能裁床", R.drawable.home_icon_cut_lock, true));
                results.add(new HomeItem(14, "智能吊挂", R.drawable.home_icon_cs_lock, true));
                results.add(new HomeItem(15, "预警提示", R.drawable.home_icon_earlyw_lock, true));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RecyclerView(container.getContext());
    }

    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerAdapter adapter = new RecyclerAdapter(R.layout.item_grid, results);
        recyclerView = (RecyclerView) view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));

        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(adapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(getActivity(), 70);   //震动70ms
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                HomeItem item = results.get(vh.getLayoutPosition());
                /*
                if(!item.isLock()) {
                    Toast.makeText(getActivity(), item.getId() + " " + item.getName(), Toast.LENGTH_SHORT).show();
                }*/
                if(!item.isLock()) {
                    Intent it = null;
                    switch (item.getId()) {
                        case 2:
                            it = new Intent(getActivity(), MyDesignActivity.class);
                            it.putExtra("title", "我的设计");
                            break;

                        case 3:
                            it = new Intent(getActivity(), GraphicNotesActivity.class);
                            it.putExtra("title", "款图批注");
                            break;

                        case 4:
                            it = new Intent(getActivity(), DesignMaterialActivity.class);
                            it.putExtra("title", "设计样料");
                            break;

                        case 1:
                            it = new Intent(getActivity(), BuyerSampleActivity.class);
                            it.putExtra("title", "买手样衣");
                            break;

                        case 7:
                            it = new Intent(getActivity(), CostAccountsActivity.class);
                            it.putExtra("title", "成本核算");
                            break;

                        case 6:
                            it = new Intent(getActivity(), SampleManagerActivity.class);
                            it.putExtra("title", "样衣管家");
                            break;

                        case 9:
                            it = new Intent(getActivity(), MaterialPurchaseActivity.class);
                            it.putExtra("title", "物料采购");
                            break;

                        case 5:
                            it = new Intent(getActivity(), SpecialTechnicsActivity.class);
                            it.putExtra("title", "特殊工艺");
                            break;

                        case 12:
                            it = new Intent(getActivity(), TeamClothesCustomActivity.class);
                            it.putExtra("title", "团服定制");
                            break;

                        case 8:
                            it = new Intent(getActivity(), AnalysisOfResearchActivity.class);
                            it.putExtra("title", "研发分析");
                            break;

                        case 11:
                            it = new Intent(getActivity(), PersonCustomActivity.class);
                            it.putExtra("title", "个人定制");
                            break;

                        case 15:
                            it = new Intent(getActivity(), WarningTipsActivity.class);
                            it.putExtra("title", "预警提示");
                            break;

                        default:
                            break;
                    }
                    if (it != null) {
                        startActivity(it);
                    }
                }
            }
        });
    }

    @Override
    public void onFinishDrag() {
        //存入缓存
        ACache.get(getActivity()).put("items", (ArrayList<HomeItem>) results);
    }
}