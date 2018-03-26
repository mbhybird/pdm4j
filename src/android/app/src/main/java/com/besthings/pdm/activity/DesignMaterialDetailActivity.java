package com.besthings.pdm.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;

public class DesignMaterialDetailActivity extends BaseActivity {
    private static final String TAG = DesignMaterialDetailActivity.class.getSimpleName();
    ImageView ibCapture;
    TextView tvMaterialName;
    TextView tvWidth;
    TextView tvSpecs;
    TextView tvPrice;
    TextView tvColor;
    TextView tvUnit;
    TextView tvSupplierName;
    TextView tvLinkMan;
    TextView tvSupplierPhone;
    TextView tvSupplierAddr;
    TextView tvMoneyUnit;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_material_detail);

        getSupportActionBar().hide();

        ibCapture = (ImageButton) findViewById(R.id.design_material_detail_ibCapture);
        tvMaterialName = (TextView) findViewById(R.id.design_material_detail_tvMaterialName);
        tvWidth = (TextView) findViewById(R.id.design_material_detail_tvWidth);
        tvSpecs = (TextView) findViewById(R.id.design_material_detail_tvSpecs);
        tvPrice = (TextView) findViewById(R.id.design_material_detail_tvPrice);
        tvColor = (TextView) findViewById(R.id.design_material_detail_tvColor);
        tvUnit = (TextView) findViewById(R.id.design_material_detail_tvUnit);
        tvSupplierName = (TextView) findViewById(R.id.design_material_detail_tvSupplierName);
        tvLinkMan = (TextView) findViewById(R.id.design_material_detail_tvLinkMan);
        tvSupplierPhone = (TextView) findViewById(R.id.design_material_detail_tvSupplierPhone);
        tvSupplierAddr = (TextView) findViewById(R.id.design_material_detail_tvSupplierAddr);
        tvMoneyUnit = (TextView) findViewById(R.id.design_material_detail_tvMoneyUnit);
        btnBack = (Button) findViewById(R.id.design_material_detail_btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DesignMaterialDetailActivity.this.finish();
            }
        });
        if (getIntent().hasExtra("pic")) {
            ibCapture.setScaleType(ImageView.ScaleType.FIT_CENTER);
            String pic = getIntent().getStringExtra("pic");
            if (pic.equals("")) {
                ibCapture.setImageResource(R.drawable.empty);
            } else {
                ibCapture.setImageBitmap(ImageUtil.getURLImage(pic));
            }
        }
        tvMaterialName.setText(getIntent().getStringExtra("MaterialName"));
        tvWidth.setText(getIntent().getStringExtra("Width"));
        tvSpecs.setText(getIntent().getStringExtra("Specs"));
        tvPrice.setText(getIntent().getStringExtra("Price"));
        tvMoneyUnit.setText(getIntent().getStringExtra("MoneyUnit"));
        tvColor.setText(getIntent().getStringExtra("Color"));
        tvUnit.setText(getIntent().getStringExtra("Unit"));
        tvSupplierName.setText(getIntent().getStringExtra("SupplierName"));
        tvLinkMan.setText(getIntent().getStringExtra("LinkMan"));
        tvSupplierPhone.setText(getIntent().getStringExtra("SupplierPhone"));
        tvSupplierAddr.setText(getIntent().getStringExtra("SupplierAddr"));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return true;
    }
}
