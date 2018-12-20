package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.RechargeConfigDataBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.id_rechargelayout)
    TagFlowLayout idRechargelayout;
    @BindView(R.id.cb_recharge_wx)
    CheckBox cbRechargeWx;
    @BindView(R.id.cb_recharge_zfb)
    CheckBox cbRechargeZfb;
    @BindView(R.id.tv_kindly_reminder)
    TextView tvKindlyReminder;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    private TagAdapter tagAdapter;

    private View rechargeItemView;

//    private String[] mVals = new String[]
//            {"100点", "200点", "300点 ", "400点", "500点", "600点",
//                    "700点", "800点", "900点", "1000点", "2000点",
//                    "3000点", "4000点", "5000点", "10000点"};

    private List<RechargeConfigDataBean.MapListBean> configList;

    private String topicTips;

    /**
     * 支付方式 1 微信 2 支付宝
     */
    private String paytype = "1";
    private LayoutInflater inflate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initdata() {
        cbRechargeZfb.setOnCheckedChangeListener(this);
        cbRechargeWx.setOnCheckedChangeListener(this);
        //  获取充值列表
        getConfigData();

    }

    private void initConfigList() {
        // 充值列表数据填充
        final LayoutInflater mInflater = LayoutInflater.from(RechargeActivity.this);
        tagAdapter = new TagAdapter<RechargeConfigDataBean.MapListBean>(configList) {
            @Override
            public View getView(FlowLayout parent, int position, RechargeConfigDataBean.MapListBean config) {
                rechargeItemView = mInflater.inflate(R.layout.recharge_item,
                        idRechargelayout, false);
                LinearLayout ll_recharge_item = (LinearLayout) rechargeItemView.findViewById(R.id.ll_recharge_item);
                TextView tv_token_num = (TextView) rechargeItemView.findViewById(R.id.tv_token_num);
                TextView tv_rmb_num = (TextView) rechargeItemView.findViewById(R.id.tv_rmb_num);
                tv_token_num.setText(config.getFillValue() + "点");
                String text_pay = String.format(getResources().getString(R.string.price_rmb), config.getPayPrice());
                tv_rmb_num.setText(text_pay);
                return rechargeItemView;
            }
        };
        idRechargelayout.setAdapter(tagAdapter);
        // 默认选中第一个
        tagAdapter.setSelectedList(0);

        idRechargelayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Logger.e(configList.get(position).getPayPrice().toString());
                return true;
            }
        });
        // 设置温馨提示内容 tv_kindly_reminder
        tvKindlyReminder.setText(topicTips.replace("。", "。\n"));
    }

    /**
     * 获取充值配置
     */
    private void getConfigData() {
        showProgressDialog();
        addSubscription(apiStores.getRechargeConfig(), new ApiCallback<HttpResponse<RechargeConfigDataBean>>() {
            @Override
            public void onSuccess(HttpResponse<RechargeConfigDataBean> model) {

                if (model.isStatus()) {
                    configList = model.getData().getMapList();
                    topicTips = model.getData().getReminder();
                    initConfigList();
                } else {
                    toastShow(model.getErrorDescription());
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    @Override
    protected void initTitle() {
        title.setText(R.string.recharge_center);
    }

    @OnClick({R.id.back, R.id.tv_recharge})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_recharge:
                toastShow("recharge");
//                startActivity(new Intent(RechargeActivity.this, RechargeRecordActivity.class));
                startActivity(new Intent(RechargeActivity.this, PayRecordActivity.class));
                break;
            default:
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_recharge_wx:
                if (isChecked) {
                    cbRechargeZfb.setChecked(false);
                    paytype = "1";
                } else {
                    cbRechargeZfb.setChecked(true);
                    paytype = "2";
                }
                break;
            case R.id.cb_recharge_zfb:
                if (isChecked) {
                    cbRechargeWx.setChecked(false);
                    paytype = "2";
                } else {
                    cbRechargeWx.setChecked(true);
                    paytype = "1";
                }
                break;

            default:
                break;
        }
    }
}
