package com.besthings.pdm.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.besthings.bean.UserLoginBean;
import com.besthings.bean.UserQRBean;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ACache;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.besthings.pdm.utils.Constant.DEV_MODE;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    Button btnLogin;
    CheckBox chkRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();

        final EditText etPhoneNo = (EditText) findViewById(R.id.login_etPhoneNo);
        final EditText etPassword = (EditText) findViewById(R.id.login_etPassword);
        chkRemember = (CheckBox) findViewById(R.id.login_chkRemember);

        if (DEV_MODE) {
            etPhoneNo.setText(ACache.get(LoginActivity.this).getAsString("PhoneID"));
            etPassword.setText(ACache.get(LoginActivity.this).getAsString("Password"));
        }

        btnLogin = (Button) findViewById(R.id.login_btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final UserLoginBean userLoginBean = myApp.NetHelper.getUserLoginBean("", etPhoneNo.getText().toString(), etPassword.getText().toString());
                    if (userLoginBean != null && userLoginBean.getRes() == 0) {
                        ACache.get(LoginActivity.this).put("PhoneID", etPhoneNo.getText().toString());
                        ACache.get(LoginActivity.this).put("Password", etPassword.getText().toString());
                        if (chkRemember.isChecked()) {
                            ACache.get(LoginActivity.this).put("UserLogon", "true");
                        }
                        if (ACache.get(LoginActivity.this).get("FirstTime") == null) {
                            ACache.get(LoginActivity.this).put("FirstTime", "false");
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("系统提示")
                                    .setMessage("亲，由于您是第一次登录系统，请注意扫码设置云服务器地址!")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            ACache.get(LoginActivity.this).put("Maker", userLoginBean.getRet().getMaker());
                                            ACache.get(LoginActivity.this).put("UserName", userLoginBean.getRet().getUserName());
                                            ACache.get(LoginActivity.this).put("MobilePhone", userLoginBean.getRet().getMobilePhone());

                                            Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(it);
                                            LoginActivity.this.finish();
                                        }
                                    }).show();
                        } else {
                            ACache.get(LoginActivity.this).put("Maker", userLoginBean.getRet().getMaker());
                            ACache.get(LoginActivity.this).put("UserName", userLoginBean.getRet().getUserName());
                            ACache.get(LoginActivity.this).put("MobilePhone", userLoginBean.getRet().getMobilePhone());

                            Intent it = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(it);
                            LoginActivity.this.finish();
                        }
                    } else {
                        LoginActivity.this.showAlert("登录失败！\r\n1.请检查网络是否正常 \r\n2.账号和密码是否填写正确");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LoginActivity.this.showAlert("登录失败！\r\n1.请检查网络是否正常 \r\n2.账号和密码是否填写正确");
                }
            }
        });

        TextView tv = (TextView) findViewById(R.id.login_tvScan);
        //tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //tv.getPaint().setAntiAlias(true);
        tv.setTextColor(Color.BLUE);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(LoginActivity.this)
                        .setOrientationLocked(false)
                        .initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(this, String.format("Scanned: %s", result.getContents()), Toast.LENGTH_LONG).show();
                    UserQRBean userQRBean = myApp.NetHelper.mapper.readValue(result.getContents(), UserQRBean.class);
                    if (userQRBean != null) {
                        ACache.get(LoginActivity.this).put("Server", userQRBean.getSvr());
                        ACache.get(LoginActivity.this).put("Port", userQRBean.getPort());
                        UserLoginBean userLoginBean = myApp.NetHelper.getUserLoginBean(userQRBean.getMID(), "", "");
                        if (userLoginBean != null && userLoginBean.getRes() == 0) {
                            if (chkRemember.isChecked()) {
                                ACache.get(LoginActivity.this).put("UserLogon", "true");
                            }
                            ACache.get(LoginActivity.this).put("FirstTime", "false");
                            ACache.get(LoginActivity.this).put("Maker", userLoginBean.getRet().getMaker());
                            ACache.get(LoginActivity.this).put("UserName", userLoginBean.getRet().getUserName());
                            ACache.get(LoginActivity.this).put("MobilePhone", userLoginBean.getRet().getMobilePhone());

                            Intent it = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(it);
                            LoginActivity.this.finish();
                        } else {
                            LoginActivity.this.showAlert("扫码登录失败！\r\n1.请检查网络是否正常 \r\n2.二维码是否合法");
                        }
                    } else {
                        LoginActivity.this.showAlert("扫码登录失败！\r\n1.请检查网络是否正常 \r\n2.二维码是否合法");
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LoginActivity.this.showAlert("扫码登录失败！\r\n1.请检查网络是否正常 \r\n2.二维码是否合法");
        }
    }

}
