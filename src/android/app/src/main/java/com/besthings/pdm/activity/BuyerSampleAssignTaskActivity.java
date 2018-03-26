package com.besthings.pdm.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.besthings.bean.UserListRet;
import com.besthings.pdm.R;
import com.besthings.pdm.entity.UserListViewHolder;
import com.besthings.pdm.utils.ACache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuyerSampleAssignTaskActivity extends BaseActivity {
    private static final String TAG = BuyerSampleAssignTaskActivity.class.getSimpleName();
    private int selectPosition = -1;
    ListView mListView;
    UserListAdapter mAdapter;
    List<UserListRet> mDataList = new ArrayList<>();
    RadioButton mRBtn;
    AlertDialog mPbDialog;
    String mDesigner = "";
    String mMID = "";
    String mMaker = "";
    boolean mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_sample_assign_task);

        getSupportActionBar().hide();

        mRBtn = (RadioButton) findViewById(R.id.item_buyer_sample_assign_task_rBtn);
        mListView = (ListView) findViewById(R.id.buyer_sample_assign_task_lv);
        initData();
        mAdapter = new UserListAdapter(this, mDataList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                selectPosition = position;
                mAdapter.notifyDataSetChanged();
                mDesigner = mDataList.get(position).getCode();
            }
        });
        if (getIntent().hasExtra("MID")) {
            mMID = getIntent().getStringExtra("MID");
        }


        findViewById(R.id.buyer_sample_assign_task_btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyerSampleAssignTaskActivity.this.finish();
            }
        });

        findViewById(R.id.buyer_sample_assign_task_btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPbDialog = showLoadingPB(false);
                            }
                        });
                        mMaker = ACache.get(BuyerSampleAssignTaskActivity.this).getAsString("Maker");
                        try {
                            mResult = myApp.NetHelperEx.arrangeBuyerSampleTask(mDesigner, mMID, mMaker);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPbDialog.dismiss();
                                if (mResult) {
                                    showAlert("添加记录成功");
                                } else {
                                    showAlert("添加失败");
                                }
                            }
                        });


                    }
                }).start();

            }
        });


    }

    private void initData () {
        if (mDataList != null) {
            mDataList.clear();
        }
        try {
            mDataList = myApp.NetHelperEx.getUserListBean().getRet();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return true;
    }

    public class UserListAdapter extends BaseAdapter {
        private Context context;
        private List<UserListRet> mUserList;
        private LayoutInflater mInflater;
        public UserListAdapter(Context context,List<UserListRet> mList){
            this.context = context;
            this.mUserList = mList;
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return mUserList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            UserListViewHolder viewHolder = null;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_buyer_sample_assign_task, parent, false);
                viewHolder = new UserListViewHolder();
                viewHolder.userName = (TextView)convertView.findViewById(R.id.item_buyer_sample_assign_task_tvName);
                viewHolder.userGroup = (TextView) convertView.findViewById(R.id.item_buyer_sample_assign_task_tvGroup);
                viewHolder.radioButton = (RadioButton) convertView.findViewById(R.id.item_buyer_sample_assign_task_rBtn);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (UserListViewHolder)convertView.getTag();
            }
            viewHolder.userName.setText(mUserList.get(position).getCode());
            viewHolder.userGroup.setText(mUserList.get(position).getDept());
            if(selectPosition == position){
                viewHolder.radioButton.setChecked(true);
            }
            else{
                viewHolder.radioButton.setChecked(false);
            }
            return convertView;
        }
    }

    private AlertDialog showLoadingPB(boolean state) {
        LayoutInflater inflater = getLayoutInflater();
        final View progressbar = inflater.inflate(R.layout.item_progressbar, null);
        AlertDialog.Builder normalDialog = null;
        normalDialog = new AlertDialog.Builder(this)
                .setTitle("系统提示")
                .setCancelable(state)
                .setView(progressbar);
        AlertDialog pb = normalDialog.show();
        return pb;
    }

    @Override
    public void showAlert(String message) {
        if (message.equals("添加记录成功")) {
            new AlertDialog.Builder(this)
                    .setTitle("系统提示")
                    .setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    })
                    .show();
        } else {
            super.showAlert(message);
        }
    }

}
